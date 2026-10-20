package com.bruleetelie.app

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.bruleetelie.data.db.BruleeDatabase
import com.bruleetelie.domain.QuoteStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuoteFlowE2eTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()
    private val db get() = BruleeDatabase.getInstance(composeRule.activity)
    @Before fun emptyDatabase() {
        clearTestData(composeRule)
        composeRule.onNodeWithTag("nav_production").performClick()
        composeRule.waitUntil(10000) { composeRule.onAllNodesWithTag("production_empty").fetchSemanticsNodes().isNotEmpty() }
    }
    @org.junit.After fun removeTestData() { clearTestData(composeRule) }
    private fun createQuote() {
        composeRule.onNodeWithTag("nav_wizard").performClick()
        composeRule.onNodeWithTag("recipe_name").performTextInput("Bolo E2E de Chocolate")
        composeRule.onNodeWithTag("recipe_yield").performTextReplacement("30")
        composeRule.onNodeWithTag("recipe_continue").performScrollTo().performClick()
        composeRule.onNodeWithTag("ingredient_name").performTextInput("Chocolate E2E 70%")
        composeRule.onNodeWithTag("ingredient_package_quantity").performTextInput("1000")
        composeRule.onNodeWithTag("ingredient_package_price").performTextInput("80,00")
        composeRule.onNodeWithTag("ingredient_used_quantity").performTextInput("500")
        composeRule.onNodeWithTag("ingredient_add").performScrollTo().performClick()
        composeRule.onNodeWithTag("ingredients_continue").performScrollTo().performClick()
        composeRule.onNodeWithTag("operational_prep_minutes").performTextReplacement("30")
        composeRule.onNodeWithTag("operational_cook_minutes").performTextReplacement("20")
        composeRule.onNodeWithTag("operational_packaging").performScrollTo().performTextReplacement("5,00")
        composeRule.onNodeWithTag("operational_overhead").performScrollTo().performTextReplacement("2,00")
        composeRule.onNodeWithTag("costs_calculate").performScrollTo().performClick()
        composeRule.onNodeWithTag("price_manual_input").performScrollTo().performTextReplacement("200,00")
        composeRule.onNodeWithTag("quote_advance").performScrollTo().performClick()
        composeRule.onNodeWithTag("customer_name").performTextInput("Cliente E2E")
        composeRule.onNodeWithTag("customer_phone").performTextInput("11987654321")
        composeRule.onNodeWithTag("delivery_date").performTextInput("21/10/2026 16:00")
    }
    @Test fun createRecipeWithCostsApproveProduceAndRecordActualCostsAndRevenue() {
        createQuote()
        composeRule.onNodeWithTag("quote_approve").performScrollTo().performClick()
        composeRule.onNodeWithTag("production_board").assertIsDisplayed()
        composeRule.onNodeWithText("30x Bolo E2E de Chocolate").performScrollTo().assertIsDisplayed()
        val order = runBlocking { withTimeout(10000) { db.productionOrderDao().getAllOrders().first { it.isNotEmpty() }.single() } }
        val steps = runBlocking { db.productionOrderDao().getStepsForOrderSync(order.id) }
        composeRule.onNodeWithTag("production_finalize_${order.id}").performScrollTo().assertIsNotEnabled()
        steps.forEach { composeRule.onNodeWithTag("production_step_${order.id}_${it.id}").performScrollTo().performClick() }
        composeRule.onNodeWithTag("production_finalize_${order.id}").performScrollTo().performClick()
        composeRule.onNodeWithTag("actual_minutes").performTextReplacement("90")
        composeRule.onNodeWithTag("actual_cost_0").performTextReplacement("35,00")
        composeRule.onNodeWithTag("production_confirm_completion").performClick()
        composeRule.onNodeWithTag("completion_continue").performScrollTo().performClick()
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithTag("finance_revenue").assertTextEquals("R$\u00a0200,00")
        val sales = runBlocking { withTimeout(10000) { db.saleSnapshotDao().getAllSales().first { it.isNotEmpty() } } }
        assertEquals(1, sales.size)
        assertEquals(90, sales.single().activeProductionMinutes)
        assertEquals(500L, sales.single().estimatedCostCents - sales.single().actualCostCents)
        assertEquals(QuoteStatus.ConvertedToSale.name, runBlocking { db.quoteDao().getAllQuotes().first().single().status })
    }
    @Test fun WhatsAppActuallyLaunchesCustomerIntentAndSavedQuoteCanBeApprovedLater() {
        createQuote()
        var captured: Intent? = null
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val monitor = object : Instrumentation.ActivityMonitor() {
            override fun onStartActivity(intent: Intent): Instrumentation.ActivityResult? {
                if (intent.action == Intent.ACTION_VIEW && intent.data?.host == "api.whatsapp.com") {
                    captured = intent
                    return Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)
                }
                return null
            }
        }
        instrumentation.addMonitor(monitor)
        try {
            composeRule.onNodeWithTag("quote_send_whatsapp").performScrollTo().performClick()
            composeRule.onNodeWithText("Orçamento pronto no WhatsApp").assertIsDisplayed()
            assertEquals("5511987654321", captured!!.data!!.getQueryParameter("phone"))
            assertTrue(captured!!.data!!.getQueryParameter("text")!!.contains("Cliente E2E"))
        } finally { instrumentation.removeMonitor(monitor) }
        composeRule.onNodeWithTag("quote_sent_finish").performScrollTo().performClick()
        composeRule.onNodeWithTag("quotes_screen").assertIsDisplayed()
        val quote = runBlocking { withTimeout(10000) { db.quoteDao().getAllQuotes().first { it.isNotEmpty() }.single() } }
        assertEquals(QuoteStatus.Sent.name, quote.status)
        composeRule.onNodeWithTag("quote_approve_${quote.id}").performScrollTo().performClick()
        composeRule.onNodeWithTag("production_order_order-${quote.id}").performScrollTo().assertIsDisplayed()
    }
}
