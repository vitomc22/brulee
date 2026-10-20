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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.bruleetelie.app.ui.components.MetricStatCard
import com.bruleetelie.app.ui.components.StatusPill
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.data.db.PriceObservationEntity
import com.bruleetelie.domain.FinancialAnalysisEngine
import com.bruleetelie.domain.RecipeProfitabilityRanking
import com.bruleetelie.domain.Money
import com.bruleetelie.domain.Quote
import com.bruleetelie.domain.QuoteStatus
import com.bruleetelie.domain.SaleSnapshot
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import com.bruleetelie.app.ui.components.*

@Composable
fun FinanceDashboardScreen(
    sales: List<SaleSnapshot>,
    quotes: List<Quote>,
    priceObservations: List<PriceObservationEntity>,
    ingredientNames: Map<String, String>,
    fixedCosts: Money = Money.Zero,
    revenueGoal: Money = Money.Zero,
    onExportCsv: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Este mês") }
    val now = System.currentTimeMillis()
    val cutoff = if (selectedFilter == "30 dias") {
        now - TimeUnit.DAYS.toMillis(30)
    } else {
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.DAY_OF_MONTH, 1)
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        calendar.timeInMillis
    }
    val filteredSales = sales.filter { it.completedAtMillis >= cutoff }
    val activeHours = filteredSales.sumOf { it.activeProductionMinutes }
        .toBigDecimal()
        .divide(BigDecimal(60), 4, RoundingMode.HALF_EVEN)
    val eligibleQuotes = quotes.count { it.status != QuoteStatus.Draft }
    val approvedQuotes = quotes.count {
        it.status == QuoteStatus.Approved || it.status == QuoteStatus.ConvertedToSale
    }
    val metrics = FinancialAnalysisEngine.calculateSummary(
        sales = filteredSales,
        totalProductionHours = activeHours,
        approvedQuotesCount = approvedQuotes,
        totalEligibleQuotesCount = eligibleQuotes
    )
    val breakEven = FinancialAnalysisEngine.calculateBreakEven(
        fixedCosts = fixedCosts,
        totalRevenue = metrics.totalRevenue,
        totalVariableCosts = metrics.totalCost
    )
    val rankings = RecipeProfitabilityRanking.calculate(filteredSales)
    val chartSales = filteredSales.sortedBy { it.completedAtMillis }
    val priceAlerts = priceObservations
        .groupBy { it.ingredientId }
        .mapNotNull { (ingredientId, observations) ->
            val lastTwo = observations.sortedByDescending { it.purchaseDateMillis }.take(2)
            if (lastTwo.size < 2) null else FinancialAnalysisEngine.analyzePriceVariation(
                ingredientName = ingredientNames[ingredientId] ?: ingredientId,
                oldPrice = Money(lastTwo[1].normalizedUnitPriceCents),
                newPrice = Money(lastTwo[0].normalizedUnitPriceCents)
            )
        }
        .filter { it.variationPercent >= BigDecimal("10.00") }
        .sortedByDescending { it.variationPercent }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("finance_dashboard")
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                "GESTÃO FINANCEIRA • TELIE",
                style = MaterialTheme.typography.labelSmall,
                color = BruleeSecondary,
                fontWeight = FontWeight.Bold
            )
            Text("Lucratividade por Receita", style = MaterialTheme.typography.headlineMedium)
        }

        // Filter chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                modifier = Modifier.testTag("finance_filter_month"),
                selected = selectedFilter == "Este mês",
                onClick = { selectedFilter = "Este mês" },
                label = { Text("Este mês") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BruleeSecondary,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                modifier = Modifier.testTag("finance_filter_30_days"),
                selected = selectedFilter == "30 dias",
                onClick = { selectedFilter = "30 dias" },
                label = { Text("Últimos 30 dias") }
            )
        }

        // Faturamento Total Hero Card
        Surface(
            modifier = Modifier.fillMaxWidth().testTag("finance_price_trend_alert"),
            color = BruleeSurfaceContainerLowest,
            shape = RoundedCornerShape(28.dp),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "FATURAMENTO TOTAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    metrics.totalRevenue.format(),
                    modifier = Modifier.testTag("finance_revenue"),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = BruleePrimary
                )
                Text(
                    "${metrics.salesCount} vendas concluídas no período",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("finance_revenue_profit_chart")
                .semantics { contentDescription = "Gráfico de receita e lucro por venda" },
            color = BruleeSurfaceContainerLowest,
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 1.dp
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Receita e lucro por venda", fontWeight = FontWeight.Bold)
                if (chartSales.isEmpty()) {
                    Text("Sem dados suficientes para o gráfico", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    SaleBars(chartSales)
                }
                Text("Verde: receita · Caramelo: lucro · Vermelho: prejuízo", style = MaterialTheme.typography.labelSmall)
            }
        }

        if (revenueGoal.cents > 0L) {
            val goalProgress = (metrics.totalRevenue.cents.toFloat() / revenueGoal.cents.toFloat())
                .coerceIn(0f, 1f)
            Surface(
                modifier = Modifier.fillMaxWidth().testTag("finance_revenue_goal"),
                color = BruleeSurfaceContainerLowest,
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Meta mensal", fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(progress = { goalProgress }, modifier = Modifier.fillMaxWidth())
                    Text("${metrics.totalRevenue.format()} de ${revenueGoal.format()}")
                }
            }
        }

        // KPIs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard(
                icon = BruleeSymbol.Trend,
                label = "Lucro líquido",
                value = metrics.totalProfit.format(),
                modifier = Modifier.weight(1f),
                iconBackground = BruleePrimaryFixed
            )
            MetricStatCard(
                icon = BruleeSymbol.Production,
                label = "Custo total",
                value = metrics.totalCost.format(),
                modifier = Modifier.weight(1f),
                iconBackground = BruleeSecondaryFixed
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard(
                icon = BruleeSymbol.Target,
                label = "Ticket médio",
                value = metrics.averageTicket.format(),
                modifier = Modifier.weight(1f),
                iconBackground = BruleePrimaryFixed
            )
            MetricStatCard(
                icon = BruleeSymbol.Percent,
                label = "Conversão",
                value = "${metrics.conversionRatePercent.stripTrailingZeros().toPlainString()}%",
                modifier = Modifier.weight(1f),
                iconBackground = BruleeSecondaryFixed
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard(
                icon = BruleeSymbol.Clock,
                label = "Lucro/hora",
                value = if (activeHours.signum() == 0) "Sem horas" else metrics.profitPerHour.format(),
                modifier = Modifier.weight(1f),
                iconBackground = BruleePrimaryFixed
            )
            MetricStatCard(
                icon = BruleeSymbol.Finance,
                label = "Ponto de equilíbrio",
                value = if (fixedCosts == Money.Zero) "Não configurado" else breakEven.breakEvenRevenue.format(),
                modifier = Modifier.weight(1f),
                iconBackground = BruleeSecondaryFixed
            )
        }

        // Smart Reajuste Alert
        val primaryAlert = priceAlerts.firstOrNull { it.isIncrease }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFFFEBC6),
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BruleeIcon(BruleeSymbol.Warning, tint = BruleeSecondary)
                    Text(
                        if (primaryAlert == null) "Preços dos insumos estáveis" else "Alerta: reajuste sugerido",
                        modifier = Modifier.testTag("finance_price_trend_status"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BruleeOnSecondaryContainer
                    )
                }
                Text(
                    if (primaryAlert == null) {
                        "Cadastre ao menos duas compras do mesmo ingrediente para acompanhar tendências."
                    } else {
                        "${primaryAlert.ingredientName} subiu ${primaryAlert.variationPercent.stripTrailingZeros().toPlainString()}% entre as duas últimas compras."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = BruleeOnSecondaryContainer
                )
                if (primaryAlert != null) {
                    Text(
                        "${primaryAlert.oldPrice.format()} → ${primaryAlert.newPrice.format()}",
                        fontWeight = FontWeight.Bold,
                        color = BruleeOnSecondaryContainer
                    )
                }
            }
        }

        // Section: Ouro do Ateliê (Ranking de Receitas)
        Text(
            "Ouro do Ateliê",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        if (rankings.isEmpty()) {
            Text(
                "Nenhuma venda concluída neste período.",
                modifier = Modifier.testTag("finance_empty_state"),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            rankings.take(10).forEachIndexed { index, sale ->
                ProfitabilityRankingRow(
                    rank = (index + 1).toString(),
                    name = sale.name,
                    margin = "${sale.marginPercent.stripTrailingZeros().toPlainString()}%",
                    profit = sale.profit.format()
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        TactilePrimaryButton("EXPORTAR VENDAS EM CSV", onExportCsv, icon = BruleeSymbol.Download)
    }
}

@Composable
private fun ProfitabilityRankingRow(rank: String, name: String, margin: String, profit: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BruleeSurfaceContainerLowest,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(BruleeSecondaryFixed),
                contentAlignment = Alignment.Center
            ) {
                Text(rank, fontWeight = FontWeight.Bold, color = Color(0xFF604100))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Lucro acumulado: $profit", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            StatusPill(margin, BruleePrimaryFixed, BruleePrimary)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FinanceDashboardScreenPreview() {
    BruleeTheme {
        FinanceDashboardScreen(
            sales = emptyList(),
            quotes = emptyList(),
            priceObservations = emptyList(),
            ingredientNames = emptyMap(),
            revenueGoal = Money.Zero,
            onExportCsv = {}
        )
    }
}

@Composable
private fun SaleBars(sales: List<SaleSnapshot>) {
    val upper = sales.maxOf { maxOf(it.salePrice.cents, it.profit.cents, 0L) }.coerceAtLeast(1L).toDouble()
    val lower = sales.minOf { minOf(it.profit.cents, 0L) }.toDouble()
    val span = upper - lower
    val progress by animateFloatAsState(1f, tween(if (LocalReducedMotion.current) 0 else 400), label = "Sales chart")
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        sales.forEach { sale ->
            Column(Modifier.fillMaxWidth().testTag("finance_sale_${sale.id}"), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(sale.productName, style = MaterialTheme.typography.labelLarge, maxLines = 2)
                Text("Receita ${sale.salePrice.format()} · Lucro ${sale.profit.format()}", style = MaterialTheme.typography.bodySmall)
                Canvas(Modifier.fillMaxWidth().height(42.dp).testTag("finance_bars_${sale.id}").semantics {
                    contentDescription = "${sale.productName}: receita ${sale.salePrice.format()}, lucro ${sale.profit.format()}"
                }) {
                    val origin = ((-lower / span) * size.width).toFloat()
                    drawLine(BruleeOutlineVariant, Offset(origin, 0f), Offset(origin, size.height), 1.dp.toPx())
                    listOf(sale.salePrice to BruleePrimary, sale.profit to if (sale.profit.cents < 0) BruleeError else BruleeSecondary).forEachIndexed { index, (money, color) ->
                        val extent = (money.cents.toDouble() / span * size.width).toFloat() * progress
                        drawRoundRect(color, Offset(minOf(origin, origin + extent), index * 23.dp.toPx()),
                            Size(kotlin.math.abs(extent), 12.dp.toPx()), CornerRadius(3.dp.toPx()))
                    }
                }
                Text(java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.forLanguageTag("pt-BR")).format(java.util.Date(sale.completedAtMillis)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
