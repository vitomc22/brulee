package com.bruleetelie.documents.ocr

data class ReceiptFingerprint(
    val checksumSha256: String,
    val cnpj: String?,
    val dateFormatted: String?,
    val totalPriceCents: Long
)

enum class DuplicateReceiptResult {
    NewReceipt,
    ExactChecksum,
    MatchingReceiptDetails
}

class ReceiptDuplicateDetector {
    private val fingerprints = linkedSetOf<ReceiptFingerprint>()

    fun checkAndRegister(receipt: ParsedReceipt): DuplicateReceiptResult {
        val fingerprint = receipt.fingerprint()
        val result = when {
            fingerprints.any { it.checksumSha256 == fingerprint.checksumSha256 } -> {
                DuplicateReceiptResult.ExactChecksum
            }

            fingerprint.hasReliableDetails() && fingerprints.any { existing ->
                existing.hasReliableDetails() &&
                    existing.cnpj == fingerprint.cnpj &&
                    existing.dateFormatted == fingerprint.dateFormatted &&
                    existing.totalPriceCents == fingerprint.totalPriceCents
            } -> DuplicateReceiptResult.MatchingReceiptDetails

            else -> DuplicateReceiptResult.NewReceipt
        }

        if (result == DuplicateReceiptResult.NewReceipt) {
            fingerprints += fingerprint
        }
        return result
    }

    private fun ReceiptFingerprint.hasReliableDetails(): Boolean =
        !cnpj.isNullOrBlank() && !dateFormatted.isNullOrBlank() && totalPriceCents > 0
}

fun ParsedReceipt.fingerprint(): ReceiptFingerprint = ReceiptFingerprint(
    checksumSha256 = checksumSha256,
    cnpj = cnpj,
    dateFormatted = dateFormatted,
    totalPriceCents = items.sumOf(ParsedReceiptItem::totalPriceCents)
)
