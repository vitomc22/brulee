package com.bruleetelie.documents.ocr

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class OcrLine(
    val text: String,
    val page: Int = 0,
    val boundingBox: String? = null
)

data class OcrResult(val lines: List<OcrLine>) {
    val text: String get() = lines.joinToString("\n") { it.text }
}

interface TextRecognitionGateway {
    suspend fun recognize(context: Context, imageUri: Uri, page: Int = 0): OcrResult
}

class MlKitTextRecognitionGateway : TextRecognitionGateway {
    override suspend fun recognize(context: Context, imageUri: Uri, page: Int): OcrResult {
        val image = InputImage.fromFilePath(context, imageUri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        return suspendCancellableCoroutine { continuation ->
            recognizer.process(image)
                .addOnSuccessListener { result ->
                    val lines = result.textBlocks.flatMap { block ->
                        block.lines.map { line ->
                            OcrLine(line.text, page, line.boundingBox?.toShortString())
                        }
                    }
                    continuation.resume(OcrResult(lines))
                }
                .addOnFailureListener { error -> continuation.resumeWithException(error) }
            continuation.invokeOnCancellation { recognizer.close() }
        }
    }
}
