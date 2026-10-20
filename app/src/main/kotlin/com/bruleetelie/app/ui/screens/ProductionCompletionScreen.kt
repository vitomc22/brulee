package com.bruleetelie.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.*
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.domain.CostComparisonResult

@Composable
fun ProductionCompletionScreen(
    productName: String,
    comparison: CostComparisonResult,
    reducedMotion: Boolean = false,
    onContinue: () -> Unit
) {
    var reveal by remember { mutableStateOf(reducedMotion) }
    LaunchedEffect(Unit) { reveal = true }
    AnimatedVisibility(reveal, enter = if (reducedMotion) fadeIn(initialAlpha = 1f) else fadeIn() + scaleIn(initialScale = .96f)) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp)) {
            BruleeMascot(size = 110.dp, reducedMotion = reducedMotion)
            Text("Produção concluída", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(productName, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium)
            Text("A venda foi registrada nas finanças. Confira o resultado desta produção.", textAlign = TextAlign.Center)
            Surface(Modifier.fillMaxWidth(), color = BruleeSurfaceContainerLowest, shape = RoundedCornerShape(24.dp)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Custo Real vs. Orçado", fontWeight = FontWeight.Bold)
                    CompletionAmount("Custo previsto", comparison.estimatedCosts.total.format(), "completion_estimated_cost")
                    CompletionAmount("Custo real", comparison.actualCosts.total.format(), "completion_actual_cost")
                    HorizontalDivider(color = BruleeSurfaceContainerHigh)
                    CompletionAmount("Valor da venda", comparison.salePrice.format(), "completion_sale_price")
                    CompletionAmount("Lucro real", comparison.actualProfit.format(), "completion_profit")
                    Text("Margem real: ${comparison.actualMarginPercent.toPlainString().replace('.', ',')}%", color = BruleePrimary)
                }
            }
            Surface(Modifier.fillMaxWidth(), color = if (comparison.isCostHigher) BruleeErrorContainer else BruleePrimaryFixed,
                shape = RoundedCornerShape(18.dp)) {
                Text(if (comparison.isCostHigher) "Custo acima do previsto: ${comparison.costDifference.abs().format()}"
                    else "Economia em relação ao previsto: ${comparison.costDifference.abs().format()}", Modifier.padding(16.dp))
            }
            TactilePrimaryButton("CONTINUAR PARA O PAINEL", onContinue, Modifier.testTag("completion_continue"), BruleeSymbol.Check)
        }
    }
}

@Composable
private fun CompletionAmount(label: String, value: String, tag: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, Modifier.testTag(tag), fontWeight = FontWeight.Bold)
    }
}
