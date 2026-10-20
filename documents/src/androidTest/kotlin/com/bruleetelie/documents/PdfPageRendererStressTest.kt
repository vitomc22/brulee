package com.bruleetelie.documents

import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruleetelie.documents.ocr.PdfPageRenderer
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicInteger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PdfPageRendererStressTest {
    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    @get:org.junit.Rule val fixtures = org.junit.rules.TemporaryFolder(context.cacheDir)
    private val renderer = PdfPageRenderer()

    @Test
    fun rendersTwentyAndFiftyPageDocumentsWithoutRetainingBitmaps() {
        listOf(20, 50).forEach { pageCount ->
            val pdf = createPdf(pageCount)
            val output = File(fixtures.root, "pdf-pages-$pageCount")
            val pages = renderer.renderToFiles(context, android.net.Uri.fromFile(pdf), output)
            assertEquals(pageCount, pages.size)
            assertTrue(pages.all { File(it.path!!).length() > 100 })
            output.deleteRecursively()
            pdf.delete()
        }
    }

    @Test
    fun corruptedPdfFailsClearly() {
        val file = File(fixtures.root, "corrupted.pdf").apply { writeText("not a PDF") }
        try {
            renderer.renderAll(context, android.net.Uri.fromFile(file)) { _, _ -> }
            throw AssertionError("corrupted PDF unexpectedly rendered")
        } catch (expected: Exception) {
            assertTrue(expected.message.orEmpty().isNotBlank())
        } finally {
            file.delete()
        }
    }

    @Test
    fun encryptedPdfIsRejectedWithoutCreatingPages() {
        val file = File(fixtures.root, "encrypted-receipt.pdf")
        context.assets.open("pdf/encrypted_receipt.pdf").use { input ->
            file.outputStream().use(input::copyTo)
        }
        try {
            renderer.renderAll(context, android.net.Uri.fromFile(file)) { _, _ -> }
            throw AssertionError("encrypted PDF unexpectedly rendered")
        } catch (expected: Exception) {
            assertTrue(expected.message.orEmpty().isNotBlank())
        } finally {
            file.delete()
        }
    }

    @Test
    fun cancellationStopsBeforeNextPageAndRepeatedCyclesRemainStable() {
        val pdf = createPdf(20)
        try {
            val rendered = AtomicInteger()
            try {
                renderer.renderAll(
                    context,
                    android.net.Uri.fromFile(pdf),
                    shouldContinue = { rendered.incrementAndGet() < 5 }
                ) { _, _ -> }
                throw AssertionError("cancelled PDF unexpectedly completed")
            } catch (expected: IllegalStateException) {
                assertTrue(rendered.get() < 6)
            }
            repeat(3) {
                val output = File(fixtures.root, "pdf-repeat-$it")
                assertEquals(20, renderer.renderToFiles(context, android.net.Uri.fromFile(pdf), output).size)
                output.deleteRecursively()
            }
        } finally {
            pdf.delete()
        }
    }

    private fun createPdf(pageCount: Int): File {
        val file = File(fixtures.root, "fixture-$pageCount-${System.nanoTime()}.pdf")
        val document = PdfDocument()
        val paint = Paint().apply { color = Color.BLACK; textSize = 18f }
        repeat(pageCount) { index ->
            val page = document.startPage(PdfDocument.PageInfo.Builder(595, 842, index + 1).create())
            page.canvas.drawText("OCR fixture page ${index + 1}", 40f, 80f, paint)
            document.finishPage(page)
        }
        FileOutputStream(file).use(document::writeTo)
        document.close()
        return file
    }
}
