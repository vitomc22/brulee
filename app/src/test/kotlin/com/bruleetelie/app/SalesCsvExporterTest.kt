package com.bruleetelie.app

import com.bruleetelie.app.analytics.SalesCsvExporter
import com.bruleetelie.domain.Money
import com.bruleetelie.domain.PaymentStatus
import com.bruleetelie.domain.SaleSnapshot
import java.math.BigDecimal
import org.junit.Assert.assertTrue
import org.junit.Test

class SalesCsvExporterTest {
    @Test
    fun `export preserves cents and escapes csv fields`() {
        val csv = SalesCsvExporter.export(listOf(SaleSnapshot(
            id = "s1", orderId = "o1", quoteCode = "#ORC-1", customerName = "Ana, Souza",
            productName = "Bolo \"Especial\"", salePrice = Money.fromReais("149.90"),
            estimatedCost = Money.fromReais("80.00"), actualCost = Money.fromReais("90.00"),
            profit = Money.fromReais("59.90"), marginPercent = BigDecimal("39.96"),
            completedAtFormatted = "18/10/2026", paymentStatus = PaymentStatus.Paid
        )))
        assertTrue(csv.contains("14990"))
        assertTrue(csv.contains("\"Ana, Souza\""))
        assertTrue(csv.contains("\"Bolo \"\"Especial\"\"\""))
    }
}
