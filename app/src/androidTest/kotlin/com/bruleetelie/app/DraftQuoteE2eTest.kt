package com.bruleetelie.app

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import com.bruleetelie.app.ui.BruleeAppViewModel
import com.bruleetelie.data.db.BruleeDatabase
import com.bruleetelie.data.toDomain
import com.bruleetelie.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DraftQuoteE2eTest {
    @get:Rule val rule = createAndroidComposeRule<MainActivity>()
    private val repo get() = ViewModelProvider(rule.activity)[BruleeAppViewModel::class.java].repository
    @Before fun prepare() { clearTestData(rule) }
    @After fun cleanup() { clearTestData(rule) }
    private fun click(tag: String) { rule.onNodeWithTag(tag).performScrollTo().performClick() }
    private fun type(tag: String, text: String) { rule.onNodeWithTag(tag).performScrollTo().performTextReplacement(text) }
    private fun persistedQuote(): Quote = runBlocking {
        repo.awaitPendingWrites()
        withTimeout(10000) { BruleeDatabase.getInstance(rule.activity).quoteDao().getAllQuotes().first { it.size == 1 }.single().toDomain() }
    }
    private fun startRecipe() {
        rule.onNodeWithTag("nav_wizard").performClick()
        type("recipe_name", "Bolo de teste do rascunho")
        type("recipe_yield", "12")
        click("recipe_continue")
        type("ingredient_name", "Chocolate do teste")
        type("ingredient_package_quantity", "1000")
        type("ingredient_package_price", "80,00")
        type("ingredient_used_quantity", "250")
        click("ingredient_add")
    }
    @Test fun draftResumesEveryInputUpdatesSameRecordAndApprovesIntoProduction() {
        startRecipe()
        click("ingredients_continue")
        type("operational_prep_minutes", "45")
        type("operational_cook_minutes", "30")
        type("operational_decoration_minutes", "15")
        type("operational_packaging", "4,50")
        type("operational_overhead", "2,50")
        click("energy_both")
        click("costs_calculate")
        type("price_manual_input", "150,00")
        click("quote_advance")
        type("customer_name", "Cliente do teste de rascunho")
        type("customer_phone", "11988887777")
        val delivery = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        type("delivery_date", delivery)
        type("quote_notes", "Retirada com embalagem especial")
        type("quote_validity", "7")
        click("wizard_back")
        click("quote_advance")
        rule.onNodeWithTag("delivery_date").performScrollTo().assertTextContains(delivery)
        // Recreate the whole Activity with unsaved customer inputs.
        rule.activityRule.scenario.recreate()
        rule.onNodeWithTag("customer_name").performScrollTo().assertTextContains("Cliente do teste de rascunho")
        click("quote_save_draft")
        val original = persistedQuote()
        assertEquals(QuoteStatus.Draft, original.status)
        assertEquals(15000L, original.pricing.salePrice.cents)
        assertEquals(EnergySource.Both, original.draft!!.recipe.times.energySource)
        assertEquals(450L, original.draft!!.recipe.packaging.cents)
        runBlocking {
            repo.updateSettings(OperatingSettings(hourlyLaborRate = Money(5000), gasCostPerHour = Money(640)))
            repo.awaitPendingWrites()
        }
        // Recreate on the list, forcing all recipe inputs to come from persistence.
        rule.activityRule.scenario.recreate()
        click("quote_continue_${original.id}")
        rule.onNodeWithTag("customer_phone").performScrollTo().assertTextContains("11988887777")
        rule.onNodeWithTag("delivery_date").performScrollTo().assertTextContains(delivery)
        rule.onNodeWithTag("quote_notes").performScrollTo().assertTextContains(original.notes)
        rule.onNodeWithTag("quote_validity").performScrollTo().assertTextContains("7")
        click("wizard_back")
        rule.onNodeWithTag("price_sale_value").performScrollTo().assertTextEquals(original.pricing.salePrice.format())
        click("wizard_back")
        rule.onNodeWithTag("operational_prep_minutes").performScrollTo().assertTextContains("45")
        rule.onNodeWithTag("operational_packaging").performScrollTo().assertTextContains("4.50")
        click("wizard_back")
        click("ingredient_edit")
        rule.onNodeWithTag("ingredient_used_quantity").performScrollTo().assertTextContains("250")
        type("ingredient_used_quantity", "300")
        click("ingredient_add")
        click("wizard_back")
        rule.onNodeWithTag("recipe_yield").assertTextContains("12")
        click("recipe_continue")
        click("ingredients_continue")
        click("costs_calculate")
        click("quote_advance")
        type("customer_name", "Cliente atualizado")
        click("quote_save_draft")
        val updated = persistedQuote()
        assertEquals(original.id, updated.id); assertEquals(original.code, updated.code)
        assertEquals(300.toBigDecimal(), updated.draft!!.recipe.ingredients.single().usedQuantity.value)
        assertEquals(2400L, updated.pricing.costs.ingredients.cents)
        assertEquals(15000L, updated.pricing.salePrice.cents)
        assertEquals(original.draft!!.recipe.times, updated.draft!!.recipe.times)
        click("quote_continue_${updated.id}")
        rule.onNodeWithText("Rascunho salvo. Continue em Pedidos.").assertDoesNotExist()
        rule.onNodeWithTag("quote_approve").performScrollTo().assertIsEnabled()
        click("quote_approve")
        runBlocking { repo.awaitPendingWrites() }
        assertEquals(rule.onRoot().printToString(), QuoteStatus.Approved, persistedQuote().status)
        rule.waitUntil(10000) {
            rule.onAllNodesWithTag("production_order_order-${updated.id}").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("production_order_order-${updated.id}").performScrollTo().assertIsDisplayed()
        rule.onNodeWithTag("nav_quotes").performClick()
        click("quotes_all")
        rule.onNodeWithTag("quote_card_${updated.id}").assertExists()
        rule.onNodeWithTag("quote_continue_${updated.id}").assertDoesNotExist()
        // A new quote always receives fresh state after completing an edited draft.
        click("quotes_new")
        rule.onNodeWithTag("recipe_name").assertTextContains("")
        rule.onNodeWithTag("recipe_continue").assertIsNotEnabled()
    }
    @Test fun incompleteDraftCanBeSavedAndReturnsToItsSavedStep() {
        startRecipe()
        click("wizard_save_draft")
        val quote = persistedQuote()
        assertTrue(quote.customer.name.isEmpty()); assertEquals(1, quote.draft!!.step)
        click("quote_continue_${quote.id}")
        rule.onNodeWithText("Chocolate do teste").performScrollTo().assertIsDisplayed()
        click("wizard_save_draft")
        assertEquals(quote, persistedQuote())
    }
    @Test fun systemBackMovesToPreviousStepAndRetainsInputs() {
        startRecipe()
        androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().runOnMainSync {
            rule.activity.onBackPressedDispatcher.onBackPressed()
        }
        rule.onNodeWithTag("recipe_name").assertTextContains("Bolo de teste do rascunho")
        click("recipe_continue")
        rule.onNodeWithText("Chocolate do teste").performScrollTo().assertIsDisplayed()
    }
}
