package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class OcrItemEditingE2eTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun editDetectedQuantityAndPrice_updatesItemBeforeConfirmation() {
        composeRule.onNodeWithTag("nav_stock").performClick()
        composeRule.onNodeWithTag("ocr_edit_2")
            .performScrollTo()
            .performClick()

        composeRule.onNodeWithTag("ocr_edit_quantity_2")
            .performTextClearance()
        composeRule.onNodeWithTag("ocr_edit_quantity_2")
            .performTextInput("500")
        composeRule.onNodeWithTag("ocr_edit_price_2")
            .performTextClearance()
        composeRule.onNodeWithTag("ocr_edit_price_2")
            .performTextInput("8,99")
        composeRule.onNodeWithTag("ocr_save_edit_2")
            .performScrollTo()
            .performClick()

        composeRule.onNodeWithTag("ocr_item_2")
            .performScrollTo()
            .assert(hasAnyDescendant(hasText("Quantidade: 500 g")))
            .assert(hasAnyDescendant(hasText("R$ 8,99")))
    }
}
