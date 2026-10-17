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
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.MetricStatCard
import com.bruleetelie.app.ui.components.StatusPill
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.domain.SaleSnapshot

@Composable
fun FinanceDashboardScreen(
    sales: List<SaleSnapshot>,
    onExportPdf: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Este mês") }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                selected = selectedFilter == "Este mês",
                onClick = { selectedFilter = "Este mês" },
                label = { Text("Este mês (Outubro)") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BruleeSecondary,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = selectedFilter == "30 dias",
                onClick = { selectedFilter = "30 dias" },
                label = { Text("Últimos 30 dias") }
            )
        }

        // Faturamento Total Hero Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
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
                    "R$ 8.420,00",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = BruleePrimary
                )
                Text(
                    "+18% comparado a Setembro (R$ 7.135,00)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // KPIs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard(
                icon = "↗",
                label = "Lucro líquido",
                value = "R$ 3.190,00",
                modifier = Modifier.weight(1f),
                iconBackground = BruleePrimaryFixed
            )
            MetricStatCard(
                icon = "👩‍🍳",
                label = "Mão de obra",
                value = "R$ 1.750,00",
                modifier = Modifier.weight(1f),
                iconBackground = BruleeSecondaryFixed
            )
        }

        // Smart Reajuste Alert
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
                    Text("⚠", color = BruleeSecondary)
                    Text(
                        "Alerta: reajuste sugerido",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BruleeOnSecondaryContainer
                    )
                }
                Text(
                    "Macaron Francês de Pistache teve impacto de 32% no quilo da farinha de amêndoas nos últimos 30 dias.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BruleeOnSecondaryContainer
                )
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = BruleeSecondary),
                    shape = CircleShape
                ) {
                    Text("Aplicar Preço Sugerido", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Section: Ouro do Ateliê (Ranking de Receitas)
        Text(
            "Ouro do Ateliê",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        ProfitabilityRankingRow("1", "Brigadeiro Crème Brûlée", "71,8%", "R$ 107,60")
        ProfitabilityRankingRow("2", "Bolo Nozes & Baba de Moça", "62,7%", "R$ 137,90")
        ProfitabilityRankingRow("3", "Caixa Telie Degustação", "61,1%", "R$ 41,60")

        Spacer(Modifier.height(8.dp))

        // Export Report Button
        Button(
            onClick = onExportPdf,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = BruleePrimary)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("📄")
                Text("Exportar Relatório PDF", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            }
        }
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
                Text("Lucro/unid. $profit", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            StatusPill(margin, BruleePrimaryFixed, BruleePrimary)
        }
    }
}
