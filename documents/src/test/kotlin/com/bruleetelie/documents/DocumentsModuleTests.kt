package com.bruleetelie.documents

import com.bruleetelie.documents.backup.BackupDataPayload
import com.bruleetelie.documents.backup.BackupManager
import com.bruleetelie.documents.ocr.ConfidenceLevel
import com.bruleetelie.documents.ocr.IngredientMatcher
import com.bruleetelie.documents.ocr.ReceiptParser
import com.bruleetelie.documents.ocr.DuplicateReceiptResult
import com.bruleetelie.documents.ocr.ReceiptDuplicateDetector
import com.bruleetelie.documents.pdf.DefaultPdfGenerator
import com.bruleetelie.documents.pdf.PdfTemplateType
import com.bruleetelie.domain.*
import java.io.File
import java.math.BigDecimal
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class DocumentsModuleTests {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val pdfGenerator = DefaultPdfGenerator()
    private val receiptParser = ReceiptParser()
    private val backupManager = BackupManager()

    private val sampleQuote = Quote(
        id = "quote-284",
        code = "#ORC-284",
        customer = Customer("c1", "Ana Souza", "(11) 98765-4321"),
        productName = "40 Brigadeiros Gourmet",
        productSubtitle = "Pistache tostado, Doce de Leite e Crème Brûlée",
        unitsCount = 40,
        pricing = PricingResult(
            costs = RecipeCosts(
                ingredients = Money.fromReais("38.40"),
                labor = Money.fromReais("50.00"),
                energy = Money.fromReais("5.70"),
                packaging = Money.fromReais("4.50")
            ),
            salePrice = Money.fromReais("149.90"),
            profit = Money.fromReais("51.30"),
            marginPercent = BigDecimal("34.22")
        ),
        deliveryDate = "Sábado, 14 de Outubro - 16:00",
        validDays = 3,
        notes = "Caixa Telie Ouro com laço acetinado e entrega inclusa."
    )

    @Test
    fun `test customer PDF generator NEVER exposes internal costs or profits`() {
        val pdfText = pdfGenerator.buildCustomerPdfDocument(sampleQuote)

        assertTrue(pdfText.contains("CUSTOMER_QUOTE"))
        assertTrue(pdfText.contains("Ana Souza"))
        assertTrue(pdfText.contains("40 Brigadeiros Gourmet"))
        assertTrue(pdfText.contains("149,90"))

        // Guarantee customer PDF privacy
        assertFalse(pdfText.contains("INGREDIENTS_COST"))
        assertFalse(pdfText.contains("LABOR_COST"))
        assertFalse(pdfText.contains("ENERGY_COST"))
        assertFalse(pdfText.contains("PROFIT"))
        assertFalse(pdfText.contains("MARGIN"))
        assertFalse(pdfText.contains("38.40"))
        assertFalse(pdfText.contains("50.00"))
        assertFalse(pdfText.contains("51.30"))
    }

    @Test
    fun `test internal PDF generator includes detailed costs and margin audit`() {
        val pdfText = pdfGenerator.buildInternalPdfDocument(sampleQuote)

        assertTrue(pdfText.contains("INTERNAL_DETAILED_REPORT"))
        assertTrue(pdfText.contains("INGREDIENTS_COST"))
        assertTrue(pdfText.contains("LABOR_COST"))
        assertTrue(pdfText.contains("PROFIT"))
        assertTrue(pdfText.contains("MARGIN_PERCENT"))
        assertTrue(pdfText.contains("34.22%"))
    }

    @Test
    fun `test receipt parser extracts CNPJ items and matches ingredients`() {
        val rawOcrText = """
            SUPERMERCADO DOCE AÇÚCAR
            CNPJ: 12.345.678/0001-90
            DATA: 15/10/2026
            
            1 LEITE COND MOCOCA 395G R$ 6,50
            2 MANTEIGA EXTRA 200G R$ 11,90
            3 CHOCOLATE EM PO 50% 1KG R$ 34,90
            
            TOTAL R$ 53,30
        """.trimIndent()

        val parsed = receiptParser.parse(rawOcrText)

        assertEquals("12.345.678/0001-90", parsed.cnpj)
        assertEquals("15/10/2026", parsed.dateFormatted)
        assertEquals(3, parsed.items.size)

        val firstItem = parsed.items.first()
        assertEquals("Leite Condensado 395g", firstItem.matchedIngredientName)
        assertEquals(ConfidenceLevel.HIGH, firstItem.confidence)
        assertEquals(650L, firstItem.totalPriceCents)

        assertTrue(parsed.checksumSha256.isNotBlank())
    }

    @Test
    fun `test duplicate detector identifies exact checksum`() {
        val detector = ReceiptDuplicateDetector()
        val receipt = receiptParser.parse(sampleReceiptText())

        assertEquals(DuplicateReceiptResult.NewReceipt, detector.checkAndRegister(receipt))
        assertEquals(DuplicateReceiptResult.ExactChecksum, detector.checkAndRegister(receipt))
    }

    @Test
    fun `test duplicate detector identifies same purchase despite OCR whitespace changes`() {
        val detector = ReceiptDuplicateDetector()
        val first = receiptParser.parse(sampleReceiptText())
        val rescanned = receiptParser.parse("\n${sampleReceiptText()}\n")

        assertEquals(DuplicateReceiptResult.NewReceipt, detector.checkAndRegister(first))
        assertEquals(
            DuplicateReceiptResult.MatchingReceiptDetails,
            detector.checkAndRegister(rescanned)
        )
    }

    @Test
    fun `receipt parser remains bounded for a large OCR payload`() {
        val payload = buildString {
            appendLine("SUPERMERCADO DOCE AÇÚCAR")
            appendLine("CNPJ: 12.345.678/0001-90")
            repeat(2_000) { index ->
                appendLine("${index + 1} LEITE COND MOCOCA 395G R$ 6,50")
                appendLine("ruído OCR sem item estruturado")
            }
        }

        val parsed = receiptParser.parse(payload)

        assertEquals(2_000, parsed.items.size)
        assertEquals("12.345.678/0001-90", parsed.cnpj)
        assertEquals(64, parsed.checksumSha256.length)
    }

    @Test
    fun `customer PDF text stays private with a large notes payload`() {
        val largeNotesQuote = sampleQuote.copy(notes = "nota ".repeat(20_000))

        val customerDocument = pdfGenerator.buildCustomerPdfDocument(largeNotesQuote)

        assertTrue(customerDocument.length > 100_000)
        assertFalse(customerDocument.contains("INGREDIENTS_COST"))
        assertFalse(customerDocument.contains("MARGIN_PERCENT"))
    }

    private fun sampleReceiptText(): String = """
        SUPERMERCADO DOCE AÇÚCAR
        CNPJ: 12.345.678/0001-90
        DATA: 15/10/2026
        1 LEITE COND MOCOCA 395G R$ 6,50
        2 MANTEIGA EXTRA 200G R$ 11,90
        TOTAL R$ 18,40
    """.trimIndent()

    @Test
    fun `test backup manager creates valid zip and verifies integrity`() {
        val payload = BackupDataPayload(
            quotesJson = "[{\"id\":\"q1\"}]",
            ordersJson = "[{\"id\":\"o1\"}]",
            salesJson = "[{\"id\":\"s1\"}]",
            ingredientsJson = "[{\"id\":\"i1\"}]",
            settingsJson = "{\"hourlyLaborRateCents\":2500}"
        )

        val zipFile = tempFolder.newFile("brulee_backup.zip")
        backupManager.createBackupZip(payload, zipFile)

        assertTrue(zipFile.exists())
        assertTrue(zipFile.length() > 0)

        val restoredPayload = backupManager.verifyAndExtractBackup(zipFile)
        assertEquals(payload.quotesJson, restoredPayload.quotesJson)
        assertEquals(payload.ordersJson, restoredPayload.ordersJson)
        assertEquals(payload.salesJson, restoredPayload.salesJson)
        assertEquals(payload.ingredientsJson, restoredPayload.ingredientsJson)
        assertEquals(payload.settingsJson, restoredPayload.settingsJson)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test backup manager rejects tampered backup with invalid checksum`() {
        val payload = BackupDataPayload("[]", "[]", "[]", "[]", "{}")
        val zipFile = tempFolder.newFile("tampered_backup.zip")

        // Create initial backup
        backupManager.createBackupZip(payload, zipFile)

        // Modify file content to simulate corruption
        zipFile.writeText("corrupted data content")

        // Must throw IllegalArgumentException due to checksum failure / corrupt format
        backupManager.verifyAndExtractBackup(zipFile)
    }
}
