package com.bruleetelie.documents.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/** Renders one PDF page at a time so large receipts do not exhaust memory. */
class PdfPageRenderer {
    /** Renders pages to private temporary JPEG files for downstream OCR. */
    fun renderToFiles(context: Context, uri: Uri, directory: File): List<Uri> {
        require(directory.mkdirs() || directory.isDirectory) { "Não foi possível criar o cache do PDF" }
        val files = mutableListOf<Uri>()
        renderAll(context, uri) { page, bitmap ->
            val file = File(directory, "page-${page + 1}.jpg")
            FileOutputStream(file).use { output ->
                check(bitmap.compress(Bitmap.CompressFormat.JPEG, 92, output)) {
                    "Não foi possível salvar a página ${page + 1}"
                }
            }
            files += Uri.fromFile(file)
        }
        return files
    }

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
                        try {
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            onPage(index, bitmap)
                        } finally {
                            bitmap.recycle()
                        }
                    }
                }
            }
        }
    }
}
