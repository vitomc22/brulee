package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class OcrReviewE2eTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun reviewRequiresEveryOcrItemConfirmation_beforeReturningToProduction() {
        composeRule.onNodeWithTag("nav_stock").performClick()

        composeRule.onNodeWithText("Revisão Obrigatória").assertIsDisplayed()
        composeRule.onNodeWithTag("ocr_save_confirmed")
            .performScrollTo()
            .assertIsNotEnabled()

        listOf("2", "3", "4").forEach { itemId ->
            composeRule.onNodeWithTag("ocr_confirm_$itemId")
                .performScrollTo()
                .performClick()
        }

        composeRule.onNodeWithTag("ocr_save_confirmed")
            .performScrollTo()
            .assertIsEnabled()
            .performClick()

        composeRule.onNodeWithText("Production Board").assertIsDisplayed()
        composeRule.onNodeWithText("4 observações de preço salvas").assertIsDisplayed()
    }
}
