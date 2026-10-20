package com.bruleetelie.documents

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruleetelie.documents.ocr.MlKitTextRecognitionGateway
import java.io.File
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OcrStressTest {
    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    @get:org.junit.Rule val fixtures = org.junit.rules.TemporaryFolder(context.cacheDir)
    private val gateway = MlKitTextRecognitionGateway()

    @Test
    fun ocr50SequentialRuns_completesWithoutLeakOrFailure() = runBlocking {
        val fixture = createFixture("sequential", 1400, 1800)
        try {
            repeat(50) { iteration ->
                val result = withTimeout(30_000) { gateway.recognize(context, android.net.Uri.fromFile(fixture), iteration) }
                assertTrue("iteration $iteration did not recognize marker", result.text.contains("BRULEE TEST"))
                assertTrue(result.lines.all { it.page == iteration })
            }
        } finally {
            fixture.delete()
        }
    }

    @Test
    fun ocr20LargeImages_completesWithoutOom() = runBlocking {
        repeat(20) { index ->
            val fixture = createFixture("large-$index", 3000, 4000)
            try {
                val result = withTimeout(45_000) { gateway.recognize(context, android.net.Uri.fromFile(fixture), index) }
                assertTrue(result.text.contains("BRULEE TEST"))
            } finally {
                fixture.delete()
            }
        }
    }

    @Test
    fun ocrRepeatedCancelRetry_doesNotLeaveStuckWork() = runBlocking {
        repeat(20) { index ->
            val fixture = createFixture("retry-$index", 1200, 1600)
            try {
                val cancelled: Job = launch {
                    runCatching { gateway.recognize(context, android.net.Uri.fromFile(fixture), index) }
                }
                delay(10)
                cancelled.cancel()
                cancelled.join()
                val retry = withTimeout(30_000) { gateway.recognize(context, android.net.Uri.fromFile(fixture), index) }
                assertTrue(retry.text.contains("BRULEE TEST"))
            } finally {
                fixture.delete()
            }
        }
    }

    private fun createFixture(name: String, width: Int, height: Int): File {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(bitmap).apply {
            drawColor(Color.WHITE)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.BLACK
                textSize = (width / 18).toFloat()
            }
            drawText("BRULEE TEST", 80f, 180f, paint)
            drawText("ITEM CHOCOLATE", 80f, 360f, paint)
            drawText("TOTAL 123,45", 80f, 540f, paint)
        }
        return File(fixtures.root, "ocr-$name.png").also { file ->
            file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
            bitmap.recycle()
        }
    }
}
