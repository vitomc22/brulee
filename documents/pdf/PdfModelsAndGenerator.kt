package com.bruleetelie.documents.pdf

import com.bruleetelie.domain.Quote
import java.io.File
import java.io.FileOutputStream

enum class PdfTemplateType {
    CUSTOMER,
    INTERNAL_DETAILED
}

interface PdfGenerator {
    fun generateQuotePdf(quote: Quote, templateType: PdfTemplateType, outputFile: File): File
}

class DefaultPdfGenerator : PdfGenerator {

    override fun generateQuotePdf(quote: Quote, templateType: PdfTemplateType, outputFile: File): File {
        val content = when (templateType) {
            PdfTemplateType.CUSTOMER -> buildCustomerPdfDocument(quote)
            PdfTemplateType.INTERNAL_DETAILED -> buildInternalPdfDocument(quote)
        }

        outputFile.parentFile?.mkdirs()
        FileOutputStream(outputFile).use { out ->
            out.write(content.toByteArray(Charsets.UTF_8))
        }
        return outputFile
    }

    fun buildCustomerPdfDocument(quote: Quote): String {
        return buildString {
            appendLine("%PDF-1.4")
            appendLine("% BRÛLÉE TELIE — CUSTOMER QUOTE PDF")
            appendLine("DOCUMENT_TYPE: CUSTOMER_QUOTE")
            appendLine("BRAND: Brûlée Telie Confeitaria Artesanal")
            appendLine("QUOTE_CODE: ${quote.code}")
            appendLine("CUSTOMER_NAME: ${quote.customer.name}")
            appendLine("CUSTOMER_PHONE: ${quote.customer.phone}")
            appendLine("PRODUCT_NAME: ${quote.productName}")
            if (quote.productSubtitle.isNotBlank()) {
                appendLine("PRODUCT_SUBTITLE: ${quote.productSubtitle}")
            }
            appendLine("UNITS_COUNT: ${quote.unitsCount}")
            appendLine("DELIVERY_DATE: ${quote.deliveryDate}")
            appendLine("VALID_DAYS: ${quote.validDays}")
            appendLine("TOTAL_SALE_PRICE: ${quote.pricing.salePrice.format()}")
            if (quote.notes.isNotBlank()) {
                appendLine("NOTES: ${quote.notes}")
            }
            appendLine("FOOTER: Obrigado pela preferência! Atenciosamente, Brûlée Telie.")
            appendLine("%%EOF")
        }
    }

    fun buildInternalPdfDocument(quote: Quote): String {
        val costs = quote.pricing.costs
        return buildString {
            appendLine("%PDF-1.4")
            appendLine("% BRÛLÉE TELIE — INTERNAL DETAILED AUDIT REPORT")
            appendLine("DOCUMENT_TYPE: INTERNAL_DETAILED_REPORT")
            appendLine("QUOTE_CODE: ${quote.code}")
            appendLine("CUSTOMER: ${quote.customer.name}")
            appendLine("PRODUCT: ${quote.productName}")
            appendLine("--- COST BREAKDOWN ---")
            appendLine("INGREDIENTS_COST: ${costs.ingredients.format()}")
            appendLine("LABOR_COST: ${costs.labor.format()}")
            appendLine("ENERGY_COST: ${costs.energy.format()}")
            appendLine("PACKAGING_COST: ${costs.packaging.format()}")
            appendLine("OVERHEAD_COST: ${costs.overhead.format()}")
            appendLine("TOTAL_COST: ${costs.total.format()}")
            appendLine("--- PRICING & MARGIN ---")
            appendLine("SALE_PRICE: ${quote.pricing.salePrice.format()}")
            appendLine("PROFIT: ${quote.pricing.profit.format()}")
            appendLine("MARGIN_PERCENT: ${quote.pricing.marginPercent}%")
            appendLine("MARKUP: ${quote.pricing.markup}x")
            appendLine("%%EOF")
        }
    }
}
