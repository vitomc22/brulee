package com.bruleetelie.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
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
    Quotes("Pedidos", "▧"),
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
    val repository: BruleeRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(
        BruleeUiState(
            destination = savedStateHandle.get<String>(DESTINATION_KEY)
                ?.let { name -> Destination.entries.firstOrNull { it.name == name } }
                ?: Destination.Production,
            ocrItems = savedStateHandle.get<ArrayList<String>>(OCR_ITEMS_KEY)?.mapNotNull(::decodeOcrItem)
        )
    )
    val uiState: StateFlow<BruleeUiState> = _uiState.asStateFlow()

    fun navigate(destination: Destination) {
        savedStateHandle[DESTINATION_KEY] = destination.name
        _uiState.value = _uiState.value.copy(destination = destination)
    }
    fun showOrderCompleted(order: ProductionOrder, comparison: CostComparisonResult) {
        _uiState.value = _uiState.value.copy(activeFlow = ActiveFlow.OrderCompleted(order, comparison))
    }
    fun closeActiveFlow() { _uiState.value = _uiState.value.copy(activeFlow = ActiveFlow.None) }
    fun setOcrItems(items: List<OcrItem>?) {
        savedStateHandle[OCR_ITEMS_KEY] = items?.map(::encodeOcrItem)?.let(::ArrayList)
        _uiState.value = _uiState.value.copy(ocrItems = items)
    }

    private fun encodeOcrItem(item: OcrItem): String = listOf(
        item.id, item.rawName, item.matchedIngredient, item.packageQuantityValue,
        item.packageQuantityUnit, item.packagePriceCents.toString(), item.normalizedUnitPriceCents.toString(),
        item.priceFormatted, item.confidence, item.isConfirmed.toString()
    ).joinToString(FIELD_SEPARATOR)

    private fun decodeOcrItem(encoded: String): OcrItem? {
        val fields = encoded.split(FIELD_SEPARATOR)
        if (fields.size != 10) return null
        return runCatching {
            OcrItem(
                id = fields[0], rawName = fields[1], matchedIngredient = fields[2],
                packageQuantityValue = fields[3], packageQuantityUnit = fields[4],
                packagePriceCents = fields[5].toLong(), normalizedUnitPriceCents = fields[6].toLong(),
                priceFormatted = fields[7], confidence = fields[8], isConfirmed = fields[9].toBoolean()
            )
        }.getOrNull()
    }

    private companion object {
        const val DESTINATION_KEY = "brulee.destination"
        const val OCR_ITEMS_KEY = "brulee.ocr.items"
        const val FIELD_SEPARATOR = "\u001F"
    }
}
