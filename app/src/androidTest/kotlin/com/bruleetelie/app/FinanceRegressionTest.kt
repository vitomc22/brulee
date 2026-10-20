package com.bruleetelie.app

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.bruleetelie.app.ui.screens.FinanceDashboardScreen
import com.bruleetelie.app.ui.theme.BruleeTheme
import com.bruleetelie.domain.*
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class FinanceRegressionTest {
    @get:Rule val rule = createComposeRule()
    @Test fun freshAppFinanceAndPeriodFiltersWorkWithNoSales() {
        rule.setContent { BruleeTheme { FinanceDashboardScreen(emptyList(), emptyList(), emptyList(), emptyMap(), onExportCsv = {}) } }
        rule.onNodeWithTag("finance_revenue").assertTextEquals("R$\u00a00,00")
        rule.onNodeWithTag("finance_filter_30_days").performScrollTo().performClick()
        rule.onNodeWithTag("finance_revenue").assertTextEquals("R$\u00a00,00")
        rule.onNodeWithTag("finance_empty_state").performScrollTo().assertIsDisplayed()
    }
    @Test fun clearingSalesWhileDashboardIsVisibleRestoresEmptyState() {
        var sales by mutableStateOf(listOf(SaleSnapshot("s", "o", "Q1", "Cliente", "Bolo", Money(5000), Money(2000), Money(2000), Money(3000), BigDecimal(60), "Hoje")))
        rule.setContent { BruleeTheme { FinanceDashboardScreen(sales, emptyList(), emptyList(), emptyMap(), onExportCsv = {}) } }
        rule.onNodeWithTag("finance_revenue").assertTextEquals("R$\u00a050,00")
        rule.runOnIdle { sales = emptyList() }
        rule.onNodeWithTag("finance_revenue").assertTextEquals("R$\u00a00,00")
        rule.onNodeWithTag("finance_empty_state").performScrollTo().assertIsDisplayed()
    }
    @Test fun recipeRankingAggregatesRepeatedOrdersRatherThanRepeatingRecipeRows() {
        val sales = listOf(
            SaleSnapshot("s1", "o1", "Q1", "Cliente", "10x Bolo real", Money(10000), Money(6000), Money(6000), Money(4000), BigDecimal(40), "Hoje"),
            SaleSnapshot("s2", "o2", "Q2", "Cliente", "20x Bolo real", Money(30000), Money(24000), Money(24000), Money(6000), BigDecimal(20), "Hoje")
        )
        rule.setContent { BruleeTheme { FinanceDashboardScreen(sales, emptyList(), emptyList(), emptyMap(), onExportCsv = {}) } }
        rule.onAllNodesWithText("Bolo real").assertCountEquals(1)
        rule.onNodeWithText("Lucro acumulado: R$\u00a0100,00").performScrollTo().assertIsDisplayed()
        rule.onNodeWithText("25%").assertIsDisplayed()
    }

}
