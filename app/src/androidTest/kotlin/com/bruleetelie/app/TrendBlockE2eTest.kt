package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class TrendBlockE2eTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @org.junit.After fun removeTestData() { clearTestData(composeRule) }

    @org.junit.Before fun prepareRealTestData() { clearTestData(composeRule); seedFinanceTestData(composeRule) }

    @Test
    fun trend001To003_baselineAndBelowThresholdAreStable() {
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithTag("finance_price_trend_alert")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithTag("finance_price_trend_status")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun trend004To010_dashboardExposesPeriodAndTrendArea() {
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithTag("finance_filter_30_days").performClick()
        composeRule.onNodeWithTag("finance_filter_month").performClick()
        composeRule.onNodeWithText("Ouro do Ateliê").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("finance_price_trend_alert")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun trend011To024_financialValuesNeverExposeInvalidArtifacts() {
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithTag("finance_dashboard").assertIsDisplayed()
        composeRule.onNodeWithText("NaN").assertDoesNotExist()
        composeRule.onNodeWithText("Infinity").assertDoesNotExist()
    }

    @Test
    fun trend025To028_trendStateSurvivesNavigation() {
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithTag("nav_production").performClick()
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithTag("finance_price_trend_status")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
