package com.bruleetelie.app.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.theme.BruleeGold
import com.bruleetelie.app.ui.theme.BruleeGreen
import com.bruleetelie.app.ui.theme.BruleeTheme

private enum class Destination(val label: String, val icon: String) {
    Production("Produção", "▤"), Finance("Finanças", "▣"), Orders("Pedidos", "▥"), Stock("Estoque", "▱")
}

@Composable
fun BruleeApp() {
    var destination by remember { mutableStateOf(Destination.Production) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = { BruleeTopBar(destination) },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                Destination.entries.forEach { item ->
                    NavigationBarItem(
                        selected = destination == item,
                        onClick = { destination = item },
                        icon = { Text(item.icon, style = MaterialTheme.typography.titleLarge) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedIconColor = BruleeGreen,
                        ),
                    )
                }
            }
        },
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (destination) {
                Destination.Production -> ProductionScreen()
                Destination.Finance -> FinanceScreen()
                Destination.Orders -> PlaceholderScreen("Pedidos", "Orçamentos, aprovações e histórico de vendas.")
                Destination.Stock -> PlaceholderScreen("Estoque", "Ingredientes, compras e leitura de notas fiscais.")
            }
        }
    }
}

@Composable
private fun BruleeTopBar(destination: Destination) {
    Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
        Row(
            Modifier.fillMaxWidth().statusBarsPadding().height(64.dp).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.size(38.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center,
            ) { Text("♨", color = MaterialTheme.colorScheme.onSecondaryContainer) }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("BRÛLÉE TELIE", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                Text(
                    if (destination == Destination.Production) "Production Board" else destination.label,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text("♢", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.width(12.dp))
            Box(Modifier.size(34.dp).clip(CircleShape).background(BruleeGreen), contentAlignment = Alignment.Center) {
                Text("bt", color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun ProductionScreen() {
    var firstDone by remember { mutableStateOf(true) }
    var secondDone by remember { mutableStateOf(true) }
    var thirdDone by remember { mutableStateOf(false) }
    var fourthDone by remember { mutableStateOf(false) }
    val done = listOf(firstDone, secondDone, thirdDone, fourthDone).count { it }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("●  BANCADA ATIVA", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.labelMedium)
                Text("Fila de Produção", style = MaterialTheme.typography.headlineSmall)
            }
            StatusPill("♨  8 ativas", MaterialTheme.colorScheme.primaryContainer)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("◷", "Próxima saída", "16:00 (Hoje)", Modifier.weight(1f))
            MetricCard("▧", "Fornos / cocção", "2 em andamento", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = true, onClick = {}, label = { Text("Hoje  4") })
            FilterChip(selected = false, onClick = {}, label = { Text("Amanhã  3") })
            FilterChip(selected = false, onClick = {}, label = { Text("Esta semana") })
        }
        SectionTitle("ϟ  PARA PRODUZIR HOJE", "Prioridade de entrega")
        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = Color.White), shape = RoundedCornerShape(32.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OrderHeader("#ORC-284", "16:00", "Ana Souza", "40 Brigadeiros Gourmet")
                Surface(color = MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(24.dp)) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("MICROETAPAS DA RECEITA", style = MaterialTheme.typography.labelMedium)
                            Text("$done/4 Prontas", style = MaterialTheme.typography.labelMedium, color = BruleeGreen)
                        }
                        ProductionStep("Massa cozida e pesada (18g cada)", firstDone) { firstDone = it }
                        ProductionStep("Boleamento finalizado", secondDone) { secondDone = it }
                        ProductionStep("Ornamentação e acabamento", thirdDone, active = true) { thirdDone = it }
                        ProductionStep("Embalagem cartonada e laço", fourthDone) { fourthDone = it }
                    }
                }
                Button(
                    onClick = { firstDone = true; secondDone = true; thirdDone = true; fourthDone = true },
                    Modifier.fillMaxWidth().height(54.dp),
                    shape = CircleShape,
                ) { Text("✓  Finalizar Pedido") }
            }
        }
        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = Color.White), shape = RoundedCornerShape(32.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OrderHeader("#ORC-282", "18:30", "Carla Dias", "Bolo Brûlée Red Velvet 2kg")
                Text("Massa assada • Recheio no descanso", style = MaterialTheme.typography.bodyMedium)
                LinearProgressIndicator(progress = { .5f }, Modifier.fillMaxWidth(), color = BruleeGreen)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(onClick = {}, Modifier.weight(1f)) { Text("Ver ficha") }
                    Button(onClick = {}, Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = BruleeGold)) { Text("Avançar etapa") }
                }
            }
        }
        SectionTitle("☀  AMANHÃ (MISE EN PLACE)", "3 encomendas")
        TomorrowOrder("#ORC-285", "Mariana Lima", "Cento de Macarons Sortidos", "Separar insumos")
        TomorrowOrder("#ORC-286", "Roberto Silveira", "Torta Sablée de Frutas Vermelhas", "Iniciar produção")
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun MetricCard(icon: String, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier, color = Color.White, shape = RoundedCornerShape(28.dp), shadowElevation = 1.dp) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFFFDEAD)), contentAlignment = Alignment.Center) { Text(icon) }
            Spacer(Modifier.width(10.dp))
            Column { Text(label, style = MaterialTheme.typography.labelMedium); Text(value, style = MaterialTheme.typography.titleMedium) }
        }
    }
}

@Composable
private fun ProductionStep(label: String, checked: Boolean, active: Boolean = false, onChecked: (Boolean) -> Unit) {
    Surface(color = if (active && !checked) Color(0xFFFFF3DD) else Color.White, shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked, onChecked)
            Text(
                label,
                Modifier.weight(1f),
                textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                color = if (checked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            )
            if (active && !checked) StatusPill("Agora", BruleeGold, Color.White)
        }
    }
}

@Composable
private fun OrderHeader(code: String, time: String, customer: String, product: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(50.dp).clip(RoundedCornerShape(18.dp)).background(Color(0xFFF3D6B7)), contentAlignment = Alignment.Center) { Text("🧁") }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusPill(code, Color(0xFFFFDEAD))
                Text(time, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
            }
            Text(customer, style = MaterialTheme.typography.titleMedium)
            Text(product, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun TomorrowOrder(code: String, customer: String, product: String, action: String) {
    Surface(color = Color.White, shape = RoundedCornerShape(28.dp), shadowElevation = 1.dp) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("$code  •  Amanhã", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            Text(customer, style = MaterialTheme.typography.titleMedium)
            Text(product, color = MaterialTheme.colorScheme.onSurfaceVariant)
            FilledTonalButton(onClick = {}, Modifier.fillMaxWidth()) { Text(action) }
        }
    }
}

@Composable
private fun FinanceScreen() {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("GESTÃO FINANCEIRA • TELIE", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
        Text("Lucratividade por Receita", style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(true, {}, label = { Text("Este mês (Outubro)") })
            FilterChip(false, {}, label = { Text("Últimos 30 dias") })
        }
        Surface(color = Color.White, shape = RoundedCornerShape(32.dp)) {
            Column(Modifier.fillMaxWidth().padding(18.dp)) {
                Text("FATURAMENTO TOTAL", style = MaterialTheme.typography.labelMedium)
                Text("R$ 8.420,00", style = MaterialTheme.typography.headlineLarge, color = BruleeGreen)
                Text("Comparado a Setembro (R$ 7.135,00)", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("↗", "Lucro líquido", "R$ 3.190,00", Modifier.weight(1f))
            MetricCard("◉", "Mão de obra", "R$ 1.750,00", Modifier.weight(1f))
        }
        Surface(color = Color(0xFFFFEBC6), shape = RoundedCornerShape(28.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("⚠ Alerta: reajuste sugerido", style = MaterialTheme.typography.titleMedium)
                Text("Macaron Francês de Pistache teve impacto de 32% no quilo da farinha de amêndoas.")
                Spacer(Modifier.height(10.dp))
                Button(onClick = {}) { Text("Aplicar preço sugerido") }
            }
        }
        Text("Ouro do Ateliê", style = MaterialTheme.typography.headlineSmall)
        ProfitabilityRow("1", "Brigadeiro Crème Brûlée", "71,8%", "R$ 107,60")
        ProfitabilityRow("2", "Bolo Nozes & Baba de Moça", "62,7%", "R$ 137,90")
        ProfitabilityRow("3", "Caixa Telie Degustação", "61,1%", "R$ 41,60")
        Button(onClick = {}, Modifier.fillMaxWidth().height(52.dp), shape = CircleShape) { Text("Exportar Relatório PDF") }
    }
}

@Composable
private fun ProfitabilityRow(rank: String, name: String, margin: String, profit: String) {
    Surface(color = Color.White, shape = RoundedCornerShape(28.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFFFDEAD)), contentAlignment = Alignment.Center) { Text(rank, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) { Text(name, fontWeight = FontWeight.SemiBold); Text("Lucro/unid. $profit", style = MaterialTheme.typography.bodyMedium) }
            Text(margin, fontWeight = FontWeight.Bold, color = BruleeGreen)
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String, description: String) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SectionTitle(title: String, supporting: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
        Text(supporting, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun StatusPill(text: String, background: Color, foreground: Color = MaterialTheme.colorScheme.onSurface) {
    Surface(color = background, shape = CircleShape) { Text(text, Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = foreground, style = MaterialTheme.typography.labelMedium) }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun BruleePreview() { BruleeTheme { BruleeApp() } }
