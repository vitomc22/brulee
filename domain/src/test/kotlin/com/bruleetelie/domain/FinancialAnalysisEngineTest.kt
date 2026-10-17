package com.bruleetelie.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class FinancialAnalysisEngineTest {

    @Test
    fun testCalculateSummary() {
        val sales = listOf(
            SaleSnapshot(
                id = "1",
                orderId = "o1",
                quoteCode = "#001",
                customerName = "Ana",
                productName = "Bolo",
                salePrice = Money.fromReais("100.00"),
                estimatedCost = Money.fromReais("40.00"),
                actualCost = Money.fromReais("50.00"),
                profit = Money.fromReais("50.00"),
                marginPercent = BigDecimal("50.00"),
                completedAtFormatted = "Hoje"
            )
        )

        val summary = FinancialAnalysisEngine.calculateSummary(
            sales = sales,
            totalProductionHours = BigDecimal("2.0"),
            approvedQuotesCount = 1,
            totalEligibleQuotesCount = 2
        )

        assertEquals(Money.fromReais("100.00"), summary.totalRevenue)
        assertEquals(Money.fromReais("50.00"), summary.totalCost)
        assertEquals(Money.fromReais("50.00"), summary.totalProfit)
        assertEquals(1, summary.salesCount)
        assertEquals(BigDecimal("50.00"), summary.conversionRatePercent)
        assertEquals(Money.fromReais("25.00"), summary.profitPerHour)
    }

    @Test
    fun testBreakEven() {
        val fixed = Money.fromReais("1000.00")
        val rev = Money.fromReais("5000.00")
        val varCost = Money.fromReais("2000.00")

        val be = FinancialAnalysisEngine.calculateBreakEven(fixed, rev, varCost)
        assertEquals(fixed, be.fixedCosts)
        // Contribution margin = 3000 / 5000 = 60% (0.6)
        // Break-even revenue = 1000 / 0.6 = 1667
        assertEquals(Money.fromReais("1667.00"), be.breakEvenRevenue)
    }

    @Test
    fun testPriceVariation() {
        val alert = FinancialAnalysisEngine.analyzePriceVariation(
            ingredientName = "Chocolate",
            oldPrice = Money.fromReais("30.00"),
            newPrice = Money.fromReais("36.00")
        )
        assertEquals("Chocolate", alert.ingredientName)
        assertEquals(true, alert.isIncrease)
        assertEquals(BigDecimal("20.00"), alert.variationPercent)
    }
}
