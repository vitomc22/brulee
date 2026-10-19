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
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OcrLifecycleStressTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun reviewStateSurvivesActivityRecreationWithoutAutoPersisting() {
        openReviewAndEdit()
        recreateActivity()

        composeRule.onNodeWithText("Revisão Obrigatória").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("ocr_item_2")
            .performScrollTo()
            .assertIsDisplayed()
            .assert(hasAnyDescendant(hasText("Quantidade: 500 g")))
    }

    @Test
    fun reviewStateSurvivesBackgroundForegroundWithoutDuplicateCommit() {
        openReviewAndEdit()
        val scenario = composeRule.activityRule.scenario
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.moveToState(Lifecycle.State.RESUMED)
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Revisão Obrigatória").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("ocr_save_confirmed").performScrollTo().assertIsDisplayed()
    }

    private fun openReviewAndEdit() {
        composeRule.onNodeWithTag("nav_stock").performClick()
        composeRule.onNodeWithTag("ocr_process_document")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("ocr_edit_2")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("ocr_edit_quantity_2").performTextClearance()
        composeRule.onNodeWithTag("ocr_edit_quantity_2").performTextInput("500")
        composeRule.onNodeWithTag("ocr_save_edit_2")
            .performScrollTo()
            .performClick()
    }

    private fun recreateActivity() {
        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()
    }
}
