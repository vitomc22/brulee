package com.bruleetelie.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.MetricStatCard
import com.bruleetelie.app.ui.components.StatusPill
import com.bruleetelie.app.ui.components.TactilePrimaryButton
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.domain.ProductionOrder
import com.bruleetelie.domain.ProductionStatus

@Composable
fun ProductionBoardScreen(
    orders: List<ProductionOrder>,
    onStepChecked: (orderId: String, stepId: String, isChecked: Boolean) -> Unit,
    onFinalizeOrder: (ProductionOrder) -> Unit,
    onNewQuoteClicked: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Hoje") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status & Quick Pulse Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(BruleeSecondary)
                    )
                    Text(
                        "BANCADA ATIVA",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BruleeSecondary
                    )
                }
                Text("Fila de Produção", style = MaterialTheme.typography.headlineMedium)
            }
            StatusPill(
                text = "♨ ${orders.count { it.status != ProductionStatus.Completed }} ativas",
                background = BruleePrimaryFixed,
                foreground = BruleePrimary
            )
        }

        // Ambient Micro-Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard(
                icon = "◷",
                label = "Próxima Saída",
                value = "16:00 (Hoje)",
                modifier = Modifier.weight(1f)
            )
            MetricStatCard(
                icon = "♨",
                label = "Fornos / Cocção",
                value = "2 em andamento",
                modifier = Modifier.weight(1f),
                iconBackground = BruleePrimaryFixed.copy(alpha = 0.5f)
            )
        }

        // Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "Hoje",
                onClick = { selectedFilter = "Hoje" },
                label = { Text("Hoje (${orders.size})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BruleeSecondary,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = selectedFilter == "Amanhã",
                onClick = { selectedFilter = "Amanhã" },
                label = { Text("Amanhã (3)") }
            )
            FilterChip(
                selected = selectedFilter == "Esta semana",
                onClick = { selectedFilter = "Esta semana" },
                label = { Text("Esta semana") }
            )
        }

        // Section Title: PARA PRODUZIR HOJE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("⚡", color = BruleeSecondary)
                Text(
                    "PARA PRODUZIR HOJE",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = BruleeSecondary
                )
            }
            Text(
                "Prioridade de entrega",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Active Orders
        orders.forEach { order ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = BruleeSurfaceContainerLowest),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Card Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(BruleeSecondaryFixed.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🧁", style = MaterialTheme.typography.titleLarge)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                StatusPill(
                                    text = order.quoteCode,
                                    background = BruleeSecondaryFixed,
                                    foreground = Color(0xFF604100)
                                )
                                Text(
                                    text = order.deliveryTimeFormatted,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = order.customerName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = order.productName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Microetapas da Receita (Checklist)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = BruleeSurfaceContainerLow,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "MICROETAPAS DA RECEITA",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "${order.completedStepsCount}/${order.totalStepsCount} Prontas",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = BruleePrimary
                                )
                            }

                            order.steps.forEach { step ->
                                Surface(
                                    color = if (step.isCurrent && !step.isCompleted) Color(0xFFFFF3DD) else BruleeSurfaceContainerLowest,
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = step.isCompleted,
                                            onCheckedChange = { checked ->
                                                onStepChecked(order.id, step.id, checked)
                                            },
                                            colors = CheckboxDefaults.colors(checkedColor = BruleePrimary)
                                        )
                                        Text(
                                            text = step.title,
                                            modifier = Modifier.weight(1f),
                                            style = MaterialTheme.typography.bodyMedium,
                                            textDecoration = if (step.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                            color = if (step.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else BruleeOnSurface
                                        )
                                        if (step.isCurrent && !step.isCompleted) {
                                            StatusPill("Agora", BruleeSecondary, Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // CTA to finalize order and launch lesson XP screen
                    TactilePrimaryButton(
                        text = "✓  Finalizar Pedido",
                        onClick = { onFinalizeOrder(order) }
                    )
                }
            }
        }

        // Section: AMANHÃ (MISE EN PLACE)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("☀", color = BruleeSecondary)
                Text(
                    "AMANHÃ (MISE EN PLACE)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = BruleeSecondary
                )
            }
            Text("3 encomendas", style = MaterialTheme.typography.bodySmall)
        }

        TomorrowOrderCard("#ORC-285", "Mariana Lima", "Cento de Macarons Sortidos", "Separar insumos")
        TomorrowOrderCard("#ORC-286", "Roberto Silveira", "Torta Sablée de Frutas Vermelhas", "Iniciar produção")

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun TomorrowOrderCard(code: String, customer: String, product: String, action: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BruleeSurfaceContainerLowest,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "$code  •  Amanhã",
                style = MaterialTheme.typography.labelSmall,
                color = BruleeSecondary,
                fontWeight = FontWeight.Bold
            )
            Text(customer, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(product, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            FilledTonalButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = BruleeSurfaceContainerHigh)
            ) {
                Text(action, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}
