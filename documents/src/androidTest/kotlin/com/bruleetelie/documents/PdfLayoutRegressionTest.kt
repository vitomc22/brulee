package com.bruleetelie.documents

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Base64
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.bruleetelie.documents.pdf.*
import com.bruleetelie.domain.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@SdkSuppress(minSdkVersion = 35)
class PdfLayoutRegressionTest {
    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    @get:Rule val fixtures = TemporaryFolder(context.cacheDir)
    private fun quote() = Quote("pdf-test", "#ORC-TESTE", Customer("c", "Ana Oliveira", "11987654321"),
        "Bolo de chocolate", "Cobertura artesanal e decoração em caramelo", 8,
        PricingEngine.fromSalePrice(RecipeCosts(Money(3000), Money(1000), Money(250), Money(350), Money(100)), Money(9111)),
        "25/10/2026 às 16h", 7, "Retirada na loja. Acabamento conforme combinado.")
    private fun renderer(file: File) = PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY))
    private fun text(renderer: PdfRenderer) = (0 until renderer.pageCount).joinToString("\n") { index ->
        renderer.openPage(index).use { it.textContents.joinToString("\n") { content -> content.text } }
    }.replace('\u00a0', ' ').replace(Regex("\\s+"), " ")

    @Test fun customerPdfIsOneStyledPageWithActualBusinessDataAndNoRawFieldsOrCosts() {
        val file = fixtures.newFile("customer.pdf")
        DefaultPdfGenerator().writePdf(quote(), file)
        renderer(file).use { document ->
            assertEquals("A normal quote must not spill its total onto a second page", 1, document.pageCount)
            val text = text(document)
            listOf("BRÛLÉE TELIE", "#ORC-TESTE", "Ana Oliveira", "Bolo de chocolate", "8 unidades", "25/10/2026", "7 dias", "Retirada na loja", "91,11").forEach {
                assertTrue("Missing customer information: $it\n$text", text.contains(it))
            }
            assertTrue(text.replace(" ", "").contains("(11)98765-4321"))
            listOf("CUSTOMER=", "VALID_DAYS=", "TOTAL=", "Lucro", "Margem", "Mão de obra", "30,00").forEach {
                assertFalse("Private or raw field in customer PDF: $it", text.contains(it))
            }
            document.openPage(0).use { page ->
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                try {
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    var olivePixels = 0
                    for (y in 0 until bitmap.height step 4) for (x in 0 until bitmap.width step 4) {
                        val pixel = bitmap.getPixel(x, y)
                        if (Color.red(pixel) in 55..65 && Color.green(pixel) in 78..86 && Color.blue(pixel) in 58..66) olivePixels++
                    }
                    assertTrue("The total must be drawn inside the branded olive card", olivePixels > 1000)
                } finally { bitmap.recycle() }
            }
        }
        // Explicit host-requested review artifact travels through instrumentation output; no file remains in the app.
        if (InstrumentationRegistry.getArguments().getString("exportPdfPreview") == "true") {
            InstrumentationRegistry.getInstrumentation().sendStatus(0, Bundle().apply {
                putString("stream", "\nPDF_PREVIEW_BASE64:" + Base64.encodeToString(file.readBytes(), Base64.NO_WRAP) + "\n")
            })
        }
    }

    @Test fun longNotesPaginateWithoutLosingTheFinalTermsAndTotal() {
        val file = fixtures.newFile("long.pdf")
        val notes = (1..180).joinToString("\n") { "Condição $it: decoração e entrega conforme aprovação do cliente." } + "\nFIM DAS CONDIÇÕES"
        DefaultPdfGenerator().writePdf(quote().copy(notes = notes), file)
        renderer(file).use {
            assertTrue(it.pageCount > 2)
            val content = text(it)
            assertTrue(content.contains("Condição 1:"))
            assertTrue(content.contains("FIM DAS CONDIÇÕES"))
            assertTrue("Missing total heading: ${content.takeLast(1000)}", content.replace(" ", "").contains("TOTALDOPEDIDO"))
            assertTrue(content.contains("91,11"))
        }
    }

    @Test fun internalPdfIncludesLocalizedCostsOverheadAndProfit() {
        val file = fixtures.newFile("internal.pdf")
        DefaultPdfGenerator().writePdf(quote(), file, PdfTemplateType.INTERNAL)
        renderer(file).use {
            val content = text(it)
            listOf("CONTROLE INTERNO", "Ingredientes", "Mão de obra", "Gás e energia", "Embalagem", "Outros custos", "Custo total", "Lucro", "Margem", "30,00", "47,00", "44,11").forEach { value ->
                assertTrue("Missing internal data: $value", content.contains(value))
            }
        }
    }
}
