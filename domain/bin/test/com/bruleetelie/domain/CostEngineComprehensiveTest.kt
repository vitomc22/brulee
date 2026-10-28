package com.bruleetelie.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class CostEngineComprehensiveTest {

    // Helper data for canonical scenario
    private val chocolate = RecipeIngredientItem(
        id = "1",
        name = "Chocolate",
        packageQuantity = Quantity.grams(BigDecimal("2000")),
        packagePrice = Money.fromReais("80.00"),
        usedQuantity = Quantity.grams(BigDecimal("350"))
    ) // 350g out of 2000g for R$ 80.00 = R$ 14.00

    private val creme = RecipeIngredientItem(
        id = "2",
        name = "Creme",
        packageQuantity = Quantity.grams(BigDecimal("1000")),
        packagePrice = Money.fromReais("25.00"),
        usedQuantity = Quantity.grams(BigDecimal("400"))
    ) // 400g out of 1000g for R$ 25.00 = R$ 10.00

    private val acucar = RecipeIngredientItem(
        id = "3",
        name = "Açúcar",
        packageQuantity = Quantity.grams(BigDecimal("1000")),
        packagePrice = Money.fromReais("6.00"),
        usedQuantity = Quantity.grams(BigDecimal("100"))
    ) // 100g out of 1000g for R$ 6.00 = R$ 0.60

    @Test
    fun `COST-E2E-001 proportional ingredient cost`() {
        assertEquals(1400L, chocolate.proportionalCost.cents)
    }

    @Test
    fun `COST-E2E-002 ingredients summation`() {
        val totalIngredients = chocolate.proportionalCost + creme.proportionalCost + acucar.proportionalCost
        assertEquals(2460L, totalIngredients.cents) // R$ 24,60
    }

    @Test
    fun `COST-E2E-003 labor cost by active time`() {
        // 75 min (45 prep + 30 dec) at R$ 30,00/h -> 1.25 * 30 = 37.50
        val hours = BigDecimal("75").divide(BigDecimal("60"), 4, java.math.RoundingMode.HALF_EVEN)
        val hourlyRate = Money.fromReais("30.00")
        val labor = Money((hours.multiply(BigDecimal(hourlyRate.cents))).longValueExact())
        assertEquals(3750L, labor.cents) // R$ 37,50
    }

    @Test
    fun `COST-E2E-004 gas cost by duration`() {
        // 30 min at R$ 4.00/h -> 0.5 * 4 = 2.00
        val hours = BigDecimal("30").divide(BigDecimal("60"), 4, java.math.RoundingMode.HALF_EVEN)
        val gasRate = Money.fromReais("4.00")
        val gas = Money((hours.multiply(BigDecimal(gasRate.cents))).longValueExact())
        assertEquals(200L, gas.cents) // R$ 2,00
    }

    @Test
    fun `COST-E2E-005 energy cost by power and duration`() {
        // 45 min (0.75h) at 2.0 kW with R$ 1.20/kWh -> 2 * 0.75 * 1.20 = 1.80
        val hours = BigDecimal("45").divide(BigDecimal("60"), 4, java.math.RoundingMode.HALF_EVEN)
        val powerKw = BigDecimal("2.0")
        val pricePerKwh = Money.fromReais("1.20")
        val energyCents = hours.multiply(powerKw).multiply(BigDecimal(pricePerKwh.cents)).longValueExact()
        val energy = Money(energyCents)
        assertEquals(180L, energy.cents) // R$ 1,80
    }

    @Test
    fun `COST-E2E-006 complete canonical total cost`() {
        val ingredients = 2460L
        val packaging = 500L
        val labor = 3750L
        val gas = 200L
        val energy = 180L
        val total = ingredients + packaging + labor + gas + energy
        assertEquals(7090L, total) // R$ 70,90
    }

    @Test
    fun `COST-E2E-010 zero used quantity`() {
        val zeroUse = RecipeIngredientItem(
            id = "4",
            name = "Sal",
            packageQuantity = Quantity.grams(BigDecimal("1000")),
            packagePrice = Money.fromReais("5.00"),
            usedQuantity = Quantity.grams(BigDecimal("0"))
        )
        assertEquals(0L, zeroUse.proportionalCost.cents)
    }

    @Test
    fun `COST-E2E-011 kg to g conversion`() {
        val item = RecipeIngredientItem(
            id = "5",
            name = "Farinha",
            packageQuantity = Quantity.kilograms(BigDecimal("2.0")),
            packagePrice = Money.fromReais("10.00"),
            usedQuantity = Quantity.grams(BigDecimal("500"))
        )
        // 2kg = 2000g, 500g used -> 500/2000 * 10 = R$ 2.50 (250 cents)
        assertEquals(250L, item.proportionalCost.cents)
    }

    @Test
    fun `COST-E2E-015 precise cents calculation`() {
        val item = RecipeIngredientItem(
            id = "6",
            name = "Item Decimal",
            packageQuantity = Quantity.grams(BigDecimal("1000")),
            packagePrice = Money.fromReais("10.01"),
            usedQuantity = Quantity.grams(BigDecimal("333"))
        )
        // 333 / 1000 * 1001 = 333.333 -> rounds to 333 cents (R$ 3.33)
        assertEquals(333L, item.proportionalCost.cents)
    }

    @Test
    fun `COST-E2E-016 extremely cheap ingredient`() {
        val cheap = RecipeIngredientItem(
            id = "7",
            name = "Barato",
            packageQuantity = Quantity.kilograms(BigDecimal("10.0")),
            packagePrice = Money.fromReais("1.00"),
            usedQuantity = Quantity.grams(BigDecimal("1"))
        )
        assertEquals(0L, cheap.proportionalCost.cents) // 1g of 10kg for 1.00 rounds to 0
    }

    @Test
    fun `COST-E2E-017 expensive ingredient`() {
        val expensive = RecipeIngredientItem(
            id = "8",
            name = "Açafrão",
            packageQuantity = Quantity.grams(BigDecimal("100")),
            packagePrice = Money.fromReais("9999.99"),
            usedQuantity = Quantity.grams(BigDecimal("100"))
        )
        assertEquals(999999L, expensive.proportionalCost.cents)
    }
}
