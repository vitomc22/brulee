package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class OcrCaptureE2eTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @org.junit.After fun removeTestData() { clearTestData(composeRule) }

    @org.junit.Before fun prepareRealTestData() { clearTestData(composeRule) }

    @Test
    fun processRecognizedReceiptText_opensReviewWithParsedItems() {
        openOcrFixture(composeRule)

        composeRule.onNodeWithText("Importar Nota Fiscal").assertIsDisplayed()
        composeRule.onNodeWithTag("ocr_process_document")
            .performScrollTo()
            .performClick()

        composeRule.onNodeWithText("Revisão Obrigatória").assertIsDisplayed()
        composeRule.onNodeWithText("Leite Condensado 395g")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithText("Quantidade: 395 g")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun realDocumentScannerAction_isAvailableBeforeManualFallback() {
        openOcrFixture(composeRule)

        composeRule.onNodeWithTag("ocr_scan_document")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithTag("ocr_process_document")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
