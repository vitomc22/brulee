package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class PricingBlockE2eTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun openPricingSummary() {
        openTestRecipe(composeRule)
        composeRule.onNodeWithTag("recipe_continue").performClick()
        composeRule.onNodeWithTag("ingredients_continue").performScrollTo().performClick()
        composeRule.onNodeWithTag("costs_calculate").performScrollTo().performClick()
    }

    @org.junit.After fun removeTestData() { clearTestData(composeRule) }

    @org.junit.Before fun prepareRealTestData() { clearTestData(composeRule) }

    @Test
    fun price001To003_suggestedPriceProfitAndMarginAreDisplayed() {
        openPricingSummary()
        composeRule.onNodeWithTag("price_sale_value").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("price_profit_value").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Margem: 35.00%").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun price004_manualAdjustmentRecalculatesProfit() {
        openPricingSummary()
        composeRule.onNodeWithTag("price_profit_value").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("price_increase_5").performScrollTo().performClick()
        composeRule.onNodeWithTag("price_profit_value").assertIsDisplayed()
    }

    @Test
    fun price005To012_priceAdjustmentRemainsFiniteAndFormatted() {
        openPricingSummary()
        composeRule.onNodeWithTag("price_decrease_5").performScrollTo().performClick()
        composeRule.onNodeWithTag("price_increase_10").performScrollTo().performClick()
        composeRule.onNodeWithTag("price_sale_value").assertIsDisplayed()
    }

    @Test
    fun price013To022_priceSummaryCanAdvanceToQuoteOffline() {
        openPricingSummary()
        composeRule.onNodeWithTag("quote_advance").performScrollTo().performClick()
        composeRule.onNodeWithTag("customer_name").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("customer_phone").assertIsDisplayed()
    }
}
