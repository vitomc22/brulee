package com.bruleetelie.app.documents

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

data class ScanResult(val imageUris: List<Uri>, val pdfUri: Uri?)

interface DocumentScannerGateway {
    fun createScanIntent(activity: Activity): Task<android.content.IntentSender>
    fun parseResult(data: Intent?): ScanResult
}

class MlKitDocumentScannerGateway : DocumentScannerGateway {
    private val options = GmsDocumentScannerOptions.Builder()
        .setGalleryImportAllowed(true)
        .setPageLimit(10)
        .setResultFormats(
            GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
            GmsDocumentScannerOptions.RESULT_FORMAT_PDF
        )
        .build()

    override fun createScanIntent(activity: Activity): Task<android.content.IntentSender> =
        GmsDocumentScanning.getClient(options).getStartScanIntent(activity)

    override fun parseResult(data: Intent?): ScanResult {
        val result = GmsDocumentScanningResult.fromActivityResultIntent(data)
        return ScanResult(
            imageUris = result?.pages?.mapNotNull { it.imageUri } ?: emptyList(),
            pdfUri = result?.pdf?.uri
        )
    }
}
