package com.bruleetelie.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.screens.*
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.data.BruleeRepository
import com.bruleetelie.documents.DefaultDocumentGateway
import com.bruleetelie.domain.CostComparisonResult
import com.bruleetelie.domain.PricingEngine
import com.bruleetelie.domain.ProductionOrder

private enum class Destination(val label: String, val icon: String) {
    Production("Produção", "▤"),
    Wizard("Novo Orçamento", "➕"),
    Finance("Finanças", "▣"),
    Stock("Estoque", "📷")
}

private sealed interface ActiveFlow {
    data object None : ActiveFlow
    data class OrderCompleted(val order: ProductionOrder, val comparison: CostComparisonResult) : ActiveFlow
}

@Composable
fun BruleeApp() {
    val repository = remember { BruleeRepository() }
    val documentGateway = remember { DefaultDocumentGateway() }

    val orders by repository.productionOrders.collectAsState()
    val sales by repository.completedSales.collectAsState()
    val settings by repository.settings.collectAsState()

    var destination by remember { mutableStateOf(Destination.Production) }
    var activeFlow by remember { mutableStateOf<ActiveFlow>(ActiveFlow.None) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = { BruleeTopBar(destination) },
        bottomBar = {
            if (activeFlow == ActiveFlow.None) {
                NavigationBar(
                    containerColor = BruleeSurfaceContainerLowest,
                    tonalElevation = 4.dp
                ) {
                    Destination.entries.forEach { item ->
                        val isSelected = destination == item
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { destination = item },
                            icon = {
                                Text(
                                    item.icon,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = if (isSelected) BruleePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            label = {
                                Text(
                                    item.label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = BruleePrimaryFixed,
                                selectedIconColor = BruleePrimary,
                                selectedTextColor = BruleePrimary
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val flow = activeFlow) {
                is ActiveFlow.OrderCompleted -> {
                    ProductionCompletionScreen(
                        productName = flow.order.productName,
                        comparison = flow.comparison,
                        onContinue = { activeFlow = ActiveFlow.None }
                    )
                }
                ActiveFlow.None -> {
                    when (destination) {
                        Destination.Production -> {
                            ProductionBoardScreen(
                                orders = orders,
                                onStepChecked = { orderId, stepId, isChecked ->
                                    repository.updateStepStatus(orderId, stepId, isChecked)
                                },
                                onFinalizeOrder = { order ->
                                    // Complete order with savings comparison
                                    val comparison = PricingEngine.compareEstimatedVsActual(
                                        estimated = order.estimatedCosts,
                                        actual = order.actualCosts ?: order.estimatedCosts,
                                        salePrice = order.salePrice
                                    )
                                    repository.completeOrder(order.id, order.actualCosts)
                                    activeFlow = ActiveFlow.OrderCompleted(order, comparison)
                                },
                                onNewQuoteClicked = { destination = Destination.Wizard }
                            )
                        }

                        Destination.Wizard -> {
                            NewQuoteWizardScreen(
                                operatingSettings = settings,
                                onQuoteApproved = { quote ->
                                    repository.addQuote(quote)
                                    repository.approveQuote(quote.id)
                                    destination = Destination.Production
                                },
                                onSendWhatsApp = { quote ->
                                    repository.addQuote(quote)
                                },
                                onClose = { destination = Destination.Production }
                            )
                        }

                        Destination.Finance -> {
                            FinanceDashboardScreen(
                                sales = sales,
                                onExportPdf = { /* Export handled via DocumentGateway */ }
                            )
                        }

                        Destination.Stock -> {
                            OcrReviewScreen(
                                onFinishReview = { destination = Destination.Production }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BruleeTopBar(destination: Destination) {
    Surface(
        color = BruleeSurface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BruleeSecondaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text("♨", color = BruleeOnSecondaryContainer, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "BRÛLÉE TELIE",
                    style = MaterialTheme.typography.labelSmall,
                    color = BruleeSecondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    when (destination) {
                        Destination.Production -> "Production Board"
                        Destination.Wizard -> "Novo Orçamento"
                        Destination.Finance -> "Gestão Financeira"
                        Destination.Stock -> "Estoque & OCR"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Box(
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(BruleePrimaryFixed),
                contentAlignment = Alignment.Center
            ) {
                Text("bt", color = BruleePrimary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}
