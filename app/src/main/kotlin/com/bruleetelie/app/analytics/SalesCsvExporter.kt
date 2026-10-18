package com.bruleetelie.app.analytics

import com.bruleetelie.domain.SaleSnapshot

object SalesCsvExporter {
    fun export(sales: List<SaleSnapshot>): String = buildString {
        appendLine("id,codigo,produto,cliente,preco_venda_centavos,custo_real_centavos,lucro_centavos,margem_percentual,data_conclusao")
        sales.forEach { sale ->
            appendLine(listOf(
                sale.id,
                sale.quoteCode,
                sale.productName,
                sale.customerName,
                sale.salePrice.cents,
                sale.actualCost.cents,
                sale.profit.cents,
                sale.marginPercent.toPlainString(),
                sale.completedAtMillis
            ).joinToString(",") { escape(it.toString()) })
        }
    }

    private fun escape(value: String): String =
        if (value.any { it == ',' || it == '"' || it == '\r' || it == '\n' }) {
            "\"${value.replace("\"", "\"\"")}\""
        } else value
}
