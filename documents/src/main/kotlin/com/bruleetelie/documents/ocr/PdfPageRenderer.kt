package com.bruleetelie.documents.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri

/** Renders one PDF page at a time so large receipts do not exhaust memory. */
class PdfPageRenderer {
    fun renderAll(context: Context, uri: Uri, onPage: (page: Int, bitmap: Bitmap) -> Unit) {
        context.contentResolver.openFileDescriptor(uri, "r").use { descriptor ->
            requireNotNull(descriptor) { "Não foi possível abrir o PDF" }
            PdfRenderer(descriptor).use { renderer ->
                for (index in 0 until renderer.pageCount) {
                    renderer.openPage(index).use { page ->
                        val width = (page.width * 2).coerceAtMost(2400)
                        val height = (page.height * width / page.width).coerceAtMost(3200)
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        bitmap.eraseColor(android.graphics.Color.WHITE)
                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        onPage(index, bitmap)
                        bitmap.recycle()
                    }
                }
            }
        }
    }
}
