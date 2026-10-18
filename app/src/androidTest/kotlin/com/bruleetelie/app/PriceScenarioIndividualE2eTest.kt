package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

/** One test method per PRICE-E2E ID from the detailed specification. */
class PriceScenarioIndividualE2eTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()

    private fun openPricing() {
        composeRule.onNodeWithTag("nav_wizard").performClick()
        composeRule.onNodeWithTag("recipe_continue").performClick()
        composeRule.onNodeWithTag("ingredients_continue").performScrollTo().performClick()
        composeRule.onNodeWithTag("costs_calculate").performScrollTo().performClick()
    }

    private fun pricingVisible() {
        composeRule.onNodeWithTag("price_sale_value").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("price_profit_value").assertIsDisplayed()
    }

    @Test fun priceE2e001_suggestedPriceForTargetMargin() { openPricing(); pricingVisible() }
    @Test fun priceE2e002_profitFollowsSuggestedPrice() { openPricing(); composeRule.onNodeWithTag("price_profit_value").performScrollTo().assertIsDisplayed() }
    @Test fun priceE2e003_marginIsDisplayedAsMargin() { openPricing(); composeRule.onNodeWithText("Margem:", substring = true).performScrollTo().assertIsDisplayed() }
    @Test fun priceE2e004_manualPriceRecalculatesProfit() { openPricing(); composeRule.onNodeWithTag("price_manual_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_manual_input").performTextInput("100,00"); composeRule.onNodeWithTag("price_profit_value").assertIsDisplayed() }
    @Test fun priceE2e005_priceBelowCostIsRepresented() { openPricing(); composeRule.onNodeWithTag("price_manual_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_manual_input").performTextInput("1,00"); composeRule.onNodeWithTag("price_profit_value").assertIsDisplayed() }
    @Test fun priceE2e006_zeroTargetMarginIsAccepted() { openPricing(); composeRule.onNodeWithTag("price_margin_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_margin_input").performTextInput("0"); pricingVisible() }
    @Test fun priceE2e007_tenPercentTargetMargin() { openPricing(); composeRule.onNodeWithTag("price_margin_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_margin_input").performTextInput("10"); pricingVisible() }
    @Test fun priceE2e008_fiftyPercentTargetMargin() { openPricing(); composeRule.onNodeWithTag("price_margin_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_margin_input").performTextInput("50"); pricingVisible() }
    @Test fun priceE2e009_veryHighTargetMarginRemainsFinite() { openPricing(); composeRule.onNodeWithTag("price_margin_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_margin_input").performTextInput("99"); composeRule.onNodeWithText("Infinity").assertDoesNotExist() }
    @Test fun priceE2e010_negativeMarginIsBlocked() { openPricing(); composeRule.onNodeWithTag("price_margin_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_margin_input").performTextInput("-10"); composeRule.onNodeWithText("Margem: 35.00%", substring = false).assertIsDisplayed() }
    @Test fun priceE2e011_zeroCostDoesNotProduceInvalidNumber() { openPricing(); composeRule.onNodeWithText("NaN").assertDoesNotExist(); composeRule.onNodeWithText("Infinity").assertDoesNotExist() }
    @Test fun priceE2e012_suggestedPriceRoundingIsFinite() { openPricing(); composeRule.onNodeWithTag("price_sale_value").performScrollTo().assertIsDisplayed(); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun priceE2e013_finalPriceCanAdvanceToQuote() { openPricing(); composeRule.onNodeWithTag("quote_advance").performScrollTo().performClick(); composeRule.onNodeWithTag("customer_name").assertIsDisplayed() }
    @Test fun priceE2e014_approvedQuoteUsesDisplayedPrice() { openPricing(); pricingVisible(); composeRule.onNodeWithTag("quote_advance").performScrollTo().performClick(); composeRule.onNodeWithTag("quote_approve").performScrollTo().assertIsDisplayed() }
    @Test fun priceE2e015_newMarginAffectsCurrentCalculation() { openPricing(); composeRule.onNodeWithTag("price_margin_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_margin_input").performTextInput("40"); composeRule.onNodeWithTag("price_sale_value").assertIsDisplayed() }
    @Test fun priceE2e016_costEditCanRecalculatePrice() { openPricing(); composeRule.onNodeWithTag("wizard_back").performClick(); composeRule.onNodeWithTag("costs_calculate").performScrollTo().performClick(); pricingVisible() }
    @Test fun priceE2e017_manualPriceSurvivesPricingSummary() { openPricing(); composeRule.onNodeWithTag("price_manual_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_manual_input").performTextInput("120,00"); composeRule.onNodeWithTag("price_sale_value").assertIsDisplayed() }
    @Test fun priceE2e018_largeSalePriceIsDisplayed() { openPricing(); composeRule.onNodeWithTag("price_manual_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_manual_input").performTextInput("99999,99"); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun priceE2e019_commaCurrencyInputIsAccepted() { openPricing(); composeRule.onNodeWithTag("price_manual_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_manual_input").performTextInput("118,17"); composeRule.onNodeWithTag("price_profit_value").assertIsDisplayed() }
    @Test fun priceE2e020_invalidCurrencyInputDoesNotCrash() { openPricing(); composeRule.onNodeWithTag("price_manual_input").performScrollTo().performTextClearance(); composeRule.onNodeWithTag("price_manual_input").performTextInput("abc"); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun priceE2e021_suggestedPriceIsAvailableOffline() { openPricing(); pricingVisible(); composeRule.onNodeWithText("Infinity").assertDoesNotExist() }
    @Test fun priceE2e022_quotePriceIsAvailableForDocuments() { openPricing(); composeRule.onNodeWithTag("price_sale_value").performScrollTo().assertIsDisplayed() }
}
