package com.bruleetelie.documents.pdf

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.bruleetelie.domain.Money
import com.bruleetelie.domain.Quote
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.math.BigDecimal
import java.math.RoundingMode

enum class PdfTemplateType { CUSTOMER, INTERNAL }

/** The human readable model is shared by the preview, export and privacy tests. */
class DefaultPdfGenerator {
    private data class Section(val title: String, val rows: List<Pair<String, String>>)
    private fun sections(quote: Quote, internal: Boolean): List<Section> = buildList {
        add(Section("PREPARADO PARA VOCÊ", buildList {
            add("Cliente" to quote.customer.name)
            if (quote.customer.phone.isNotBlank()) add("Contato" to contact(quote.customer.phone))
        }))
        add(Section("SEU PEDIDO", buildList {
            add("Produto" to quote.productName)
            if (quote.productSubtitle.isNotBlank()) add("Descrição" to quote.productSubtitle)
            add("Quantidade" to "${quote.unitsCount} unidades")
            if (quote.unitsCount > 0) add("Valor médio por unidade" to Money.fromReais(quote.pricing.salePrice.toBigDecimal()
                .divide(BigDecimal(quote.unitsCount), 2, RoundingMode.HALF_EVEN)).format())
        }))
        add(Section("ENTREGA E VALIDADE", listOf(
            "Entrega prevista" to quote.deliveryDate.ifBlank { "A combinar com o cliente" },
            "Validade da proposta" to "${quote.validDays} dias a partir da emissão"
        )))
        if (quote.notes.isNotBlank()) add(Section("OBSERVAÇÕES DO PEDIDO", listOf("" to quote.notes)))
        if (internal) add(Section("CONTROLE INTERNO • NÃO ENVIAR AO CLIENTE", listOf(
            "Ingredientes" to quote.pricing.costs.ingredients.format(),
            "Mão de obra" to quote.pricing.costs.labor.format(),
            "Gás e energia" to quote.pricing.costs.energy.format(),
            "Embalagem" to quote.pricing.costs.packaging.format(),
            "Outros custos" to quote.pricing.costs.overhead.format(),
            "Custo total" to quote.pricing.costs.total.format(),
            "Lucro" to quote.pricing.profit.format(),
            "Margem" to "${quote.pricing.marginPercent.toPlainString().replace('.', ',')}%"
        )))
    }

    private fun contact(value: String): String {
        val digits = value.filter(Char::isDigit)
        val country = digits.length in 12..13 && digits.startsWith("55")
        val local = if (country) digits.drop(2) else digits
        if (local.length !in 10..11) return value
        val prefix = if (country) "+55 " else ""
        return "$prefix(${local.take(2)}) ${local.substring(2, local.length - 4)}-${local.takeLast(4)}"
    }

    fun buildCustomerPdfDocument(quote: Quote): String = text(quote, false)
    fun buildInternalPdfDocument(quote: Quote): String = text(quote, true)
    private fun text(quote: Quote, internal: Boolean) = buildString {
        appendLine("BRÛLÉE TELIE")
        appendLine(if (internal) "Relatório interno" else "Orçamento")
        appendLine(quote.code)
        sections(quote, internal).forEach { section ->
            appendLine(section.title)
            section.rows.forEach { (label, value) -> appendLine(if (label.isEmpty()) value else "$label: $value") }
        }
        appendLine("TOTAL DO PEDIDO: ${quote.pricing.salePrice.format()}")
    }

    /** A4 layout with hierarchy, brand palette, wrapped text and repeated headers on overflow. */
    fun writePdf(quote: Quote, output: File, template: PdfTemplateType = PdfTemplateType.CUSTOMER) {
        output.parentFile?.mkdirs()
        val document = PdfDocument()
        val ink = Color.rgb(35, 43, 34)
        val olive = Color.rgb(59, 82, 62)
        val gold = Color.rgb(161, 115, 49)
        val cream = Color.rgb(253, 249, 241)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        fun font(size: Float, color: Int = ink, bold: Boolean = false) {
            paint.textSize = size
            paint.color = color
            paint.typeface = Typeface.create("sans-serif", if (bold) Typeface.BOLD else Typeface.NORMAL)
        }
        var number = 1
        var page = document.startPage(PdfDocument.PageInfo.Builder(595, 842, number).create())
        var y = 0f
        fun header() {
            page.canvas.drawColor(Color.WHITE)
            paint.color = cream
            page.canvas.drawRect(0f, 0f, 595f, 94f, paint)
            font(26f, olive, true)
            page.canvas.drawText("B", 42f, 56f, paint)
            font(13f, gold, true)
            page.canvas.drawText("BRÛLÉE TELIE", 76f, 42f, paint)
            font(10f)
            page.canvas.drawText("Confeitaria artesanal • Feito com cuidado", 76f, 61f, paint)
            font(10f, gold)
            page.canvas.drawText("${quote.code}  •  ${LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}", 42f, 80f, paint)
            y = 128f
        }
        fun finish() {
            font(9f, gold)
            page.canvas.drawText("Brûlée Telie  •  ${quote.code}", 42f, 814f, paint)
            page.canvas.drawText("Página $number", 495f, 814f, paint)
            document.finishPage(page)
        }
        fun space(height: Float) {
            if (y + height > 774f) {
                finish()
                number++
                page = document.startPage(PdfDocument.PageInfo.Builder(595, 842, number).create())
                header()
            }
        }
        try {
            header()
            font(26f, olive, true)
            page.canvas.drawText(if (template == PdfTemplateType.CUSTOMER) "Seu orçamento" else "Relatório interno", 42f, y, paint)
            y += 34f
            sections(quote, template == PdfTemplateType.INTERNAL).forEach { section ->
                space(68f)
                font(10f, gold, true)
                page.canvas.drawText(section.title, 42f, y, paint)
                y += 22f
                section.rows.forEach { (label, value) ->
                    if (label.isNotEmpty()) {
                        space(34f)
                        font(10f, Color.rgb(98, 100, 91))
                        page.canvas.drawText(label, 42f, y, paint)
                        y += 14f
                    }
                    font(13f, ink, label == "Produto")
                    value.lineSequence().forEach { paragraph ->
                        wrap(paragraph, paint, 511).forEach { line ->
                            space(17f)
                            // space() may start a page and change the header paint.
                            font(13f, ink, label == "Produto")
                            page.canvas.drawText(line, 42f, y, paint)
                            y += 17f
                        }
                    }
                    y += 5f
                }
                y += 12f
            }
            space(92f)
            paint.color = olive
            page.canvas.drawRoundRect(42f, y, 553f, y + 72f, 14f, 14f, paint)
            font(10f, Color.WHITE, true)
            page.canvas.drawText("TOTAL DO PEDIDO", 60f, y + 25f, paint)
            font(26f, Color.WHITE, true)
            page.canvas.drawText(quote.pricing.salePrice.format(), 60f, y + 55f, paint)
            finish()
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
            val fitting = paint.breakText(remaining, true, maxWidth.toFloat(), null).coerceAtLeast(1)
            val space = remaining.lastIndexOf(' ', fitting.coerceAtMost(remaining.lastIndex))
            val count = if (fitting < remaining.length && space > 0) space else fitting
            result += remaining.substring(0, count)
            remaining = remaining.substring(count).trimStart()
        }
        return result
    }
}
