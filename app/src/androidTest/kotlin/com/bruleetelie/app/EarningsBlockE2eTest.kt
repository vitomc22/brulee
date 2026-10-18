package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class EarningsBlockE2eTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun earn001To006_dashboardShowsRevenueProfitAndHistoricalMetrics() {
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithTag("finance_dashboard").assertIsDisplayed()
        composeRule.onNodeWithTag("finance_revenue").assertIsDisplayed()
        composeRule.onNodeWithText("Lucro líquido").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Ticket médio").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Lucro/hora").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun earn007To017_financeSeparatesOperationalMetrics() {
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithText("FATURAMENTO TOTAL").assertIsDisplayed()
        composeRule.onNodeWithText("Lucro líquido").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("vendas concluídas no período", substring = true).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun earn018To021_financeFilterRemainsStableAfterPeriodChange() {
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithText("Últimos 30 dias").performClick()
        composeRule.onNodeWithTag("finance_dashboard").assertIsDisplayed()
        composeRule.onNodeWithText("Este mês (Outubro)").performClick()
        composeRule.onNodeWithTag("finance_dashboard").assertIsDisplayed()
    }

    @Test
    fun earn022To024_emptyAndProductionStatesRemainExplicit() {
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithTag("finance_dashboard").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_production").performClick()
        composeRule.onNodeWithText("Production Board").assertIsDisplayed()
    }
}
