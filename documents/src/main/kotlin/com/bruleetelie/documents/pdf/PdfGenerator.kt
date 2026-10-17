package com.bruleetelie.documents.pdf

import com.bruleetelie.domain.Quote

enum class PdfTemplateType { CUSTOMER, INTERNAL }

class DefaultPdfGenerator {
    fun buildCustomerPdfDocument(quote: Quote): String = buildString {
        appendLine("CUSTOMER_QUOTE")
        appendLine("CUSTOMER=${quote.customer.name}")
        appendLine("PRODUCT=${quote.productName}")
        appendLine("QUANTITY=${quote.unitsCount}")
        appendLine("TOTAL=${quote.pricing.salePrice.format()}")
        appendLine("VALID_DAYS=${quote.validDays}")
        appendLine("NOTES=${quote.notes}")
    }

    fun buildInternalPdfDocument(quote: Quote): String = buildString {
        appendLine("INTERNAL_DETAILED_REPORT")
        appendLine("QUOTE=${quote.code}")
        appendLine("INGREDIENTS_COST=${quote.pricing.costs.ingredients.toBigDecimal()}")
        appendLine("LABOR_COST=${quote.pricing.costs.labor.toBigDecimal()}")
        appendLine("ENERGY_COST=${quote.pricing.costs.energy.toBigDecimal()}")
        appendLine("PACKAGING_COST=${quote.pricing.costs.packaging.toBigDecimal()}")
        appendLine("PROFIT=${quote.pricing.profit.toBigDecimal()}")
        appendLine("MARGIN_PERCENT=${quote.pricing.marginPercent}%")
    }
}
