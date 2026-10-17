package com.bruleetelie.documents.ocr

import java.security.MessageDigest
import java.util.Locale

enum class ConfidenceLevel {
    HIGH,
    MEDIUM,
    LOW
}

data class ParsedReceiptItem(
    val id: String,
    val rawText: String,
    val detectedProductName: String,
    val matchedIngredientName: String?,
    val quantityValue: String,
    val quantityUnit: String,
    val unitPriceCents: Long,
    val totalPriceCents: Long,
    val confidence: ConfidenceLevel
)

data class ParsedReceipt(
    val establishmentName: String,
    val cnpj: String,
    val dateFormatted: String,
    val items: List<ParsedReceiptItem>,
    val totalAmountCents: Long,
    val checksumSha256: String
)

class IngredientMatcher(
    private val knownIngredients: List<String> = defaultKnownIngredients()
) {
    private val aliasesMap = mapOf(
        "mussarela" to "Mussarela",
        "ovos" to "Ovos Brancos Grandes",
        "leite cond" to "Leite Condensado 395g",
        "leite condensado" to "Leite Condensado 395g",
        "creme de leite" to "Creme de Leite 200g",
        "chocolate" to "Chocolate em Pó 50%",
        "manteiga" to "Manteiga Extra Sem Sal",
        "pistache" to "Pasta de Pistache Puro",
        "farinha" to "Farinha de Trigo Especial",
        "açucar" to "Açúcar de Confeiteiro",
        "acucar" to "Açúcar de Confeiteiro"
    )

    fun match(rawItemText: String): Pair<String?, ConfidenceLevel> {
        val normalized = rawItemText.lowercase(Locale.ROOT)
            .replace("[^a-z0-9\\s]".toRegex(), " ")
            .trim()

        // 1. Direct alias match
        for ((alias, canonical) in aliasesMap) {
            if (normalized.contains(alias)) {
                return Pair(canonical, ConfidenceLevel.HIGH)
            }
        }

        // 2. Token overlap with known ingredients
        for (known in knownIngredients) {
            val knownNormalized = known.lowercase(Locale.ROOT)
            val tokens = knownNormalized.split(" ").filter { it.length > 3 }
            val matchCount = tokens.count { normalized.contains(it) }
            if (matchCount >= 2) {
                return Pair(known, ConfidenceLevel.HIGH)
            } else if (matchCount == 1) {
                return Pair(known, ConfidenceLevel.MEDIUM)
            }
        }

        return Pair(null, ConfidenceLevel.LOW)
    }

    companion object {
        fun defaultKnownIngredients() = listOf(
            "Chocolate em Pó 50%",
            "Leite Condensado 395g",
            "Creme de Leite 200g",
            "Manteiga Extra Sem Sal",
            "Pasta de Pistache Puro",
            "Farinha de Trigo Especial",
            "Açúcar de Confeiteiro",
            "Ovos Brancos Grandes"
        )
    }
}

class ReceiptParser(
    private val ingredientMatcher: IngredientMatcher = IngredientMatcher()
) {

    fun parse(rawOcrText: String): ParsedReceipt {
        val lines = rawOcrText.lines().map { it.trim() }.filter { it.isNotBlank() }

        var establishment = "Estabelecimento Comercial"
        var cnpj = ""
        var dateStr = "Hoje"
        val items = mutableListOf<ParsedReceiptItem>()
        var totalCents = 0L

        val cnpjRegex = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}".toRegex()
        val dateRegex = "\\d{2}/\\d{2}/\\d{4}".toRegex()
        val priceRegex = "R?\\$\\s*(\\d+[.,]\\d{2})".toRegex()

        for (line in lines) {
            // Extract CNPJ
            cnpjRegex.find(line)?.let { cnpj = it.value }

            // Extract Date
            dateRegex.find(line)?.let { dateStr = it.value }

            // Extract establishment (first line that looks like store name)
            if (establishment == "Estabelecimento Comercial" && !line.contains("CNPJ") && !line.contains("TOTAL")) {
                establishment = line
            }

            // Extract item lines (containing quantity x price or total price)
            val matches = priceRegex.findAll(line).toList()
            if (matches.isNotEmpty() && !line.contains("TOTAL", ignoreCase = true) && !line.contains("TROCO", ignoreCase = true)) {
                val priceStr = matches.last().groupValues[1].replace(",", ".")
                val cents = (priceStr.toDouble() * 100).toLong()

                val rawName = line.replace(priceRegex, "").trim()
                val (matchedIng, confidence) = ingredientMatcher.match(rawName)

                items.add(
                    ParsedReceiptItem(
                        id = "item-${items.size + 1}",
                        rawText = line,
                        detectedProductName = if (rawName.isBlank()) "Produto Detectado" else rawName,
                        matchedIngredientName = matchedIng,
                        quantityValue = "1",
                        quantityUnit = "un",
                        unitPriceCents = cents,
                        totalPriceCents = cents,
                        confidence = confidence
                    )
                )
                totalCents += cents
            }
        }

        val checksum = calculateSha256(rawOcrText)

        return ParsedReceipt(
            establishmentName = establishment,
            cnpj = if (cnpj.isBlank()) "00.000.000/0001-00" else cnpj,
            dateFormatted = dateStr,
            items = items,
            totalAmountCents = totalCents,
            checksumSha256 = checksum
        )
    }

    private fun calculateSha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }
}
