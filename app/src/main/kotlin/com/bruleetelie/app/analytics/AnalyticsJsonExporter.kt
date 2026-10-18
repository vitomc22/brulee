package com.bruleetelie.app.analytics

import com.bruleetelie.data.db.PriceObservationEntity
import com.bruleetelie.domain.SaleSnapshot
import java.math.BigDecimal
import java.math.RoundingMode

object AnalyticsJsonExporter {
    fun export(
        sales: List<SaleSnapshot>,
        observations: List<PriceObservationEntity>,
        ingredientNames: Map<String, String>
    ): String = buildString {
        append("{\"schemaVersion\":1,\"sales\":[")
        sales.forEachIndexed { index, sale ->
            if (index > 0) append(',')
            append('{')
            field("id", sale.id); append(',')
            field("productName", sale.productName); append(',')
            number("salePriceCents", sale.salePrice.cents); append(',')
            number("estimatedCostCents", sale.estimatedCost.cents); append(',')
            number("actualCostCents", sale.actualCost.cents); append(',')
            number("profitCents", sale.profit.cents); append(',')
            number("activeProductionMinutes", sale.activeProductionMinutes.toLong()); append(',')
            number("completedAtMillis", sale.completedAtMillis)
            append('}')
        }
        append("],\"priceTrends\":[")
        observations.groupBy { it.ingredientId }.mapNotNull { (ingredientId, values) ->
            val pair = values.sortedByDescending { it.purchaseDateMillis }.take(2)
            if (pair.size < 2 || pair[1].normalizedUnitPriceCents == 0L) null else {
                val variation = BigDecimal(pair[0].normalizedUnitPriceCents - pair[1].normalizedUnitPriceCents)
                    .divide(BigDecimal(pair[1].normalizedUnitPriceCents), 4, RoundingMode.HALF_EVEN)
                    .movePointRight(2)
                Triple(ingredientNames[ingredientId] ?: ingredientId, variation, pair[0].purchaseDateMillis)
            }
        }.sortedByDescending { it.third }.forEachIndexed { index, trend ->
            if (index > 0) append(',')
            append('{'); field("ingredientName", trend.first); append(',')
            append("\"variationPercent\":").append(trend.second.toPlainString()); append('}')
        }
        append("]}")
    }

    private fun StringBuilder.field(name: String, value: String) {
        append('"').append(name).append("\":\"").append(escape(value)).append('"')
    }

    private fun StringBuilder.number(name: String, value: Long) {
        append('"').append(name).append("\":").append(value)
    }

    private fun escape(value: String): String = buildString {
        value.forEach { character ->
            when (character) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (character.code < 0x20) append("\\u%04x".format(character.code)) else append(character)
            }
        }
    }
}
