package com.bruleetelie.app

import androidx.compose.ui.graphics.Color
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.domain.Money
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class BruleeAppLogicTest {

    @Test
    fun `test stitch design tokens are defined correctly`() {
        assertNotNull(BruleePrimary)
        assertNotNull(BruleeSecondary)
        assertNotNull(BruleeSurface)
        assertNotNull(BruleeBackground)

        assertEquals(Color(0xFF3B523E), BruleePrimary)
        assertEquals(Color(0xFFB88C42), BruleeSecondary)
        assertEquals(Color(0xFFFDF9F1), BruleeSurface)
        assertEquals(Color(0xFFFAF9F6), BruleeBackground)
    }

    @Test
    fun `test money formatting for UI display`() {
        val money = Money.fromReais("149.90")
        val formatted = money.format()
        assertEquals("R$\u00A0149,90", formatted)
    }

    @Test
    fun `test quote approval logic updates state and adds order`() {
        val repo = com.bruleetelie.data.BruleeRepository()
        val initialOrdersCount = repo.productionOrders.value.size

        val quote = com.bruleetelie.domain.Quote(
            id = "q-preview-1",
            code = "#ORC-300",
            customer = com.bruleetelie.domain.Customer("c-1", "Mariana", "(11) 91234-5678"),
            productName = "Bolo Red Velvet",
            productSubtitle = "Com recheio cream cheese",
            unitsCount = 1,
            pricing = com.bruleetelie.domain.PricingResult(
                costs = com.bruleetelie.domain.RecipeCosts(Money.fromReais("50.00"), Money.fromReais("40.00"), Money.fromReais("5.00"), Money.fromReais("5.00")),
                salePrice = Money.fromReais("150.00"),
                profit = Money.fromReais("50.00"),
                marginPercent = java.math.BigDecimal("33.33")
            ),
            deliveryDate = "Amanhã"
        )

        repo.addQuote(quote)
        repo.approveQuote(quote.id)

        val updatedOrders = repo.productionOrders.value
        assertEquals(initialOrdersCount + 1, updatedOrders.size)
        assertEquals("#ORC-300", updatedOrders.first().quoteCode)
    }
}
