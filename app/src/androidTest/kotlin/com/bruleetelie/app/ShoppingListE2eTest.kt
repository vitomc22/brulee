package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue

class ShoppingListE2eTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @org.junit.After fun removeTestData() { clearTestData(composeRule) }

    @org.junit.Before fun prepareRealTestData() { clearTestData(composeRule) }

    @Test
    fun addCompleteAndRemoveShoppingItem() {
        composeRule.onNodeWithTag("nav_shopping").performClick()
        composeRule.onNodeWithTag("shopping_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("shopping_name").performTextInput("Chocolate teste E2E")
        composeRule.onNodeWithTag("shopping_quantity").performTextInput("2 kg")
        composeRule.onNodeWithTag("shopping_add").performClick()
        composeRule.onNodeWithText("Chocolate teste E2E").assertIsDisplayed()
        composeRule.onNodeWithTag("shopping_pending_count").assertTextEquals("1 item(ns) pendente(s)")

        composeRule.onNodeWithTag("shopping_check_0").performClick()
        composeRule.onNodeWithTag("shopping_pending_count").assertTextEquals("Tudo comprado por aqui ✨")

        composeRule.onNodeWithTag("shopping_delete_0").performClick()
        assertTrue(composeRule.onAllNodesWithText("Chocolate teste E2E").fetchSemanticsNodes().isEmpty())
    }
}
