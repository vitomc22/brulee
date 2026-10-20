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
import com.bruleetelie.domain.*

@Composable
fun QuotesScreen(
    quotes: List<Quote>, onNew: () -> Unit, onApprove: (Quote) -> Unit,
    onWhatsApp: (Quote) -> Unit, onPdf: (Quote) -> Unit, onReject: (Quote) -> Unit
) {
    var onlyPending by rememberSaveable { mutableStateOf(true) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp).testTag("quotes_screen"), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Seus pedidos", style = MaterialTheme.typography.headlineMedium)
        Text("Acompanhe os orçamentos e aprove os pedidos confirmados pelo cliente.")
        TactilePrimaryButton("NOVO ORÇAMENTO", onNew, Modifier.testTag("quotes_new"))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(onlyPending, { onlyPending = true }, label = { Text("Aguardando aprovação") }, modifier = Modifier.testTag("quotes_pending"))
            FilterChip(!onlyPending, { onlyPending = false }, label = { Text("Todos") }, modifier = Modifier.testTag("quotes_all"))
        }
        val visible = quotes.filter { !onlyPending || it.status in listOf(QuoteStatus.Draft, QuoteStatus.Sent) }
        if (visible.isEmpty()) Text("Nenhum orçamento neste filtro.", Modifier.testTag("quotes_empty"))
        visible.forEach { quote -> key(quote.id) {
            ElevatedCard(Modifier.fillMaxWidth().testTag("quote_card_${quote.id}")) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatusPill("${quote.code} • ${quote.status.label}")
                    Text(quote.customer.name, style = MaterialTheme.typography.titleLarge)
                    Text("${quote.unitsCount}x ${quote.productName}")
                    Text("Entrega: ${quote.deliveryDate}")
                    Text(quote.pricing.salePrice.format(), style = MaterialTheme.typography.headlineSmall)
                    TactileSecondaryButton("ABRIR NO WHATSAPP", { onWhatsApp(quote) }, Modifier.testTag("quote_whatsapp_${quote.id}"), enabled = quote.customer.phone.filter(Char::isDigit).length in 10..15)
                    TactileSecondaryButton("COMPARTILHAR PDF", { onPdf(quote) }, Modifier.testTag("quote_pdf_${quote.id}"))
                    if (quote.status in listOf(QuoteStatus.Draft, QuoteStatus.Sent)) {
                        TactilePrimaryButton("APROVAR E PRODUZIR", { onApprove(quote) }, Modifier.testTag("quote_approve_${quote.id}"))
                        TextButton({ onReject(quote) }, Modifier.testTag("quote_reject_${quote.id}")) { Text("Marcar como recusado") }
                    }
                }
            }
        } }
    }
}
