package com.bruleetelie.app

import com.bruleetelie.app.analytics.AnalyticsJsonExporter
import com.bruleetelie.domain.Money
import com.bruleetelie.domain.SaleSnapshot
import java.math.BigDecimal
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyticsJsonExporterTest {
    @Test
    fun `export escapes names and preserves monetary cents`() {
        val sale = SaleSnapshot(
            id = "s1", orderId = "o1", quoteCode = "#1", customerName = "Ana",
            productName = "Bolo \"Brûlée\"", salePrice = Money(10_090),
            estimatedCost = Money(5_000), actualCost = Money(5_100), profit = Money(4_990),
            marginPercent = BigDecimal("49.45"), completedAtFormatted = "Hoje",
            completedAtMillis = 123L
        )

        val json = AnalyticsJsonExporter.export(listOf(sale), emptyList(), emptyMap())

        assertTrue(json.contains("Bolo \\\"Brûlée\\\""))
        assertTrue(json.contains("\"salePriceCents\":10090"))
        assertTrue(json.contains("\"completedAtMillis\":123"))
    }
}
