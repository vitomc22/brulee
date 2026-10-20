package com.bruleetelie.app

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.bruleetelie.app.ui.screens.ProductionBoardScreen
import com.bruleetelie.app.ui.theme.BruleeTheme
import com.bruleetelie.domain.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProductionRegressionTest {
    @get:Rule val rule = createComposeRule()
    private fun order(id: String = "real", date: LocalDate = LocalDate.now(), status: ProductionStatus = ProductionStatus.Pending) = ProductionOrder(
        id, "q-$id", "#ORC-$id", "Cliente $id", "Bolo $id", date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " 16:00",
        listOf(ProductionStep("s1", "Preparar"), ProductionStep("s2", "Embalar")), status,
        RecipeCosts(Money(1000), Money(500), Money(100), Money(200)), salePrice = Money(3000)
    )
    @Test fun emptyBoardHasNoFakeOrdersAndNewQuoteWorks() {
        var clicked = false
        rule.setContent { BruleeTheme { ProductionBoardScreen(emptyList(), { _, _, _ -> }, {}, { clicked = true }) } }
        rule.onNodeWithTag("production_empty").assertIsDisplayed()
        listOf("Mariana Lima", "Roberto Silveira", "2 em andamento", "3 encomendas").forEach { rule.onNodeWithText(it).assertDoesNotExist() }
        rule.onNodeWithTag("production_new_quote").performClick()
        rule.runOnIdle { assertTrue(clicked) }
    }
    @Test fun dateFiltersReallyChangeOrdersAndExcludeCompleted() {
        val today = order("today"); val tomorrow = order("tomorrow", LocalDate.now().plusDays(1)); val later = order("later", LocalDate.now().plusDays(10)); val done = order("done", status = ProductionStatus.Completed)
        rule.setContent { BruleeTheme { ProductionBoardScreen(listOf(today, tomorrow, later, done), { _, _, _ -> }, {}, {}) } }
        rule.onNodeWithTag("production_order_done").assertDoesNotExist()
        rule.onNodeWithTag("production_filter_1").performClick()
        rule.onNodeWithTag("production_order_today").assertExists(); rule.onNodeWithTag("production_order_tomorrow").assertDoesNotExist()
        rule.onNodeWithTag("production_filter_2").performClick()
        rule.onNodeWithTag("production_order_tomorrow").assertExists(); rule.onNodeWithTag("production_order_today").assertDoesNotExist()
        rule.onNodeWithTag("production_filter_3").performClick()
        rule.onNodeWithTag("production_order_later").assertDoesNotExist()
        rule.onNodeWithTag("production_filter_4").performScrollTo().performClick()
        rule.onNodeWithTag("production_order_done").assertExists(); rule.onNodeWithTag("production_order_today").assertDoesNotExist()
    }
    @Test fun checklistEnablesFinalizationAndActualCostCanBeChanged() {
        var current by mutableStateOf(order())
        var finished: ProductionOrder? = null
        rule.setContent { BruleeTheme { ProductionBoardScreen(listOf(current), { _, stepId, checked -> current = current.copy(steps = current.steps.map { if (it.id == stepId) it.copy(isCompleted = checked) else it }) }, { finished = it }, {}) } }
        rule.onNodeWithTag("production_finalize_real").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("production_step_real_s1").performScrollTo().performClick()
        rule.onNodeWithTag("production_finalize_real").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("production_step_real_s2").performScrollTo().performClick()
        rule.onNodeWithTag("production_finalize_real").performScrollTo().performClick()
        rule.onNodeWithTag("actual_cost_0").performTextReplacement("12,50")
        rule.onNodeWithTag("production_confirm_completion").performClick()
        rule.runOnIdle { assertEquals(1250L, finished!!.actualCosts!!.ingredients.cents); assertEquals(1000L, finished!!.estimatedCosts.ingredients.cents) }
    }
    @Test fun invalidActualCostBlocksCompletionAndCancelDoesNotSell() {
        val order = order().let { it.copy(steps = it.steps.map { step -> step.copy(isCompleted = true) }) }
        var finished = false
        rule.setContent { BruleeTheme { ProductionBoardScreen(listOf(order), { _, _, _ -> }, { finished = true }, {}) } }
        rule.onNodeWithTag("production_finalize_real").performScrollTo().performClick()
        rule.onNodeWithTag("actual_cost_0").performTextReplacement("-1")
        rule.onNodeWithTag("production_confirm_completion").assertIsNotEnabled()
        rule.onNodeWithText("Cancelar").performClick()
        rule.runOnIdle { assertFalse(finished) }
    }
    @Test fun completedOrderCannotBeFinalizedAgain() {
        rule.setContent { BruleeTheme { ProductionBoardScreen(listOf(order(status = ProductionStatus.Completed)), { _, _, _ -> }, {}, {}) } }
        rule.onNodeWithTag("production_filter_4").performScrollTo().performClick()
        rule.onNodeWithTag("production_finalize_real").assertDoesNotExist()
        rule.onNodeWithTag("production_step_real_s1").performScrollTo().assertIsNotEnabled()
    }
}
