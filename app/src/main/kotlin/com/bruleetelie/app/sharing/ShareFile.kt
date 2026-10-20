package com.bruleetelie.app.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ShareFile {
    fun shareUri(context: Context, uri: Uri, mimeType: String, title: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, title).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun openWhatsApp(context: Context, url: String) {
        context.startActivity(whatsAppIntent(url).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun whatsAppIntent(url: String): Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        .addCategory(Intent.CATEGORY_BROWSABLE)

    fun uriForFile(context: Context, fileName: String): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", context.getFileStreamPath(fileName))

    fun uriForFile(context: Context, file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
