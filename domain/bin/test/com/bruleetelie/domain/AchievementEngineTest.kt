package com.bruleetelie.domain

import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneOffset
import org.junit.Assert.assertEquals
import org.junit.Test

class AchievementEngineTest {
    private val today = LocalDate.of(2026, 10, 18)

    @Test
    fun `counts one sale day only once and walks consecutive days`() {
        val sales = listOf(sale(today), sale(today), sale(today.minusDays(1)), sale(today.minusDays(2)))
        assertEquals(3, AchievementEngine.salesStreak(sales, today, ZoneOffset.UTC))
    }

    @Test
    fun `allows streak whose last sale was yesterday`() {
        val sales = listOf(sale(today.minusDays(1)), sale(today.minusDays(2)))
        assertEquals(2, AchievementEngine.salesStreak(sales, today, ZoneOffset.UTC))
    }

    @Test
    fun `stops profitable streak on first loss`() {
        val sales = listOf(sale(today, 100), sale(today.minusDays(1), 200), sale(today.minusDays(2), -1))
        assertEquals(2, AchievementEngine.profitableSalesStreak(sales))
    }

    private fun sale(day: LocalDate, profit: Long = 100) = SaleSnapshot(
        id = day.toString() + profit,
        orderId = "order",
        quoteCode = "quote",
        customerName = "Cliente",
        productName = "Produto",
        salePrice = Money(500),
        estimatedCost = Money(400),
        actualCost = Money(400),
        profit = Money(profit),
        marginPercent = BigDecimal.TEN,
        completedAtFormatted = day.toString(),
        completedAtMillis = day.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )
}
