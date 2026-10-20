package com.bruleetelie.app

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.bruleetelie.app.ui.screens.ProductionCompletionScreen
import com.bruleetelie.app.ui.theme.BruleeTheme
import com.bruleetelie.domain.*
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CompletionRegressionTest {
    @get:Rule val rule = createComposeRule()
    @Test fun completionShowsBusinessResultsAndContainsNoGameMechanics() {
        val comparison = PricingEngine.compareEstimatedVsActual(RecipeCosts(Money(6000), Money.Zero, Money.Zero, Money.Zero), RecipeCosts(Money(5000), Money.Zero, Money.Zero, Money.Zero), Money(9111))
        var continued = false
        rule.setContent { BruleeTheme { ProductionCompletionScreen("Bolo do cliente", comparison, reducedMotion = true, onContinue = { continued = true }) } }
        rule.onNodeWithTag("completion_estimated_cost").assertTextEquals("R$\u00a060,00")
        rule.onNodeWithTag("completion_actual_cost").assertTextEquals("R$\u00a050,00")
        rule.onNodeWithTag("completion_sale_price").performScrollTo().assertTextEquals("R$\u00a091,11")
        rule.onNodeWithTag("completion_profit").assertTextEquals("R$\u00a041,11")
        listOf("XP", "OFENSIVA", "Streak", "Nv.", "BÔNUS").forEach { rule.onNodeWithText(it, substring = true).assertDoesNotExist() }
        rule.onNodeWithTag("completion_continue").performScrollTo().performClick()
        rule.runOnIdle { assertTrue(continued) }
    }
    @Test fun completionRetainsNegativeProfitAndHigherActualCosts() {
        val comparison = PricingEngine.compareEstimatedVsActual(RecipeCosts(Money(6000), Money.Zero, Money.Zero, Money.Zero), RecipeCosts(Money(10000), Money.Zero, Money.Zero, Money.Zero), Money(9111))
        rule.setContent { BruleeTheme { ProductionCompletionScreen("Bolo", comparison, reducedMotion = true, onContinue = {}) } }
        rule.onNodeWithTag("completion_profit").performScrollTo().assertTextEquals("-R$\u00a08,89")
        rule.onNodeWithText("Custo acima do previsto: R$\u00a040,00").performScrollTo().assertIsDisplayed()
    }
}
