package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class CanonicalWizardCostE2eTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCanonicalCostWizardFlow() {
        // Open new quote wizard
        composeRule.onNodeWithTag("nav_wizard").performClick()

        // Step 1: Recipe name
        composeRule.onNodeWithTag("recipe_name")
            .performTextClearance()
        composeRule.onNodeWithTag("recipe_name")
            .performTextInput("Bolo Canônico E2E")
        composeRule.onNodeWithTag("recipe_continue").performClick()

        // Step 2: Add ingredients (Chocolate, Creme, Açúcar)
        // Chocolate: 2000g, R$ 80.00, used 350g
        composeRule.onNodeWithTag("ingredient_name")
            .performTextInput("Chocolate")
        composeRule.onNodeWithTag("ingredient_package_quantity")
            .performTextInput("2000")
        composeRule.onNodeWithTag("ingredient_package_price")
            .performTextInput("80,00")
        composeRule.onNodeWithTag("ingredient_used_quantity")
            .performTextInput("350")
        composeRule.onNodeWithTag("ingredient_add")
            .performScrollTo()
            .performClick()

        // Verify ingredient added
        composeRule.onNodeWithText("Chocolate")
            .performScrollTo()
            .assertIsDisplayed()

        // Continue to operational costs
        composeRule.onNodeWithTag("ingredients_continue")
            .performScrollTo()
            .performClick()

        // Step 3: Operational costs / times
        composeRule.onNodeWithTag("operational_prep_minutes").performTextClearance()
        composeRule.onNodeWithTag("operational_prep_minutes").performTextInput("45")
        composeRule.onNodeWithTag("operational_cook_minutes").performTextClearance()
        composeRule.onNodeWithTag("operational_cook_minutes").performTextInput("45")
        composeRule.onNodeWithTag("operational_decoration_minutes").performTextClearance()
        composeRule.onNodeWithTag("operational_decoration_minutes").performTextInput("30")
        composeRule.onNodeWithTag("energy_both")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithTag("costs_calculate")
            .performScrollTo()
            .performClick()

        // Step 4: Cost summary & pricing should show
        composeRule.onNodeWithTag("cost_summary_screen")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun wizardCanNavigateBackAndRemoveIngredient() {
        composeRule.onNodeWithTag("nav_wizard").performClick()
        composeRule.onNodeWithTag("recipe_continue").performClick()

        composeRule.onNodeWithTag("ingredient_name").performTextInput("Ingrediente removível")
        composeRule.onNodeWithTag("ingredient_package_quantity").performTextInput("100")
        composeRule.onNodeWithTag("ingredient_package_price").performTextInput("10,00")
        composeRule.onNodeWithTag("ingredient_used_quantity").performTextInput("10")
        composeRule.onNodeWithTag("ingredient_add").performClick()

        composeRule.onAllNodesWithTag("ingredient_remove")[3].performClick()
        composeRule.onNodeWithTag("ingredients_continue")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("operational_prep_minutes")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithTag("wizard_back").performClick()
        composeRule.onNodeWithTag("ingredient_name")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
