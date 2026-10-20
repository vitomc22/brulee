package com.bruleetelie.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.*
import com.bruleetelie.data.db.IngredientEntity
import com.bruleetelie.data.db.PriceObservationEntity
import com.bruleetelie.domain.*

@Composable
fun InventoryScreen(
    ingredients: List<IngredientEntity>, observations: List<PriceObservationEntity>,
    onImport: () -> Unit, onSave: (String, String, String, Money) -> Unit,
    onBuy: (String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var unit by rememberSaveable { mutableStateOf("g") }
    var price by rememberSaveable { mutableStateOf("") }
    val parsedPrice = runCatching { Money.fromReais(price) }.getOrNull()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp).testTag("inventory_screen"), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Ingredientes e preços", style = MaterialTheme.typography.headlineMedium)
        Text("Registre suas compras ou importe uma nota para acompanhar os preços reais.")
        TactilePrimaryButton("IMPORTAR NOTA / OCR", onImport, Modifier.testTag("stock_import"))
        OutlinedTextField(name, { name = it }, label = { Text("Ingrediente") }, modifier = Modifier.fillMaxWidth().testTag("stock_name"))
        OutlinedTextField(quantity, { quantity = it }, label = { Text("Quantidade da embalagem") }, modifier = Modifier.fillMaxWidth().testTag("stock_quantity"))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("g", "kg", "ml", "l", "un").forEach { value -> FilterChip(unit == value, { unit = value }, label = { Text(value) }, modifier = Modifier.testTag("stock_unit_$value")) }
        }
        OutlinedTextField(price, { price = it }, label = { Text("Preço pago (R$)") }, modifier = Modifier.fillMaxWidth().testTag("stock_price"))
        TactileSecondaryButton("SALVAR PREÇO", {
            onSave(name.trim(), quantity.replace(',', '.'), unit, parsedPrice!!)
            name = ""; quantity = ""; price = ""
        }, Modifier.testTag("stock_save"), enabled = name.isNotBlank() && quantity.replace(',', '.').toBigDecimalOrNull()?.signum() == 1 && parsedPrice != null && parsedPrice > Money.Zero)
        if (ingredients.isEmpty()) Text("Nenhum ingrediente cadastrado.", Modifier.testTag("inventory_empty"))
        ingredients.forEach { ingredient ->
            val latest = observations.filter { it.ingredientId == ingredient.id }.maxByOrNull { it.purchaseDateMillis }
            ElevatedCard(Modifier.fillMaxWidth().testTag("stock_ingredient_${ingredient.id}")) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(ingredient.name, style = MaterialTheme.typography.titleMedium)
                    Text(latest?.let { "${it.packageQuantityValue} ${it.packageQuantityUnit} • ${Money(it.packagePriceCents).format()}" } ?: "Sem preço cadastrado")
                    TextButton({ onBuy(ingredient.name) }, Modifier.testTag("stock_buy_${ingredient.id}")) { Text("Adicionar à lista de compras") }
                }
            }
        }
    }
}
