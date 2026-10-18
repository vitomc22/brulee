package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class ProductionBlockE2eTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun dev001To010_productionBoardShowsOrderAndChecklist() {
        composeRule.onNodeWithTag("nav_production").performClick()
        composeRule.onNodeWithText("Production Board").assertIsDisplayed()
        composeRule.onAllNodesWithText("MICROETAPAS DA RECEITA")[0].performScrollTo().assertIsDisplayed()
    }

    @Test
    fun dev011To019_checklistSupportsAtomicStepProgress() {
        composeRule.onNodeWithTag("nav_production").performClick()
        composeRule.onNodeWithTag("production_step_ord-284_s1-284").performScrollTo().performClick()
        composeRule.onNodeWithTag("production_order_ord-284").assertIsDisplayed()
    }

    @Test
    fun dev020To024_productionCanBeReopenedFromNavigation() {
        composeRule.onNodeWithTag("nav_production").performClick()
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithTag("nav_production").performClick()
        composeRule.onNodeWithText("Production Board").assertIsDisplayed()
    }

    @Test
    fun dev025To028_productionStateDoesNotExposeInvalidValues() {
        composeRule.onNodeWithTag("nav_production").performClick()
        composeRule.onNodeWithText("NaN").assertDoesNotExist()
        composeRule.onNodeWithText("Infinity").assertDoesNotExist()
    }
}
