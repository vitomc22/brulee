package com.bruleetelie.app

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import com.bruleetelie.app.ui.screens.*
import com.bruleetelie.app.ui.components.*
import com.bruleetelie.app.ui.theme.BruleeTheme
import com.bruleetelie.domain.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Locale

class QuoteUiRegressionTest {
    @get:Rule val rule = createComposeRule()
    @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
    private fun calendarDay(date: LocalDate): SemanticsMatcher {
        val description = androidx.compose.material3.DatePickerDefaults.dateFormatter().formatDate(
            date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(), Locale.getDefault(), forContentDescription = true)!!
        return hasText(description, substring = true) and SemanticsMatcher.expectValue(
            androidx.compose.ui.semantics.SemanticsProperties.Role, androidx.compose.ui.semantics.Role.Button)
    }
    @Test fun calendarSelectsBrazilianDateRejectsPastDatesAndSurvivesRestoration() {
        var date by mutableStateOf("")
        val restoration = StateRestorationTester(rule)
        restoration.setContent { BruleeTheme { DeliveryDateField(date) { date = it } } }
        rule.onNodeWithTag("delivery_date_picker").performClick()
        rule.onNodeWithTag("delivery_date_confirm").assertIsNotEnabled()
        val today = LocalDate.now()
        if (today.dayOfMonth > 1) rule.onNode(calendarDay(today.minusDays(1))).assertIsNotEnabled()
        rule.onNode(calendarDay(today)).performClick()
        restoration.emulateSavedInstanceStateRestore()
        rule.onNodeWithTag("delivery_date_calendar").assertExists()
        rule.onNodeWithTag("delivery_date_confirm").performClick()
        rule.runOnIdle { assertEquals(today.format(DeliveryDateFormat), date) }
        rule.onNodeWithTag("delivery_date_picker").performClick()
        rule.onNodeWithTag("delivery_date_cancel").performClick()
        rule.runOnIdle { assertEquals(today.format(DeliveryDateFormat), date) }
        rule.onNodeWithTag("delivery_date").performTextReplacement("01/01/2000")
        rule.onNodeWithText("Escolha hoje ou uma data futura válida").assertExists()
    }
    @Test fun lazyOrdersListSupportsManyRecordsAndShowsLifecycleStatus() {
        val quotes = List(120) { i -> Quote("q$i", "#ORC-$i", Customer("c$i", "Cliente $i", ""), "Bolo", "", 12,
            PricingEngine.fromSalePrice(RecipeCosts(Money(2000), Money.Zero, Money.Zero, Money.Zero), Money(5000)), "25/10/2026",
            status = if (i == 119) QuoteStatus.ConvertedToSale else QuoteStatus.Draft) }
        var continued = ""
        rule.setContent { BruleeTheme { QuotesScreen(quotes, {}, {}, {}, {}, {}, onContinueDraft = { continued = it.id }) } }
        rule.onNodeWithTag("quote_continue_q0").performClick()
        rule.runOnIdle { assertEquals("q0", continued) }
        rule.onNodeWithTag("quotes_all").performClick()
        rule.onNodeWithTag("quotes_screen").performScrollToNode(hasTestTag("quote_card_q119"))
        rule.onNodeWithText("Concluído").assertExists()
        rule.onNodeWithTag("quote_continue_q119").assertDoesNotExist()
        rule.onNodeWithTag("quotes_screen").performScrollToNode(hasTestTag("quote_card_q60"))
        rule.onNodeWithTag("quote_continue_q60").assertExists()
    }
    @Test fun emptyListAndOfficialBrandAreAccessibleWithReducedMotion() {
        rule.setContent { BruleeTheme { CompositionLocalProvider(LocalReducedMotion provides true) {
            androidx.compose.foundation.layout.Column {
                BruleeLogo(Modifier.size(44.dp))
                QuotesScreen(emptyList(), {}, {}, {}, {}, {})
            }
        } } }
        rule.onNodeWithContentDescription("Brûlée Ateliê de Doces").assertExists()
        rule.onNodeWithTag("quotes_empty").assertExists()
        rule.onNodeWithTag("brulee_mascot").assert(SemanticsMatcher.expectValue(androidx.compose.ui.semantics.SemanticsProperties.StateDescription, "Movimento reduzido"))
    }

    @Test fun ordersDistinguishApprovalFromActiveProductionAndCompletion() {
        val costs = RecipeCosts(Money(2000), Money.Zero, Money.Zero, Money.Zero)
        val quote = Quote("q", "#ORC-Q", Customer("c", "Cliente", ""), "Bolo", "", 12,
            PricingEngine.fromSalePrice(costs, Money(5000)), "25/10/2026", status = QuoteStatus.Approved)
        var order by mutableStateOf(ProductionOrder("o", quote.id, quote.code, quote.customer.name, quote.productName,
            quote.deliveryDate, emptyList(), estimatedCosts = costs, salePrice = quote.pricing.salePrice))
        var productionOpened = false
        rule.setContent { BruleeTheme {
            QuotesScreen(listOf(quote), {}, {}, {}, {}, {}, orders = listOf(order), onProduction = { productionOpened = true })
        } }
        rule.onNodeWithTag("quotes_all").performClick()
        rule.onNodeWithText("Aprovado").assertExists()
        rule.onNodeWithTag("quote_production_q").performClick()
        rule.runOnIdle { assertTrue(productionOpened); order = order.copy(status = ProductionStatus.InProgress) }
        rule.onNodeWithText("Em produção").assertExists()
        rule.runOnIdle { order = order.copy(status = ProductionStatus.Completed) }
        rule.onNodeWithText("Concluído").assertExists()
        rule.runOnIdle { order = order.copy(status = ProductionStatus.Cancelled) }
        rule.onNodeWithText("Cancelado").assertExists()
    }
}
