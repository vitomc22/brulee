package com.bruleetelie.documents.pdf

import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.bruleetelie.domain.Quote
import java.io.File

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

    /** Writes a real, shareable Android PDF while keeping the text representation for audits/tests. */
    fun writePdf(quote: Quote, output: File, template: PdfTemplateType = PdfTemplateType.CUSTOMER) {
        output.parentFile?.mkdirs()
        val content = if (template == PdfTemplateType.CUSTOMER) {
            buildCustomerPdfDocument(quote)
        } else {
            buildInternalPdfDocument(quote)
        }
        val document = PdfDocument()
        try {
            val page = document.startPage(PdfDocument.PageInfo.Builder(595, 842, 1).create())
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 14f }
            content.lineSequence().forEachIndexed { index, line ->
                page.canvas.drawText(line, 40f, 60f + index * 24f, paint)
            }
            document.finishPage(page)
            output.outputStream().use(document::writeTo)
        } finally {
            document.close()
        }
    }
}
