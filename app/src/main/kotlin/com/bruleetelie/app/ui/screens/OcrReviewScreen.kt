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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.StatusPill
import com.bruleetelie.app.ui.components.TactilePrimaryButton
import com.bruleetelie.app.ui.components.TactileSecondaryButton
import com.bruleetelie.app.ui.theme.*
import java.math.BigDecimal
import java.math.RoundingMode

data class OcrItem(
    val id: String,
    val rawName: String,
    val matchedIngredient: String,
    val packageQuantityValue: String,
    val packageQuantityUnit: String,
    val packagePriceCents: Long,
    val normalizedUnitPriceCents: Long,
    val priceFormatted: String,
    val confidence: String,
    var isConfirmed: Boolean = false
) {
    val quantity: String get() = "$packageQuantityValue $packageQuantityUnit"
}

@Composable
fun OcrReviewScreen(
    initialItems: List<OcrItem>,
    onSaveConfirmedItems: (List<OcrItem>) -> Unit
) {
    val items = remember(initialItems) {
        mutableStateListOf<OcrItem>().apply { addAll(initialItems) }
    }
    val allItemsConfirmed = items.all(OcrItem::isConfirmed)
    var editingItemId by remember { mutableStateOf<String?>(null) }
    var editedQuantity by remember { mutableStateOf("") }
    var editedPrice by remember { mutableStateOf("") }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ocr_item_${item.id}"),
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
                            modifier = Modifier
                                .weight(1f)
                                .testTag("ocr_confirm_${item.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (item.isConfirmed) BruleePrimary else BruleeSurfaceContainerHigh,
                                contentColor = if (item.isConfirmed) Color.White else BruleeOnSurface
                            ),
                            shape = CircleShape
                        ) {
                            Text(if (item.isConfirmed) "✓ Confirmado" else "Confirmar")
                        }

                        FilledTonalButton(
                            onClick = {
                                editingItemId = item.id
                                editedQuantity = item.packageQuantityValue
                                editedPrice = centsToInput(item.packagePriceCents)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("ocr_edit_${item.id}"),
                            shape = CircleShape
                        ) { Text("Editar") }
                    }

                    TextButton(
                        onClick = {
                            if (editingItemId == item.id) editingItemId = null
                            items.removeAt(index)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ocr_ignore_${item.id}")
                    ) {
                        Text("IGNORAR ITEM")
                    }

                    if (editingItemId == item.id) {
                        OutlinedTextField(
                            value = editedQuantity,
                            onValueChange = { editedQuantity = it },
                            label = { Text("Quantidade (${item.packageQuantityUnit})") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ocr_edit_quantity_${item.id}")
                        )
                        OutlinedTextField(
                            value = editedPrice,
                            onValueChange = { editedPrice = it },
                            label = { Text("Preço do pacote") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ocr_edit_price_${item.id}")
                        )

                        val parsedQuantity = editedQuantity.replace(',', '.').toBigDecimalOrNull()
                        val parsedPriceCents = parsePriceCents(editedPrice)
                        val canSaveEdit = parsedQuantity != null && parsedQuantity.signum() == 1 &&
                            parsedPriceCents != null && parsedPriceCents > 0

                        TactileSecondaryButton(
                            text = "SALVAR CORREÇÃO",
                            enabled = canSaveEdit,
                            modifier = Modifier.testTag("ocr_save_edit_${item.id}"),
                            onClick = {
                                val quantity = requireNotNull(parsedQuantity)
                                val priceCents = requireNotNull(parsedPriceCents)
                                items[index] = item.copy(
                                    packageQuantityValue = quantity.stripTrailingZeros().toPlainString(),
                                    packagePriceCents = priceCents,
                                    normalizedUnitPriceCents = normalizedUnitPrice(
                                        packagePriceCents = priceCents,
                                        packageQuantity = quantity,
                                        packageUnit = item.packageQuantityUnit
                                    ),
                                    priceFormatted = formatCents(priceCents),
                                    isConfirmed = false
                                )
                                editingItemId = null
                            }
                        )
                    }
                }
            }
        }

        TactilePrimaryButton(
            text = "SALVAR ITENS CONFIRMADOS",
            onClick = { onSaveConfirmedItems(items.filter(OcrItem::isConfirmed)) },
            enabled = allItemsConfirmed,
            modifier = Modifier.testTag("ocr_save_confirmed")
        )
    }
}

private fun parsePriceCents(value: String): Long? = runCatching {
    value.trim().replace(".", "").replace(',', '.')
        .toBigDecimal()
        .movePointRight(2)
        .setScale(0, RoundingMode.HALF_UP)
        .longValueExact()
}.getOrNull()

private fun normalizedUnitPrice(
    packagePriceCents: Long,
    packageQuantity: BigDecimal,
    packageUnit: String
): Long {
    val normalizationFactor = if (packageUnit == "g" || packageUnit == "ml") {
        BigDecimal("1000")
    } else {
        BigDecimal.ONE
    }
    return BigDecimal(packagePriceCents)
        .multiply(normalizationFactor)
        .divide(packageQuantity, 0, RoundingMode.HALF_UP)
        .longValueExact()
}

private fun centsToInput(cents: Long): String =
    BigDecimal(cents).movePointLeft(2).setScale(2).toPlainString().replace('.', ',')

private fun formatCents(cents: Long): String = "R$ ${centsToInput(cents)}"

@Preview(showBackground = true)
@Composable
private fun OcrReviewScreenPreview() {
    BruleeTheme {
        OcrReviewScreen(
            initialItems = listOf(
                OcrItem("1", "CHOC NOBRE LEITE 1KG", "Chocolate em Pó 50%", "1", "kg", 3490, 3490, "R$ 34,90", "Alta")
            ),
            onSaveConfirmedItems = {}
        )
    }
}
