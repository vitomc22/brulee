package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class FinanceDashboardE2eTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun dashboardUsesPersistedFinancialData() {
        composeRule.onNodeWithTag("nav_finance").performClick()

        composeRule.onNodeWithTag("finance_dashboard").assertIsDisplayed()
        composeRule.onNodeWithTag("finance_revenue")
            .assertIsDisplayed()
            .assertTextEquals("R$\u00A0288,00")
        composeRule.onNodeWithText("Ticket médio").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Conversão").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Lucro/hora").performScrollTo().assertIsDisplayed()
    }
}
