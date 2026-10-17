package com.bruleetelie.documents

import com.bruleetelie.domain.*
import java.math.BigDecimal
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentGatewayTest {

    private val gateway = DefaultDocumentGateway()

    private val sampleQuote = Quote(
        id = "quote-284",
        code = "#ORC-284",
        customer = Customer("c1", "Ana Souza", "(11) 98765-4321"),
        productName = "40 Brigadeiros Gourmet",
        productSubtitle = "Pistache tostado, Doce de Leite e Crème Brûlée",
        unitsCount = 40,
        pricing = PricingResult(
            costs = RecipeCosts(
                ingredients = Money.fromReais("38.40"),
                labor = Money.fromReais("50.00"),
                energy = Money.fromReais("5.70"),
                packaging = Money.fromReais("4.50")
            ),
            salePrice = Money.fromReais("149.90"),
            profit = Money.fromReais("51.30"),
            marginPercent = BigDecimal("34.22")
        ),
        deliveryDate = "Sábado, 14 de Outubro - 16:00",
        validDays = 3,
        notes = "Caixa Telie Ouro com laço acetinado e entrega inclusa."
    )

    @Test
    fun `test customer quote text contains customer information and total price`() {
        val text = gateway.generateCustomerQuoteText(sampleQuote)

        assertTrue(text.contains("Brûlée Telie"))
        assertTrue(text.contains("Ana Souza"))
        assertTrue(text.contains("#ORC-284"))
        assertTrue(text.contains("40 Brigadeiros Gourmet"))
        assertTrue(text.contains("40 unidades"))
        assertTrue(text.contains("R$\u00A0149,90") || text.contains("149,90"))
        assertTrue(text.contains("Sábado, 14 de Outubro - 16:00"))
        assertTrue(text.contains("3 dias"))
    }

    @Test
    fun `test customer quote text NEVER leaks internal costs margins or profits`() {
        val text = gateway.generateCustomerQuoteText(sampleQuote)

        // Sensitive business data that must NEVER appear on customer quote
        assertFalse("Não deve mostrar lucro", text.contains("51,30"))
        assertFalse("Não deve mostrar margem", text.contains("34,22"))
        assertFalse("Não deve mostrar custo de ingredientes", text.contains("38,40"))
        assertFalse("Não deve mostrar custo de mão de obra", text.contains("50,00"))
        assertFalse("Não deve mostrar custo de gás/energia", text.contains("5,70"))
        assertFalse("Não deve conter palavras-chave internas", text.contains("custo", ignoreCase = true))
        assertFalse("Não deve conter palavra margem", text.contains("margem", ignoreCase = true))
        assertFalse("Não deve conter palavra lucro", text.contains("lucro", ignoreCase = true))
    }

    @Test
    fun `test whatsapp url formatting`() {
        val url = gateway.formatWhatsAppUrl(sampleQuote)

        assertTrue(url.startsWith("https://api.whatsapp.com/send?phone=11987654321&text="))
        assertTrue(url.contains("ORC-284"))
    }
}
