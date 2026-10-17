package com.bruleetelie.domain

import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Test

class PricingEngineTest {
    @Test
    fun `calcula preco sem perder centavos`() {
        val costs = RecipeCosts(
            ingredients = Money(3_840),
            labor = Money(5_000),
            energy = Money(570),
            packaging = Money(450),
        )

        val result = PricingEngine.fromTargetMargin(costs, BigDecimal("34.22"))

        assertEquals(9_860, result.costs.total.cents)
        assertEquals(14_990, result.salePrice.cents)
        assertEquals(5_130, result.profit.cents)
    }

    @Test
    fun `test canonical scenario cost total 70_90`() {
        val ingredientsCost = Money.fromReais("24.60")
        val packagingCost = Money.fromReais("5.00")
        val laborCost = Money.fromReais("37.50")
        val gasCost = Money.fromReais("2.00")
        val energyCost = Money.fromReais("1.80")

        val recipeCosts = RecipeCosts(
            ingredients = ingredientsCost,
            packaging = packagingCost,
            labor = laborCost,
            energy = gasCost + energyCost
        )

        assertEquals(7090, recipeCosts.total.cents)
        assertEquals("R$ 70,90", recipeCosts.total.format())
    }
}

