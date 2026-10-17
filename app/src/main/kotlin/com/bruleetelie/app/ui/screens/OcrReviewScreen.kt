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
import com.bruleetelie.app.ui.components.StatusPill
import com.bruleetelie.app.ui.components.TactilePrimaryButton
import com.bruleetelie.app.ui.theme.*

data class OcrItem(
    val id: String,
    val rawName: String,
    val matchedIngredient: String,
    val quantity: String,
    val priceFormatted: String,
    val confidence: String,
    var isConfirmed: Boolean = false
)

@Composable
fun OcrReviewScreen(
    onFinishReview: () -> Unit
) {
    val items = remember {
        mutableStateListOf(
            OcrItem("1", "CHOC NOBRE LEITE 1KG", "Chocolate Nobre", "1 kg", "R$ 34,90", "Alta (98%)", isConfirmed = true),
            OcrItem("2", "LEITE CONDENS MOCA TP 395G", "Leite Condensado", "395 g", "R$ 7,49", "Alta (96%)"),
            OcrItem("3", "MANTEIGA EXTRA S SAL 200G", "Manteiga Extra", "200 g", "R$ 11,89", "Média (85%)"),
            OcrItem("4", "CACAU PO 100% 500G", "Cacau em Pó 100%", "500 g", "R$ 29,90", "Alta (94%)")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text("SCANNER • NOTA FISCAL", style = MaterialTheme.typography.labelSmall, color = BruleeSecondary, fontWeight = FontWeight.Bold)
            Text("Revisão de Itens", style = MaterialTheme.typography.headlineMedium)
        }

        // Mascot Guidance Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BruleeSurfaceContainerLow,
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(BruleeSecondaryFixed),
                    contentAlignment = Alignment.Center
                ) { Text("🔍") }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Revisão Obrigatória", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Nenhum preço é atualizado no estoque sem sua aprovação.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Scanned Items
        items.forEachIndexed { index, item ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BruleeSurfaceContainerLowest,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.rawName, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        StatusPill(
                            text = item.confidence,
                            background = BruleePrimaryFixed.copy(alpha = 0.6f),
                            foreground = BruleePrimary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(item.matchedIngredient, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Quantidade: ${item.quantity}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(item.priceFormatted, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = BruleePrimary)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { items[index] = item.copy(isConfirmed = true) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (item.isConfirmed) BruleePrimary else BruleeSurfaceContainerHigh,
                                contentColor = if (item.isConfirmed) Color.White else BruleeOnSurface
                            ),
                            shape = CircleShape
                        ) {
                            Text(if (item.isConfirmed) "✓ Confirmado" else "Confirmar")
                        }

                        FilledTonalButton(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            shape = CircleShape
                        ) { Text("Editar") }
                    }
                }
            }
        }

        TactilePrimaryButton(
            text = "SALVAR ITENS CONFIRMADOS",
            onClick = onFinishReview
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OcrReviewScreenPreview() {
    BruleeTheme {
        OcrReviewScreen(onFinishReview = {})
    }
}
