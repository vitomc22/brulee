package com.bruleetelie.app

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import com.bruleetelie.app.ui.screens.NewQuoteWizardScreen
import com.bruleetelie.app.ui.theme.BruleeTheme
import com.bruleetelie.app.ui.components.LocalReducedMotion
import com.bruleetelie.domain.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class WizardRegressionTest {
    @get:Rule val rule = createComposeRule()
    private var savedRecipe: SavedRecipe? = null
    private var savedQuote: Quote? = null
    private var sharedQuote: Quote? = null
    private var sentQuote: Quote? = null
    private fun content(reduced: Boolean = false) {
        rule.setContent { BruleeTheme { CompositionLocalProvider(LocalReducedMotion provides reduced) {
            NewQuoteWizardScreen(OperatingSettings(), { savedQuote = it }, { sentQuote = it }, { sharedQuote = it }, {}, onSaveRecipe = { savedRecipe = it }, onSaveQuote = { savedQuote = it })
        } } }
    }
    private fun name() {
        rule.onNodeWithTag("recipe_name").performTextInput("Bolo real")
        rule.onNodeWithTag("recipe_yield").performTextReplacement("8")
        rule.onNodeWithTag("recipe_continue").performScrollTo().performClick()
    }
    private fun ingredient() {
        rule.onNodeWithTag("ingredient_name").performTextInput("Farinha real")
        rule.onNodeWithTag("ingredient_package_quantity").performTextInput("1000")
        rule.onNodeWithTag("ingredient_package_price").performTextInput("10,00")
        rule.onNodeWithTag("ingredient_used_quantity").performTextInput("200")
        rule.onNodeWithTag("ingredient_add").performScrollTo().performClick()
    }
    private fun customer() {
        name(); ingredient()
        rule.onNodeWithTag("ingredients_continue").performScrollTo().performClick()
        rule.onNodeWithTag("costs_calculate").performScrollTo().performClick()
        rule.onNodeWithTag("quote_advance").performScrollTo().performClick()
        rule.onNodeWithTag("customer_name").performTextInput("Cliente real")
        rule.onNodeWithTag("delivery_date").performTextInput("21/10/2026 16:00")
    }
    @Test fun startsEmptyAndRequiresRecipeAndPositiveYield() {
        content()
        rule.onNodeWithTag("recipe_name").assertTextContains("")
        rule.onNodeWithTag("recipe_continue").assertIsNotEnabled()
        rule.onNodeWithTag("recipe_name").performTextInput("Bolo")
        rule.onNodeWithTag("recipe_yield").performTextReplacement("0")
        rule.onNodeWithTag("recipe_continue").assertIsNotEnabled()
        rule.onNodeWithTag("recipe_yield").performTextReplacement("5")
        rule.onNodeWithTag("recipe_continue").performScrollTo().performClick()
        rule.onNodeWithTag("ingredient_remove").assertDoesNotExist()
        rule.onNodeWithTag("ingredients_continue").performScrollTo().assertIsNotEnabled()
    }
    @Test fun removingLastIngredientBlocksAdvanceAndEditingRecalculates() {
        content(); name(); ingredient()
        rule.onNodeWithTag("ingredient_edit").performScrollTo().performClick()
        rule.onNodeWithTag("ingredient_used_quantity").performScrollTo().performTextReplacement("500")
        rule.onNodeWithTag("ingredient_add").performScrollTo().performClick()
        rule.onNodeWithText("R$\u00a05,00").performScrollTo().assertIsDisplayed()
        rule.onNodeWithTag("ingredient_remove").performScrollTo().performClick()
        rule.onNodeWithTag("ingredients_continue").performScrollTo().assertIsNotEnabled()
    }
    @Test fun unfinishedIngredientMustBeSavedOrCancelledBeforeLeavingTheStep() {
        content(); name(); ingredient()
        rule.onNodeWithTag("ingredient_edit").performScrollTo().assertIsDisplayed().performClick()
        rule.onNodeWithTag("ingredient_name").performScrollTo().performTextReplacement(
            "Chocolate com nome bastante longo para conferir a disposição das ações")
        rule.onNodeWithTag("ingredients_continue").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("wizard_save_draft").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("ingredient_add").performScrollTo().performClick()
        rule.onNodeWithTag("ingredient_edit").performScrollTo().assertIsDisplayed()
        rule.onNodeWithTag("ingredient_remove").assertIsDisplayed()
        rule.onNodeWithTag("ingredient_edit").performClick()
        rule.onNodeWithTag("ingredient_cancel_edit").performScrollTo().performClick()
        rule.onNodeWithTag("ingredients_continue").performScrollTo().assertIsEnabled()
        rule.onNodeWithTag("wizard_save_draft").performScrollTo().assertIsEnabled().performClick()
        rule.runOnIdle { assertEquals("Chocolate com nome bastante longo para conferir a disposição das ações", savedQuote!!.draft!!.recipe.ingredients.single().name) }
    }
    @Test fun operationalInputsCanBeClearedAndPackagingAndOverheadPersist() {
        content(); name(); ingredient()
        rule.onNodeWithTag("ingredients_continue").performScrollTo().performClick()
        rule.onNodeWithTag("operational_prep_minutes").performTextClearance()
        rule.onNodeWithTag("costs_calculate").performScrollTo().assertIsEnabled()
        rule.onNodeWithTag("operational_prep_minutes").performScrollTo().performTextInput("60")
        rule.onNodeWithTag("operational_packaging").performScrollTo().performTextReplacement("3,50")
        rule.onNodeWithTag("operational_overhead").performScrollTo().performTextReplacement("2,00")
        rule.onNodeWithTag("costs_calculate").performScrollTo().performClick()
        rule.runOnIdle { assertEquals(8, savedRecipe!!.yieldCount); assertEquals(350L, savedRecipe!!.packaging.cents); assertEquals(200L, savedRecipe!!.overhead.cents) }
    }
    @Test fun WhatsAppDoesNotClaimSuccessWhenCallbackFailsToOpen() {
        content(); customer()
        rule.onNodeWithTag("quote_send_whatsapp").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("customer_phone").performScrollTo().performTextInput("11987654321")
        rule.onNodeWithTag("quote_send_whatsapp").performScrollTo().performClick()
        rule.onNodeWithText("Orçamento pronto no WhatsApp").assertDoesNotExist()
        rule.runOnIdle { assertNotNull(sentQuote); assertEquals(QuoteStatus.Draft, sentQuote!!.status) }
    }
    @Test fun pdfDraftAndApprovalShareTheSameQuoteIdentity() {
        content(); customer()
        rule.onNodeWithTag("quote_share_pdf").performScrollTo().performClick()
        rule.onNodeWithTag("quote_approve").performScrollTo().performClick()
        rule.runOnIdle { assertEquals(sharedQuote!!.id, savedQuote!!.id); assertEquals(sharedQuote!!.code, savedQuote!!.code); assertFalse(savedQuote!!.code == "#ORC-284") }
    }
    @Test fun formSurvivesActivityStateRestorationIncludingIngredients() {
        val restoration = StateRestorationTester(rule)
        restoration.setContent { BruleeTheme { NewQuoteWizardScreen(OperatingSettings(), {}, {}, {}, {}) } }
        name(); ingredient()
        restoration.emulateSavedInstanceStateRestore()
        rule.onNodeWithText("Farinha real").performScrollTo().assertIsDisplayed()
        rule.onNodeWithTag("wizard_back").performScrollTo().performClick()
        rule.onNodeWithTag("recipe_name").assertTextContains("Bolo real")
        rule.onNodeWithTag("recipe_yield").assertTextContains("8")
    }
    @Test fun reducedMotionStillAllowsEveryWizardStepAndDraftSave() {
        content(reduced = true); customer()
        rule.onNodeWithTag("quote_save_draft").performScrollTo().performClick()
        rule.runOnIdle { assertEquals("Cliente real", savedQuote!!.customer.name); assertEquals(8, savedQuote!!.unitsCount) }
    }
    @Test fun legacyDraftEditsAvailableFieldsWithoutLosingSavedTotalsOrIdentity() {
        val legacy = Quote("old-draft", "#ORC-ANTIGO", Customer("old-customer", "Cliente do rascunho", "11987654321"),
            "Receita antiga", "", 8,
            PricingEngine.fromSalePrice(RecipeCosts(Money(2000), Money(1000), Money(300), Money(400)), Money(9000)),
            java.time.LocalDate.now().plusDays(2).format(com.bruleetelie.app.ui.screens.DeliveryDateFormat),
            validDays = 7, notes = "Retirada combinada")
        rule.setContent { BruleeTheme {
            NewQuoteWizardScreen(OperatingSettings(), {}, {}, {}, {}, initialQuote = legacy, onSaveQuote = { savedQuote = it })
        } }
        rule.onNodeWithTag("legacy_draft_notice").assertExists()
        rule.onNodeWithTag("customer_name").performScrollTo().assertTextContains(legacy.customer.name)
            .performTextReplacement("Cliente atualizado")
        rule.onNodeWithTag("delivery_date").performScrollTo().assertTextContains(legacy.deliveryDate)
        rule.onNodeWithTag("quote_save_draft").performScrollTo().performClick()
        rule.runOnIdle {
            assertEquals(legacy.copy(customer = legacy.customer.copy(name = "Cliente atualizado")), savedQuote)
        }
    }
    @Test fun manualCurrencyKeepsSequentialKeystrokesAndInvalidPriceBlocksAdvance() {
        content(); name(); ingredient()
        rule.onNodeWithTag("ingredients_continue").performScrollTo().performClick()
        rule.onNodeWithTag("costs_calculate").performScrollTo().performClick()
        rule.onNodeWithTag("price_manual_input").performScrollTo().performTextClearance()
        rule.onNodeWithTag("price_manual_input").performTextInput("1")
        rule.onNodeWithTag("price_manual_input").performTextInput("2")
        rule.onNodeWithTag("price_manual_input").performTextInput(",")
        rule.onNodeWithTag("price_manual_input").performTextInput("50")
        rule.onNodeWithTag("price_manual_input").assertTextContains("12,50")
        rule.onNodeWithTag("price_sale_value").performScrollTo().assertTextEquals("R$\u00a012,50")
        rule.onNodeWithTag("price_manual_input").performScrollTo().performTextReplacement("abc")
        rule.onNodeWithTag("quote_advance").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("price_manual_input").performScrollTo().performTextReplacement("1,00")
        rule.onNodeWithTag("price_decrease_5").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("price_loss_warning").performScrollTo().assertIsDisplayed()
        rule.onNodeWithTag("quote_advance").performScrollTo().assertIsEnabled()
    }
    @Test fun savedRecipeCanBeLoadedWithReducedMotionAndItsRealYield() {
        val recipe = SavedRecipe("saved", "v1", 1, "Minha receita", 6, listOf(RecipeIngredientItem("i", "Ovo", Quantity(12.toBigDecimal(), UnitOfMeasure.Unit), Money(1200), Quantity(3.toBigDecimal(), UnitOfMeasure.Unit))), OperationalTimes())
        rule.setContent { BruleeTheme { CompositionLocalProvider(LocalReducedMotion provides true) {
            NewQuoteWizardScreen(OperatingSettings(), {}, {}, {}, {}, savedRecipes = listOf(recipe))
        } } }
        rule.onNodeWithTag("brulee_mascot").assert(SemanticsMatcher.expectValue(androidx.compose.ui.semantics.SemanticsProperties.StateDescription, "Movimento reduzido"))
        rule.onNodeWithTag("recipe_load_saved").performScrollTo().performClick()
        rule.onNodeWithTag("recipe_name").assertTextContains("Minha receita")
        rule.onNodeWithTag("recipe_yield").assertTextContains("6")
        rule.onNodeWithTag("recipe_continue").performScrollTo().performClick()
        rule.onNodeWithText("Ovo").performScrollTo().assertIsDisplayed()
    }

    @Test fun optionalOperatingFieldsEmptyCalculateZeroWithoutReusingOldValues() {
        content(); name(); ingredient()
        rule.onNodeWithTag("ingredients_continue").performScrollTo().performClick()
        rule.onNodeWithTag("operational_prep_minutes").performTextReplacement("60")
        rule.onNodeWithTag("operational_packaging").performScrollTo().performTextReplacement("12,50")
        listOf("prep_minutes", "cook_minutes", "decoration_minutes", "packaging", "overhead").forEach {
            rule.onNodeWithTag("operational_$it").performScrollTo().performTextClearance()
        }
        rule.onNodeWithTag("costs_calculate").performScrollTo().assertIsEnabled().performClick()
        rule.runOnIdle {
            assertEquals(0, savedRecipe!!.times.prepTime.value.signum())
            assertEquals(0L, savedRecipe!!.packaging.cents)
            assertEquals(0L, savedRecipe!!.overhead.cents)
        }
        rule.onNodeWithTag("price_sale_value").performScrollTo().assertIsDisplayed()
    }
    @Test fun invalidOperatingCostsExplainErrorsAndCorrectionEnablesCalculation() {
        content(); name(); ingredient()
        rule.onNodeWithTag("ingredients_continue").performScrollTo().performClick()
        rule.onNodeWithTag("operational_prep_minutes").performTextReplacement("-5")
        rule.onNodeWithText("Informe minutos iguais ou maiores que zero").assertExists()
        rule.onNodeWithTag("costs_calculate").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("operational_prep_minutes").performScrollTo().performTextReplacement(" 10,5 ")
        rule.onNodeWithTag("operational_packaging").performScrollTo().performTextReplacement("abc")
        rule.onNodeWithText("Informe um valor igual ou maior que zero, como 3,50").assertExists()
        rule.onNodeWithTag("costs_calculate").performScrollTo().assertIsNotEnabled()
        rule.onNodeWithTag("operational_packaging").performScrollTo().performTextReplacement("3,50")
        rule.onNodeWithTag("costs_calculate").performScrollTo().assertIsEnabled().performClick()
        rule.runOnIdle { assertEquals("10.5", savedRecipe!!.times.prepTime.value.stripTrailingZeros().toPlainString()); assertEquals(350L, savedRecipe!!.packaging.cents) }
    }
    @Test fun pdfCarriesCustomerDeliveryValidityAndNotesFromTheForm() {
        content(); customer()
        rule.onNodeWithTag("customer_phone").performScrollTo().performTextInput("11987654321")
        rule.onNodeWithTag("quote_validity").performScrollTo().performTextReplacement("7")
        rule.onNodeWithTag("quote_notes").performScrollTo().performTextInput("Cobertura de chocolate e retirada às 16h.")
        rule.onNodeWithTag("quote_share_pdf").performScrollTo().performClick()
        rule.runOnIdle {
            assertEquals(7, sharedQuote!!.validDays)
            assertEquals("Cobertura de chocolate e retirada às 16h.", sharedQuote!!.notes)
            assertEquals("11987654321", sharedQuote!!.customer.phone)
            assertEquals("21/10/2026 16:00", sharedQuote!!.deliveryDate)
        }
    }

}
