package com.bruleetelie.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import com.bruleetelie.app.ui.theme.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.*
import com.bruleetelie.domain.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private fun deliveryDay(value: String): LocalDate? {
    val date = value.trim().take(10)
    return runCatching { LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")) }.getOrNull()
        ?: runCatching { LocalDate.parse(date) }.getOrNull()
        ?: when {
            value.contains("amanhã", true) -> LocalDate.now().plusDays(1)
            value.contains("hoje", true) -> LocalDate.now()
            else -> null
        }
}

@Composable
fun ProductionBoardScreen(
    orders: List<ProductionOrder>,
    onStepChecked: (orderId: String, stepId: String, isChecked: Boolean) -> Unit,
    onFinalizeOrder: (ProductionOrder) -> Unit,
    onNewQuoteClicked: () -> Unit
) {
    var selectedFilter by rememberSaveable { mutableStateOf("Em aberto") }
    var finishingId by rememberSaveable { mutableStateOf<String?>(null) }
    val active = orders.filter { it.status == ProductionStatus.Pending || it.status == ProductionStatus.InProgress }
    val today = LocalDate.now()
    val filtered = when (selectedFilter) {
        "Hoje" -> active.filter { deliveryDay(it.deliveryTimeFormatted)?.let { date -> date <= today } == true }
        "Amanhã" -> active.filter { deliveryDay(it.deliveryTimeFormatted) == today.plusDays(1) }
        "Esta semana" -> active.filter { deliveryDay(it.deliveryTimeFormatted)?.let { date -> date >= today && date <= today.plusDays(6) } == true }
        "Concluídos" -> orders.filter { it.status == ProductionStatus.Completed }
        else -> active
    }.sortedWith(compareBy<ProductionOrder> { deliveryDay(it.deliveryTimeFormatted) ?: LocalDate.MAX }.thenBy { it.deliveryTimeFormatted })
    val next = active.minByOrNull { deliveryDay(it.deliveryTimeFormatted) ?: LocalDate.MAX }

    Column(
        Modifier.fillMaxSize().testTag("production_board").verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("SUA BANCADA", style = MaterialTheme.typography.labelSmall, color = BruleeSecondary)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Fila de Produção", Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            StatusPill("${active.size} ativas")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricStatCard(BruleeSymbol.Clock, "Próxima entrega", next?.deliveryTimeFormatted ?: "Sem entregas", Modifier.weight(1f))
            MetricStatCard(BruleeSymbol.Flame, "Em produção", "${active.count { it.status == ProductionStatus.InProgress }} pedidos", Modifier.weight(1f))
        }
        TactilePrimaryButton("NOVO ORÇAMENTO", onNewQuoteClicked, Modifier.testTag("production_new_quote"), icon = BruleeSymbol.Plus)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Em aberto", "Hoje", "Amanhã", "Esta semana", "Concluídos").forEachIndexed { index, filter ->
                FilterChip(selectedFilter == filter, { selectedFilter = filter }, label = { Text(filter) }, modifier = Modifier.testTag("production_filter_$index"), shape = RoundedCornerShape(22.dp), colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BruleeSecondary, selectedLabelColor = androidx.compose.ui.graphics.Color.White))
            }
        }
        if (filtered.isEmpty()) {
            Surface(Modifier.fillMaxWidth().testTag("production_empty"), shape = RoundedCornerShape(24.dp), color = androidx.compose.ui.graphics.Color(0xFFE3EDDF)) {
                Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    BruleeMascot(size = 112.dp)
                    Text(if (active.isEmpty() && selectedFilter == "Em aberto") "Sua próxima receita começa aqui" else "Nenhum pedido neste filtro", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text("Crie um orçamento e aprove com o cliente para acompanhar cada etapa da produção.", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        filtered.forEach { order -> key(order.id) {
            val reduced = LocalReducedMotion.current
            val progress by animateFloatAsState(
                if (order.totalStepsCount == 0) 0f else order.completedStepsCount.toFloat() / order.totalStepsCount,
                tween(if (reduced) 0 else 320), label = "Recipe progress"
            )
            ElevatedCard(Modifier.fillMaxWidth().testTag("production_order_${order.id}").animateContentSize(tween(if (reduced) 0 else 280)), shape = RoundedCornerShape(24.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatusPill("${order.quoteCode} • ${order.status.label}")
                    Text(order.customerName, style = MaterialTheme.typography.titleLarge)
                    Text(order.productName, fontWeight = FontWeight.Bold)
                    Text("Entrega: ${order.deliveryTimeFormatted}")
                    Text("MICROETAPAS DA RECEITA", style = MaterialTheme.typography.labelLarge)
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().testTag("production_progress_${order.id}"))
                    Text("${order.completedStepsCount}/${order.totalStepsCount} Prontas")
                    order.steps.forEach { step ->
                        val color by animateColorAsState(if (step.isCompleted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface, tween(if (reduced) 0 else 240), label = "Checklist color")
                        Surface(color = color, shape = RoundedCornerShape(14.dp)) {
                            Row(Modifier.fillMaxWidth().padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(step.isCompleted, { onStepChecked(order.id, step.id, it) },
                                    modifier = Modifier.testTag("production_step_${order.id}_${step.id}"), enabled = order.status != ProductionStatus.Completed)
                                Text(step.title, Modifier.weight(1f), textDecoration = if (step.isCompleted) TextDecoration.LineThrough else null)
                            }
                        }
                    }
                    if (order.status != ProductionStatus.Completed) {
                        TactilePrimaryButton("Finalizar Pedido", { finishingId = order.id }, Modifier.testTag("production_finalize_${order.id}"), enabled = order.isAllStepsDone)
                        if (!order.isAllStepsDone) Text("Conclua as etapas para registrar os custos reais e finalizar.", style = MaterialTheme.typography.bodySmall)
                    } else Text("Custo real: ${(order.actualCosts ?: order.estimatedCosts).total.format()}")
                }
            }
        } }
    }
    orders.firstOrNull { it.id == finishingId }?.let { order ->
        ActualCostsDialog(order, onDismiss = { finishingId = null }, onConfirm = { costs, minutes ->
            finishingId = null
            onFinalizeOrder(order.copy(actualCosts = costs, activeProductionMinutes = minutes))
        })
    }
}

@Composable
private fun ActualCostsDialog(order: ProductionOrder, onDismiss: () -> Unit, onConfirm: (RecipeCosts, Int) -> Unit) {
    val costs = order.actualCosts ?: order.estimatedCosts
    val initial = listOf(costs.ingredients, costs.labor, costs.energy, costs.packaging, costs.overhead)
    val values = remember(order.id) { initial.map { mutableStateOf(it.toBigDecimal().toPlainString()) } }
    var minutes by rememberSaveable(order.id) { mutableStateOf("0") }
    val parsed = values.map { runCatching { Money.fromReais(it.value) }.getOrNull()?.takeIf { money -> money >= Money.Zero } }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Quanto custou produzir?") },
        text = { Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Confira os valores reais. O orçamento original será preservado.")
            OutlinedTextField(minutes, { minutes = it }, label = { Text("Tempo ativo real (minutos)") }, modifier = Modifier.fillMaxWidth().testTag("actual_minutes"))
            listOf("Ingredientes", "Mão de obra", "Gás e energia", "Embalagem", "Outros custos").forEachIndexed { index, label ->
                OutlinedTextField(values[index].value, { values[index].value = it }, label = { Text(label) }, isError = parsed[index] == null,
                    modifier = Modifier.fillMaxWidth().testTag("actual_cost_$index"), singleLine = true)
            }
        } },
        confirmButton = { TextButton(onClick = { onConfirm(RecipeCosts(parsed[0]!!, parsed[1]!!, parsed[2]!!, parsed[3]!!, parsed[4]!!), minutes.toInt()) }, enabled = parsed.all { it != null } && (minutes.toIntOrNull() ?: -1) >= 0, modifier = Modifier.testTag("production_confirm_completion")) { Text("FINALIZAR") } },
        dismissButton = { TextButton(onDismiss) { Text("Cancelar") } }
    )
}
