package com.bruleetelie.app

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.bruleetelie.app.ui.screens.*
import com.bruleetelie.app.ui.theme.BruleeTheme
import com.bruleetelie.app.sharing.ShareFile
import com.bruleetelie.data.BruleeRepository
import com.bruleetelie.domain.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import android.content.Intent

class OperationalScreensRegressionTest {
    @get:Rule val rule = createComposeRule()
    private fun quote() = Quote("q1", "#ORC-REAL", Customer("c1", "Cliente real", "11999998888"), "Bolo", "", 1,
        PricingEngine.fromSalePrice(RecipeCosts(Money(2000), Money(1000), Money(200), Money(300)), Money(6000)), "21/10/2026 16:00")
    @Test fun pendingQuotesCanBeSharedApprovedOrRejectedAndAllFilterWorks() {
        var quotes by mutableStateOf(listOf(quote(), quote().copy(id = "done", status = QuoteStatus.ConvertedToSale)))
        var shared = ""; var pdf = ""; var approved = ""
        rule.setContent { BruleeTheme { QuotesScreen(quotes, {}, { approved = it.id }, { shared = it.id }, { pdf = it.id }, { q -> quotes = quotes.map { if (it.id == q.id) it.copy(status = QuoteStatus.Rejected) else it } }) } }
        rule.onNodeWithTag("quote_card_done").assertDoesNotExist()
        rule.onNodeWithTag("quote_whatsapp_q1").performScrollTo().performClick()
        rule.onNodeWithTag("quote_pdf_q1").performScrollTo().performClick()
        rule.onNodeWithTag("quote_approve_q1").performScrollTo().performClick()
        rule.runOnIdle { assertEquals("q1", shared); assertEquals("q1", pdf); assertEquals("q1", approved) }
        rule.onNodeWithTag("quote_reject_q1").performScrollTo().performClick()
        rule.onNodeWithTag("quote_card_q1").assertDoesNotExist()
        rule.onNodeWithTag("quotes_all").performScrollTo().performClick()
        rule.onNodeWithTag("quote_card_done").assertExists()
        rule.onNodeWithTag("quote_approve_done").assertDoesNotExist()
    }
    @Test fun inventoryAcceptsOnlyValidPricesAndCanAddToShopping() {
        val repo = BruleeRepository()
        val ingredient = repo.addIngredient("Chocolate real", "kg")
        repo.addPriceObservation(ingredient.id, "", "1", "kg", 5000, 5000)
        var saved: List<String>? = null; var buy = ""
        val ingredientSnapshot = repo.ingredients.value
        val observationSnapshot = repo.priceObservations.value
        rule.setContent { BruleeTheme { InventoryScreen(ingredientSnapshot, observationSnapshot, {}, { name, qty, unit, price -> saved = listOf(name, qty, unit, price.cents.toString()) }, { buy = it }) } }
        rule.onNodeWithTag("stock_save").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("stock_name").performScrollTo().performTextInput("Farinha")
        rule.onNodeWithTag("stock_quantity").performTextInput("1000")
        rule.onNodeWithTag("stock_price").performTextInput("-10")
        rule.onNodeWithTag("stock_save").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("stock_price").performScrollTo().performTextReplacement("8,50")
        rule.onNodeWithTag("stock_save").performScrollTo().performClick()
        rule.runOnIdle { assertEquals(listOf("Farinha", "1000", "g", "850"), saved) }
        rule.onNodeWithTag("stock_buy_${ingredient.id}").performScrollTo().performClick()
        rule.runOnIdle { assertEquals("Chocolate real", buy) }
    }
    @Test fun fixedCostsAndDefaultMarginAreEditableAndAnalyticsExportIsReachable() {
        var settings by mutableStateOf(OperatingSettings())
        var export = false
        rule.setContent { BruleeTheme { BackupScreen(settings, { settings = it }, {}, {}, { export = true }) } }
        rule.onNodeWithTag("settings_fixed_costs").performScrollTo().performTextReplacement("500,00")
        rule.onNodeWithTag("settings_default_margin").performScrollTo().performTextReplacement("40")
        rule.onNodeWithTag("analytics_export").performScrollTo().performClick()
        rule.runOnIdle { assertEquals(50000L, settings.monthlyFixedCosts.cents); assertEquals(40.toBigDecimal(), settings.defaultMarginPercent); assertTrue(export) }
    }
    @Test fun WhatsAppUsesViewIntentWithEncodedCustomerMessage() {
        val url = com.bruleetelie.documents.DefaultDocumentGateway().formatWhatsAppUrl(quote())
        val intent = ShareFile.whatsAppIntent(url)
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals("5511999998888", intent.data!!.getQueryParameter("phone"))
        assertTrue(intent.data!!.getQueryParameter("text")!!.contains("Cliente real"))
        assertFalse(intent.data!!.getQueryParameter("text")!!.contains("margem", true))
    }
    @Test fun OCRStartsEmptySupportsPdfAndDisablesActionsWhileProcessing() {
        var processing by mutableStateOf(false)
        var imports = 0
        rule.setContent { BruleeTheme { OcrCaptureScreen({}, onPdfRequested = { imports++; processing = true }, processing = processing) } }
        rule.onNodeWithTag("ocr_process_document").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("ocr_raw_text").performScrollTo().performTextInput("Texto de compra")
        rule.onNodeWithTag("ocr_process_document").performScrollTo().assertIsEnabled()
        rule.onNodeWithTag("ocr_import_pdf").performScrollTo().performClick()
        rule.onNodeWithTag("ocr_scan_document").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("ocr_process_document").performScrollTo().assertIsNotEnabled()
        rule.runOnIdle { assertEquals(1, imports) }
    }

    @Test fun realCustomerPdfIsSharedFromApplicationContextWithReadableUri() {
        val instrumentation = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext.applicationContext
        val file = java.io.File(java.io.File(context.cacheDir, "shared").apply { mkdirs() }, "share-test-${java.util.UUID.randomUUID()}.pdf")
        var captured: Intent? = null
        val monitor = object : android.app.Instrumentation.ActivityMonitor() {
            override fun onStartActivity(intent: Intent): android.app.Instrumentation.ActivityResult? {
                if (intent.action == Intent.ACTION_CHOOSER) {
                    captured = intent
                    return android.app.Instrumentation.ActivityResult(android.app.Activity.RESULT_CANCELED, null)
                }
                return null
            }
        }
        instrumentation.addMonitor(monitor)
        try {
            com.bruleetelie.documents.pdf.DefaultPdfGenerator().writePdf(quote(), file, com.bruleetelie.documents.pdf.PdfTemplateType.CUSTOMER)
            val uri = ShareFile.uriForFile(context, file)
            assertTrue(context.contentResolver.openInputStream(uri)!!.bufferedReader(Charsets.ISO_8859_1).use { it.readLine() }.startsWith("%PDF-"))
            ShareFile.shareUri(context, uri, "application/pdf", "Compartilhar orçamento")
            assertTrue(captured!!.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
            @Suppress("DEPRECATION") val send = captured!!.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)!!
            assertEquals(Intent.ACTION_SEND, send.action)
            assertEquals("application/pdf", send.type)
            assertTrue(send.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION != 0)
        } finally {
            instrumentation.removeMonitor(monitor)
            file.delete()
        }
    }

}
