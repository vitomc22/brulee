package com.bruleetelie.documents.ocr

import java.security.MessageDigest

enum class ConfidenceLevel { HIGH, MEDIUM, LOW }

data class ParsedReceiptItem(
    val rawDescription: String,
    val matchedIngredientName: String?,
    val confidence: ConfidenceLevel,
    val totalPriceCents: Long
)

data class ParsedReceipt(
    val cnpj: String?,
    val dateFormatted: String?,
    val items: List<ParsedReceiptItem>,
    val checksumSha256: String
)

class IngredientMatcher {
    fun match(description: String): Pair<String?, ConfidenceLevel> {
        val normalized = description.uppercase()
        return when {
            "LEITE COND" in normalized -> "Leite Condensado 395g" to ConfidenceLevel.HIGH
            "MANTEIGA" in normalized -> "Manteiga 200g" to ConfidenceLevel.HIGH
            "CHOCOLATE" in normalized -> "Chocolate em Pó 50% 1kg" to ConfidenceLevel.HIGH
            else -> null to ConfidenceLevel.LOW
        }
    }
}

class ReceiptParser(private val matcher: IngredientMatcher = IngredientMatcher()) {
    private val cnpjRegex = Regex("""\b\d{2}\.\d{3}\.\d{3}/\d{4}-\d{2}\b""")
    private val dateRegex = Regex("""\b\d{2}/\d{2}/\d{4}\b""")
    private val itemRegex = Regex("""^\s*\d+\s+(.+?)\s+R\$\s*([\d.,]+)\s*$""", RegexOption.IGNORE_CASE)

    fun parse(rawText: String): ParsedReceipt {
        val items = rawText.lineSequence().mapNotNull { line ->
            val result = itemRegex.matchEntire(line) ?: return@mapNotNull null
            val description = result.groupValues[1].trim()
            val (ingredient, confidence) = matcher.match(description)
            ParsedReceiptItem(description, ingredient, confidence, parseCents(result.groupValues[2]))
        }.toList()

        return ParsedReceipt(
            cnpj = cnpjRegex.find(rawText)?.value,
            dateFormatted = dateRegex.find(rawText)?.value,
            items = items,
            checksumSha256 = sha256(rawText.toByteArray())
        )
    }

    private fun parseCents(value: String): Long {
        val normalized = value.replace(".", "").replace(',', '.')
        return normalized.toBigDecimal().movePointRight(2).longValueExact()
    }

    private fun sha256(bytes: ByteArray): String =
        MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
}
