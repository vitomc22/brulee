package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class QuoteFlowE2eTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun createRecipeWithIngredientAndCosts_thenApproveQuote_addsItToProduction() {
        composeRule.onNodeWithTag("nav_wizard").performClick()

        composeRule.onNodeWithTag("recipe_name")
            .performTextClearance()
        composeRule.onNodeWithTag("recipe_name")
            .performTextInput("Bolo E2E de Chocolate")
        composeRule.onNodeWithTag("recipe_continue").performClick()

        composeRule.onNodeWithTag("ingredient_name")
            .performTextInput("Chocolate E2E 70%")
        composeRule.onNodeWithTag("ingredient_package_quantity")
            .performTextInput("1000")
        composeRule.onNodeWithTag("ingredient_package_price")
            .performTextInput("80,00")
        composeRule.onNodeWithTag("ingredient_used_quantity")
            .performTextInput("500")
        composeRule.onNodeWithTag("ingredient_add")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithText("Chocolate E2E 70%")
            .performScrollTo()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("ingredients_continue")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("costs_calculate")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("quote_advance")
            .performScrollTo()
            .performClick()

        composeRule.onNodeWithTag("customer_name")
            .performTextClearance()
        composeRule.onNodeWithTag("customer_name")
            .performTextInput("Cliente E2E")
        composeRule.onNodeWithTag("quote_approve")
            .performScrollTo()
            .performClick()

        composeRule.onNodeWithText("Production Board").assertIsDisplayed()
        composeRule.onNodeWithText("30x Bolo E2E de Chocolate")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
