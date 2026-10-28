package com.bruleetelie.domain

import java.math.BigDecimal
import org.junit.Assert.*
import org.junit.Test

class RecipeProfitabilityRankingTest {
    private fun sale(name: String, revenue: Long, profit: Long) = SaleSnapshot(name, name, "Q", "Cliente", name,
        Money(revenue), Money(revenue - profit), Money(revenue - profit), Money(profit), BigDecimal.ZERO, "Hoje")
    @Test fun `same recipe across quantities is aggregated and margin is weighted`() {
        val result = RecipeProfitabilityRanking.calculate(listOf(sale("10x Bolo", 10000, 4000), sale("20x Bolo", 30000, 6000), sale("Torta", 20000, 9000)))
        assertEquals(2, result.size); assertEquals("Bolo", result.first().name)
        assertEquals(2, result.first().salesCount); assertEquals(Money(10000), result.first().profit)
        assertEquals(BigDecimal("25.00"), result.first().marginPercent)
    }
    @Test fun `empty sales and zero revenue remain finite`() {
        assertTrue(RecipeProfitabilityRanking.calculate(emptyList()).isEmpty())
        assertEquals(BigDecimal.ZERO, RecipeProfitabilityRanking.calculate(listOf(sale("Amostra", 0, -200))).single().marginPercent)
    }
}
