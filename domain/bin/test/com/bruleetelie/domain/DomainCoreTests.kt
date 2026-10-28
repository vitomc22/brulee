package com.bruleetelie.domain

import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class DomainCoreTests {

    @Test
    fun `test money operations and precision`() {
        val m1 = Money.fromReais("38.40")
        val m2 = Money.fromReais("50.00")
        val sum = m1 + m2
        assertEquals(8840L, sum.cents)
        assertEquals(Money.fromReais("88.40"), sum)
        assertEquals("R$\u00A088,40", sum.format())

        val diff = sum - m1
        assertEquals(m2, diff)

        val multiplied = m1.multiply(BigDecimal("1.5"))
        assertEquals(5760L, multiplied.cents) // 38.40 * 1.5 = 57.60
    }

    @Test
    fun `test quantity conversions within same dimension`() {
        val twoKg = Quantity.kilograms(2)
        val inGrams = twoKg.convertTo(UnitOfMeasure.Gram)
        assertEquals(0, BigDecimal("2000").compareTo(inGrams.value))

        val halfLiter = Quantity.liters(0.5)
        val inMl = halfLiter.convertTo(UnitOfMeasure.Milliliter)
        assertEquals(0, BigDecimal("500").compareTo(inMl.value))

        val ninetyMin = Quantity.minutes(90)
        val inHours = ninetyMin.convertTo(UnitOfMeasure.Hour)
        assertEquals(0, BigDecimal("1.5").compareTo(inHours.value))
    }

    @Test
    fun `test incompatible quantity conversions throw exception`() {
        val weight = Quantity.kilograms(1)
        assertThrows(IllegalArgumentException::class.java) {
            weight.convertTo(UnitOfMeasure.Liter)
        }
    }

    @Test
    fun `test proportional cost calculation for ingredients`() {
        // Bought 2 kg for R$ 64,90
        val packageQty = Quantity.kilograms(2)
        val packagePrice = Money.fromReais("64.90")

        // Used 350 g
        val usedQty = Quantity.grams(350)

        // Cost = 64.90 * (350 / 2000) = 64.90 * 0.175 = 11.3575 -> R$ 11,36
        val proportionalCost = Quantity.calculateProportionalCost(packageQty, packagePrice, usedQty)
        assertEquals(1136L, proportionalCost.cents)
    }

    @Test
    fun `test recipe cost calculator with operational costs and complexity`() {
        val chocolate = RecipeIngredientItem(
            id = "1",
            name = "Chocolate Nobre",
            packageQuantity = Quantity.kilograms(1),
            packagePrice = Money.fromReais("60.00"),
            usedQuantity = Quantity.grams(350) // R$ 21,00
        )
        val milk = RecipeIngredientItem(
            id = "2",
            name = "Leite Condensado",
            packageQuantity = Quantity.grams(395),
            packagePrice = Money.fromReais("6.50"),
            usedQuantity = Quantity.grams(395) // R$ 6,50
        )

        val times = OperationalTimes(
            prepTime = Quantity.minutes(60), // 1h
            cookTime = Quantity.minutes(30), // 0.5h
            decorationTime = Quantity.minutes(30), // 0.5h
            energySource = EnergySource.Gas,
            complexity = Complexity.Normal // multiplier 1.15
        )

        val settings = OperatingSettings(
            hourlyLaborRate = Money.fromReais("20.00"),
            gasCostPerHour = Money.fromReais("4.00")
        )

        val costs = RecipeCostCalculator.calculate(
            ingredients = listOf(chocolate, milk),
            times = times,
            settings = settings,
            packaging = Money.fromReais("4.50")
        )

        // Ingredients: 21.00 + 6.50 = 27.50
        assertEquals(2750L, costs.ingredients.cents)

        // Labor: (1h + 0.5h) = 1.5h * 1.15 = 1.725h * R$ 20,00 = R$ 34,50
        assertEquals(3450L, costs.labor.cents)

        // Gas: 0.5h * R$ 4,00 = R$ 2,00
        assertEquals(200L, costs.energy.cents)

        // Packaging: 4.50
        assertEquals(450L, costs.packaging.cents)

        // Total: 27.50 + 34.50 + 2.00 + 4.50 = 68.50
        assertEquals(6850L, costs.total.cents)
    }

    @Test
    fun `test pricing from target margin and interactive price update`() {
        val costs = RecipeCosts(
            ingredients = Money.fromReais("38.40"),
            labor = Money.fromReais("50.00"),
            energy = Money.fromReais("5.70"),
            packaging = Money.fromReais("4.50")
        )
        assertEquals(9860L, costs.total.cents) // R$ 98,60

        // Calculate price for 34.22% margin -> R$ 149,90
        val pricing = PricingEngine.fromTargetMargin(costs, BigDecimal("34.22"))
        assertEquals(14990L, pricing.salePrice.cents)
        assertEquals(5130L, pricing.profit.cents)
        assertEquals(BigDecimal("34.22"), pricing.marginPercent)

        // User edits sale price to R$ 160,00
        val edited = PricingEngine.fromSalePrice(costs, Money.fromReais("160.00"))
        assertEquals(16000L, edited.salePrice.cents)
        assertEquals(6140L, edited.profit.cents)
        assertTrue(edited.marginPercent > pricing.marginPercent)
    }

    @Test
    fun `test estimated vs actual cost comparison`() {
        val estimated = RecipeCosts(
            ingredients = Money.fromReais("38.40"),
            labor = Money.fromReais("50.00"),
            energy = Money.fromReais("5.70"),
            packaging = Money.fromReais("4.50")
        ) // 98.60

        val actualWithSavings = RecipeCosts(
            ingredients = Money.fromReais("34.00"),
            labor = Money.fromReais("50.00"),
            energy = Money.fromReais("5.70"),
            packaging = Money.fromReais("4.50")
        ) // 94.20 (economy of 4.40)

        val comparison = PricingEngine.compareEstimatedVsActual(
            estimated = estimated,
            actual = actualWithSavings,
            salePrice = Money.fromReais("149.90")
        )

        assertFalse(comparison.isCostHigher)
        assertEquals(440L, comparison.costDifference.cents)
        assertEquals(5570L, comparison.actualProfit.cents) // 149.90 - 94.20 = 55.70
        assertTrue(comparison.actualMarginPercent > comparison.estimatedMarginPercent)
    }
}
