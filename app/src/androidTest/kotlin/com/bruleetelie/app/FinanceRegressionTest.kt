package com.bruleetelie.app

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.bruleetelie.app.ui.screens.FinanceDashboardScreen
import com.bruleetelie.app.ui.theme.BruleeTheme
import com.bruleetelie.domain.*
import androidx.compose.ui.graphics.toPixelMap
import com.bruleetelie.app.ui.theme.*
import org.junit.Assert.assertTrue
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

    @OptIn(ExperimentalTestApi::class)
    @Test fun aSingleSaleOf9111DrawsBothRevenueAndProfitBars() {
        val sale = SaleSnapshot("single", "o", "Q", "Cliente", "Bolo de chocolate", Money(9111), Money(5000), Money(5000), Money(4111), BigDecimal("45.12"), "Hoje")
        rule.setContent { BruleeTheme { FinanceDashboardScreen(listOf(sale), emptyList(), emptyList(), emptyMap(), onExportCsv = {}) } }
        rule.onNodeWithTag("finance_revenue").assertTextEquals("R$\u00a091,11")
        val chart = rule.onNodeWithTag("finance_bars_single").performScrollTo().assertIsDisplayed()
        chart.assertContentDescriptionEquals("Bolo de chocolate: receita R$\u00a091,11, lucro R$\u00a041,11")
        val pixels = chart.captureToImage().toPixelMap()
        var green = 0; var caramel = 0
        for (y in 0 until pixels.height step 2) for (x in 0 until pixels.width step 2) {
            val color = pixels[x, y]
            if (kotlin.math.abs(color.red - BruleePrimary.red) < .02f && kotlin.math.abs(color.green - BruleePrimary.green) < .02f) green++
            if (kotlin.math.abs(color.red - BruleeSecondary.red) < .02f && kotlin.math.abs(color.green - BruleeSecondary.green) < .02f) caramel++
        }
        assertTrue("Revenue must paint an actual visible bar", green > 100)
        assertTrue("Profit must paint an actual visible bar", caramel > 100)
        assertTrue("41.11 profit bar must be shorter than 91.11 revenue", green > caramel * 2)
    }
    @Test fun chartDisplaysLossesAndReturnsToEmptyWhenSaleLeavesTheSelectedPeriod() {
        val sale = SaleSnapshot("loss", "o", "Q", "Cliente", "Bolo", Money(9111), Money(10000), Money(10000), Money(-889), BigDecimal("-9.76"), "Hoje")
        var sales by mutableStateOf(listOf(sale))
        rule.setContent { BruleeTheme { FinanceDashboardScreen(sales, emptyList(), emptyList(), emptyMap(), onExportCsv = {}) } }
        rule.onNodeWithTag("finance_bars_loss").performScrollTo().assertContentDescriptionEquals("Bolo: receita R$\u00a091,11, lucro -R$\u00a08,89")
        rule.runOnIdle { sales = listOf(sale.copy(completedAtMillis = 1L)) }
        rule.onNodeWithTag("finance_bars_loss").assertDoesNotExist()
        rule.onNodeWithTag("finance_revenue").performScrollTo().assertTextEquals("R$\u00a00,00")
    }

}
