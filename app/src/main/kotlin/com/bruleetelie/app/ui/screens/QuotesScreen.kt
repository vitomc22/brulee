package com.bruleetelie.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.*
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.domain.*

@Composable
fun QuotesScreen(
    quotes: List<Quote>, onNew: () -> Unit, onApprove: (Quote) -> Unit,
    onWhatsApp: (Quote) -> Unit, onPdf: (Quote) -> Unit, onReject: (Quote) -> Unit,
    onContinueDraft: (Quote) -> Unit = {}, orders: List<ProductionOrder> = emptyList(),
    onProduction: () -> Unit = {}
) {
    var onlyPending by rememberSaveable { mutableStateOf(true) }
    val visible = remember(quotes, onlyPending) { quotes.filter { !onlyPending || it.status in listOf(QuoteStatus.Draft, QuoteStatus.Sent) } }
    val ordersByQuote = remember(orders) { orders.associateBy { it.quoteId } }
    LazyColumn(Modifier.fillMaxSize().testTag("quotes_screen"), contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item(key = "header") {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Seus pedidos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("${quotes.size} orçamentos • ${quotes.count { it.status == QuoteStatus.Draft }} rascunhos",
                        style = MaterialTheme.typography.bodySmall, color = BruleeOnSurfaceVariant)
                }
                FilledTonalIconButton(onNew, Modifier.testTag("quotes_new").semantics { contentDescription = "Novo orçamento" }) {
                    BruleeIcon(BruleeSymbol.Plus)
                }
            }
        }
        item(key = "filters") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(onlyPending, { onlyPending = true }, label = { Text("Em aberto") }, modifier = Modifier.testTag("quotes_pending"))
                FilterChip(!onlyPending, { onlyPending = false }, label = { Text("Todos") }, modifier = Modifier.testTag("quotes_all"))
            }
        }
        if (visible.isEmpty()) item(key = "empty") {
            Surface(color = BruleePrimaryFixed.copy(alpha = .4f), shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.fillMaxWidth().padding(24.dp).testTag("quotes_empty"),
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    BruleeMascot(size = 64.dp)
                    Text(if (quotes.isEmpty()) "Seu próximo pedido começa aqui" else "Nenhum orçamento neste filtro.", style = MaterialTheme.typography.titleMedium)
                    Text("Crie um orçamento ou continue um rascunho para combinar os detalhes com o cliente.", style = MaterialTheme.typography.bodySmall)
                    TextButton(onNew, Modifier.testTag("quotes_empty_new")) { Text("Criar orçamento") }
                }
            }
        }
        items(visible, key = { it.id }) { quote ->
            QuoteCard(quote, ordersByQuote[quote.id], onContinueDraft, onApprove, onWhatsApp, onPdf, onReject, onProduction)
        }
    }
}

@Composable
private fun QuoteCard(quote: Quote, order: ProductionOrder?, onContinue: (Quote) -> Unit,
    onApprove: (Quote) -> Unit, onWhatsApp: (Quote) -> Unit, onPdf: (Quote) -> Unit,
    onReject: (Quote) -> Unit, onProduction: () -> Unit) {
    var menu by remember { mutableStateOf(false) }
    val status = when (order?.status) {
        ProductionStatus.Pending -> "Aprovado"
        ProductionStatus.InProgress -> "Em produção"
        ProductionStatus.Completed -> "Concluído"
        ProductionStatus.Cancelled -> "Cancelado"
        null -> when (quote.status) {
        QuoteStatus.Sent -> "Aguardando"
        QuoteStatus.ConvertedToSale -> "Concluído"
        else -> quote.status.label
        }
    }
    val foreground = when {
        order?.status == ProductionStatus.Cancelled || quote.status in listOf(QuoteStatus.Rejected, QuoteStatus.Expired) -> BruleeOnSurfaceVariant
        quote.status == QuoteStatus.Draft -> BruleeTertiary
        quote.status == QuoteStatus.Sent -> BruleeOnSecondaryContainer
        else -> BruleePrimary
    }
    Surface(Modifier.fillMaxWidth().testTag("quote_card_${quote.id}"), shape = RoundedCornerShape(16.dp),
        color = BruleeSurfaceContainerLowest, border = BorderStroke(1.dp, BruleeOutlineVariant.copy(alpha = .5f))) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(quote.code, Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = BruleeOnSurfaceVariant)
                StatusPill(status, foreground.copy(alpha = .1f), foreground)
            }
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(quote.customer.name.ifBlank { "Cliente a definir" }, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${quote.unitsCount} × ${quote.productName}", style = MaterialTheme.typography.bodySmall,
                        maxLines = 2, overflow = TextOverflow.Ellipsis, color = BruleeOnSurfaceVariant)
                }
                Text(quote.pricing.salePrice.format(), style = MaterialTheme.typography.titleMedium,
                    color = BruleePrimary, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                BruleeIcon(BruleeSymbol.Calendar, Modifier.size(16.dp), tint = BruleeOnSurfaceVariant)
                Spacer(Modifier.width(6.dp))
                Text(quote.deliveryDate.ifBlank { "Entrega a combinar" }, style = MaterialTheme.typography.bodySmall, color = BruleeOnSurfaceVariant)
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                when (quote.status) {
                    QuoteStatus.Draft -> TextButton({ onContinue(quote) }, Modifier.weight(1f).testTag("quote_continue_${quote.id}")) { Text("Continuar orçamento") }
                    QuoteStatus.Sent -> TextButton({ onApprove(quote) }, Modifier.weight(1f).testTag("quote_approve_${quote.id}"),
                        enabled = quote.customer.name.isNotBlank() && isValidDeliveryDate(quote.deliveryDate)) { Text("Aprovar e produzir") }
                    QuoteStatus.Approved -> TextButton(onProduction, Modifier.weight(1f).testTag("quote_production_${quote.id}")) { Text("Ver produção") }
                    else -> TextButton({ onPdf(quote) }, Modifier.weight(1f).testTag("quote_pdf_primary_${quote.id}")) { Text("Ver PDF") }
                }
                Box {
                    TextButton({ menu = true }, Modifier.testTag("quote_actions_${quote.id}").semantics { contentDescription = "Mais ações do orçamento ${quote.code}" }) { Text("Mais") }
                    DropdownMenu(menu, { menu = false }) {
                        DropdownMenuItem(text = { Text("Abrir no WhatsApp") }, onClick = { menu = false; onWhatsApp(quote) },
                            enabled = quote.customer.name.isNotBlank() && isValidDeliveryDate(quote.deliveryDate) && quote.customer.phone.filter(Char::isDigit).length in 10..15,
                            modifier = Modifier.testTag("quote_whatsapp_${quote.id}"))
                        DropdownMenuItem(text = { Text("Compartilhar PDF") }, onClick = { menu = false; onPdf(quote) },
                            enabled = quote.customer.name.isNotBlank() && isValidDeliveryDate(quote.deliveryDate), modifier = Modifier.testTag("quote_pdf_${quote.id}"))
                        if (quote.status in listOf(QuoteStatus.Draft, QuoteStatus.Sent)) DropdownMenuItem(
                            text = { Text("Marcar como recusado") }, onClick = { menu = false; onReject(quote) }, modifier = Modifier.testTag("quote_reject_${quote.id}"))
                    }
                }
            }
        }
    }
}
