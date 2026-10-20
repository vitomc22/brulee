package com.bruleetelie.documents

import com.bruleetelie.domain.Money
import com.bruleetelie.domain.Quote
import java.io.File

interface DocumentGateway {
    fun generateCustomerQuoteText(quote: Quote): String
    fun formatWhatsAppUrl(quote: Quote): String
}

class DefaultDocumentGateway : DocumentGateway {

    override fun generateCustomerQuoteText(quote: Quote): String {
        return buildString {
            appendLine("🍰 *Brûlée Telie — Confeitaria Artesanal*")
            appendLine("Olá, *${quote.customer.name}*! Segue seu orçamento:")
            appendLine()
            appendLine("📋 *Orçamento:* ${quote.code}")
            appendLine("🧁 *Item:* ${quote.productName}")
            if (quote.productSubtitle.isNotBlank()) {
                appendLine("   _${quote.productSubtitle}_")
            }
            appendLine("📦 *Quantidade:* ${quote.unitsCount} unidades")
            appendLine("📅 *Previsão de Entrega:* ${quote.deliveryDate}")
            appendLine()
            appendLine("💰 *Valor Total:* ${quote.pricing.salePrice.format()}")
            appendLine("⏳ *Validade:* ${quote.validDays} dias")
            if (quote.notes.isNotBlank()) {
                appendLine("📝 *Observações:* ${quote.notes}")
            }
            appendLine()
            appendLine("Pagamento via Pix ou Cartão de Crédito. Para confirmar, basta responder esta mensagem! ✨")
        }
    }

    override fun formatWhatsAppUrl(quote: Quote): String {
        val digits = quote.customer.phone.filter { it.isDigit() }
        require(digits.length in 10..15) { "Informe um telefone com DDD" }
        val phoneDigits = if (digits.length in 10..11) "55$digits" else digits
        val encodedMessage = java.net.URLEncoder.encode(generateCustomerQuoteText(quote), "UTF-8")
        return "https://api.whatsapp.com/send?phone=$phoneDigits&text=$encodedMessage"
    }
}
