package com.bruleetelie.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bruleetelie.data.BruleeRepository
import com.bruleetelie.domain.CostComparisonResult
import com.bruleetelie.domain.ProductionOrder
import com.bruleetelie.app.ui.screens.OcrItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class Destination(val label: String, val icon: String) {
    Production("Produção", "▤"),
    Wizard("Novo Orçamento", "➕"),
    Finance("Finanças", "▣"),
    Stock("Estoque", "📷"),
    Backup("Backup", "↥"),
    Shopping("Compras", "🛒")
}

sealed interface ActiveFlow {
    data object None : ActiveFlow
    data class OrderCompleted(val order: ProductionOrder, val comparison: CostComparisonResult) : ActiveFlow
}

data class BruleeUiState(
    val destination: Destination = Destination.Production,
    val activeFlow: ActiveFlow = ActiveFlow.None,
    val ocrItems: List<OcrItem>? = null
)

@HiltViewModel
class BruleeAppViewModel @Inject constructor(
    application: Application,
    val repository: BruleeRepository
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(BruleeUiState())
    val uiState: StateFlow<BruleeUiState> = _uiState.asStateFlow()

    fun navigate(destination: Destination) = _uiState.value.copy(destination = destination).also { _uiState.value = it }
    fun showOrderCompleted(order: ProductionOrder, comparison: CostComparisonResult) {
        _uiState.value = _uiState.value.copy(activeFlow = ActiveFlow.OrderCompleted(order, comparison))
    }
    fun closeActiveFlow() { _uiState.value = _uiState.value.copy(activeFlow = ActiveFlow.None) }
    fun setOcrItems(items: List<OcrItem>?) { _uiState.value = _uiState.value.copy(ocrItems = items) }
}
