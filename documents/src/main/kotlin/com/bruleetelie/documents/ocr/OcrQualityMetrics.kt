package com.bruleetelie.documents.ocr

/** Deterministic OCR quality measurements used by fixtures and CI regression tests. */
data class OcrQualityReport(
    val characterErrorRate: Double,
    val wordErrorRate: Double,
    val itemPrecision: Double,
    val itemRecall: Double,
    val priceAccuracy: Double,
    val quantityAccuracy: Double,
    val totalAccuracy: Double
)

object OcrQualityMetrics {
    fun compare(expected: String, actual: String): Pair<Double, Double> {
        val expectedWords = words(expected)
        val actualWords = words(actual)
        return levenshtein(expected, actual).toDouble() / expected.length.coerceAtLeast(1) to
            levenshtein(expectedWords, actualWords).toDouble() / expectedWords.size.coerceAtLeast(1)
    }

    fun report(
        expectedText: String,
        actualText: String,
        expectedItems: Set<String>,
        actualItems: Set<String>,
        expectedPrices: Map<String, Long>,
        actualPrices: Map<String, Long>,
        expectedQuantities: Map<String, String>,
        actualQuantities: Map<String, String>,
        expectedTotalCents: Long,
        actualTotalCents: Long
    ): OcrQualityReport {
        val (cer, wer) = compare(expectedText, actualText)
        val truePositives = expectedItems.intersect(actualItems).size.toDouble()
        return OcrQualityReport(
            characterErrorRate = cer,
            wordErrorRate = wer,
            itemPrecision = truePositives / actualItems.size.coerceAtLeast(1),
            itemRecall = truePositives / expectedItems.size.coerceAtLeast(1),
            priceAccuracy = accuracy(expectedPrices, actualPrices),
            quantityAccuracy = accuracy(expectedQuantities, actualQuantities),
            totalAccuracy = if (expectedTotalCents == actualTotalCents) 1.0 else 0.0
        )
    }

    private fun accuracy(expected: Map<*, *>, actual: Map<*, *>): Double {
        if (expected.isEmpty()) return 1.0
        return expected.count { (key, value) -> actual[key] == value }.toDouble() / expected.size
    }

    private fun levenshtein(left: String, right: String): Int =
        levenshtein(left.toList(), right.toList())

    private fun <T> levenshtein(left: List<T>, right: List<T>): Int {
        var previous = IntArray(right.size + 1) { it }
        for (i in left.indices) {
            val current = IntArray(right.size + 1)
            current[0] = i + 1
            for (j in right.indices) {
                current[j + 1] = minOf(
                    current[j] + 1,
                    previous[j + 1] + 1,
                    previous[j] + if (left[i] == right[j]) 0 else 1
                )
            }
            previous = current
        }
        return previous.last()
    }

    private fun words(value: String): List<String> =
        value.trim().split(Regex("\\s+")).filter(String::isNotEmpty)
}
