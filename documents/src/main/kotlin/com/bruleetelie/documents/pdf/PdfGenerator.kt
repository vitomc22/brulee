package com.bruleetelie.documents.pdf

import android.graphics.Paint
import android.graphics.Typeface
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
        appendLine("NOTES=${quote.notes}")
    }

    /** Writes a paginated, shareable Android PDF while keeping the text representation for audits/tests. */
    fun writePdf(quote: Quote, output: File, template: PdfTemplateType = PdfTemplateType.CUSTOMER) {
        output.parentFile?.mkdirs()
        val content = if (template == PdfTemplateType.CUSTOMER) buildCustomerPdfDocument(quote)
        else buildInternalPdfDocument(quote)
        val document = PdfDocument()
        try {
            val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 14f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
            val titlePaint = Paint(bodyPaint).apply {
                textSize = 18f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val pageWidth = 595
            val pageHeight = 842
            val margin = 40f
            val lines = content.lineSequence()
                .flatMap { wrap(it, bodyPaint, (pageWidth - 2 * margin).toInt()).asSequence() }
                .toList()
            var pageNumber = 1
            var page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
            var y = 88f

            fun drawHeader() {
                page.canvas.drawText("BRÛLÉE TELIE", margin, 42f, titlePaint)
                page.canvas.drawLine(margin, 54f, pageWidth - margin, 54f, bodyPaint)
            }

            fun finishPage() {
                page.canvas.drawText("Página $pageNumber", margin, pageHeight - 24f, bodyPaint)
                document.finishPage(page)
            }

            drawHeader()
            lines.forEach { line ->
                if (y > pageHeight - 58f) {
                    finishPage()
                    pageNumber += 1
                    page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
                    y = 88f
                    drawHeader()
                }
                page.canvas.drawText(line, margin, y, bodyPaint)
                y += 22f
            }
            finishPage()
            output.outputStream().use(document::writeTo)
        } finally {
            document.close()
        }
    }

    private fun wrap(value: String, paint: Paint, maxWidth: Int): List<String> {
        if (value.isEmpty()) return listOf("")
        val result = mutableListOf<String>()
        var remaining = value
        while (remaining.isNotEmpty()) {
            val count = paint.breakText(remaining, true, maxWidth.toFloat(), null).coerceAtLeast(1)
            result += remaining.substring(0, count)
            remaining = remaining.substring(count)
        }
        return result
    }
}
