package com.bruleetelie.app

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class OcrIgnoreItemE2eTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun ignoreDetectedItem_excludesItFromSavedPriceObservations() {
        composeRule.onNodeWithTag("nav_stock").performClick()
        composeRule.onNodeWithTag("ocr_ignore_2")
            .performScrollTo()
            .performClick()

        composeRule.onAllNodesWithText("LEITE CONDENS MOCA TP 395G").assertCountEquals(0)

        listOf("3", "4").forEach { itemId ->
            composeRule.onNodeWithTag("ocr_confirm_$itemId")
                .performScrollTo()
                .performClick()
        }
        composeRule.onNodeWithTag("ocr_save_confirmed")
            .performScrollTo()
            .performClick()

        composeRule.onNodeWithText("Production Board").assertIsDisplayed()
        composeRule.onNodeWithText("3 observações de preço salvas").assertIsDisplayed()
    }
}
