package com.bruleetelie.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.bruleetelie.domain.ShoppingItem

@Composable
fun ShoppingListScreen(
    items: List<ShoppingItem>,
    onAdd: (String, String) -> Unit,
    onPurchasedChanged: (String, Boolean) -> Unit,
    onDelete: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    val pending = items.count { !it.isPurchased }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).testTag("shopping_screen"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Lista de compras", style = MaterialTheme.typography.headlineMedium)
        Text(
            if (pending == 0) "Tudo comprado por aqui ✨" else "$pending item(ns) pendente(s)",
            modifier = Modifier.testTag("shopping_pending_count"),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Ingrediente") },
                singleLine = true,
                modifier = Modifier.weight(1.5f).testTag("shopping_name")
            )
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantidade") },
                singleLine = true,
                modifier = Modifier.weight(1f).testTag("shopping_quantity")
            )
        }
        Button(
            enabled = name.isNotBlank() && quantity.isNotBlank(),
            onClick = {
                onAdd(name, quantity)
                name = ""
                quantity = ""
            },
            modifier = Modifier.fillMaxWidth().testTag("shopping_add")
        ) { Text("ADICIONAR À LISTA") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
                Surface(tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(8.dp).testTag("shopping_item_${item.id}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = item.isPurchased,
                            onCheckedChange = { onPurchasedChanged(item.id, it) },
                            modifier = Modifier.testTag("shopping_check_$index")
                        )
                        Column(Modifier.weight(1f)) {
                            Text(
                                item.name,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = if (item.isPurchased) TextDecoration.LineThrough else null
                            )
                            Text(item.quantity, style = MaterialTheme.typography.bodySmall)
                        }
                        OutlinedButton(
                            onClick = { onDelete(item.id) },
                            modifier = Modifier.testTag("shopping_delete_$index")
                        ) { Text("Remover") }
                    }
                }
            }
        }
    }
}
