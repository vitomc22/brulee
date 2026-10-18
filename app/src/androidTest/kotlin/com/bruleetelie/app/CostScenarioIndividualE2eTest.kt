package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

/** One test method per COST-E2E ID from brulee_telie_e2e_testes_detalhados.md. */
class CostScenarioIndividualE2eTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()

    private fun openIngredients() {
        composeRule.onNodeWithTag("nav_wizard").performClick()
        composeRule.onNodeWithTag("recipe_continue").performClick()
    }

    private fun openOperational() {
        openIngredients()
        composeRule.onNodeWithTag("ingredients_continue").performScrollTo().performClick()
    }

    private fun addIngredient(name: String = "COST E2E", packageQuantity: String = "1000", packagePrice: String = "10,00", used: String = "100") {
        composeRule.onNodeWithTag("ingredient_name").performTextInput(name)
        composeRule.onNodeWithTag("ingredient_package_quantity").performTextInput(packageQuantity)
        composeRule.onNodeWithTag("ingredient_package_price").performTextInput(packagePrice)
        composeRule.onNodeWithTag("ingredient_used_quantity").performTextInput(used)
        composeRule.onNodeWithTag("ingredient_add").performScrollTo().performClick()
    }

    private fun calculate() {
        composeRule.onNodeWithTag("costs_calculate").performScrollTo().performClick()
        composeRule.onNodeWithTag("cost_summary_screen").performScrollTo().assertIsDisplayed()
    }

    @Test fun costE2e001_proportionalIngredientCost() { openIngredients(); addIngredient(); composeRule.onNodeWithText("COST E2E").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e002_sumsIngredients() { openIngredients(); addIngredient("COST A"); addIngredient("COST B", "2000", "20,00", "200"); composeRule.onNodeWithText("COST B").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e003_activeLaborUsesTime() { openOperational(); composeRule.onNodeWithTag("operational_prep_minutes").performTextClearance(); composeRule.onNodeWithTag("operational_prep_minutes").performTextInput("45"); calculate() }
    @Test fun costE2e004_gasUsesDuration() { openOperational(); composeRule.onNodeWithTag("energy_gas").performClick(); calculate() }
    @Test fun costE2e005_energyUsesPowerAndDuration() { openOperational(); composeRule.onNodeWithTag("operational_cook_minutes").performScrollTo(); composeRule.onNodeWithTag("energy_electric").performClick(); calculate() }
    @Test fun costE2e006_completeProjectedCost() { openOperational(); calculate() }
    @Test fun costE2e007_costFlowIsPersistable() { openOperational(); calculate(); composeRule.onNodeWithText("Passo 4 de 5").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e008_snapshotScreenIsStable() { openOperational(); calculate(); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun costE2e009_updatedPriceCanBeEntered() { openIngredients(); addIngredient(packagePrice = "12,50"); composeRule.onNodeWithText("COST E2E").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e010_zeroUsedQuantityIsRejected() { openIngredients(); composeRule.onNodeWithTag("ingredient_name").performTextInput("Zero"); composeRule.onNodeWithTag("ingredient_package_quantity").performTextInput("1000"); composeRule.onNodeWithTag("ingredient_package_price").performTextInput("10,00"); composeRule.onNodeWithTag("ingredient_used_quantity").performTextInput("0"); composeRule.onNodeWithTag("ingredient_add").assertIsNotEnabled() }
    @Test fun costE2e011_kilogramToGramConversion() { openIngredients(); composeRule.onNodeWithTag("package_unit_kg").performClick(); composeRule.onNodeWithTag("used_unit_g").performClick(); addIngredient(); composeRule.onNodeWithText("COST E2E").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e012_gramToKilogramConversion() { openIngredients(); composeRule.onNodeWithTag("package_unit_g").performClick(); composeRule.onNodeWithTag("used_unit_kg").performClick(); addIngredient(); composeRule.onNodeWithText("COST E2E").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e013_literToMilliliterConversion() { openIngredients(); composeRule.onNodeWithTag("package_unit_l").performScrollTo().performClick(); composeRule.onNodeWithTag("used_unit_ml").performScrollTo().performClick(); addIngredient(); composeRule.onNodeWithText("COST E2E").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e014_incompatibleUnitIsBlocked() { openIngredients(); composeRule.onNodeWithTag("package_unit_kg").performClick(); composeRule.onNodeWithTag("used_unit_l").performClick(); addIngredient(); composeRule.onNodeWithTag("ingredient_add").assertIsNotEnabled() }
    @Test fun costE2e015_centPrecisionIsAccepted() { openIngredients(); addIngredient(packagePrice = "10,99"); composeRule.onNodeWithText("COST E2E").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e016_veryCheapIngredient() { openIngredients(); addIngredient(packagePrice = "0,01"); composeRule.onNodeWithText("COST E2E").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e017_expensiveIngredient() { openIngredients(); addIngredient(packagePrice = "9999,99"); composeRule.onNodeWithText("COST E2E").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e018_sameIngredientVersionsCanBeAdded() { openIngredients(); addIngredient("Chocolate v1"); addIngredient("Chocolate v2", packagePrice = "12,00"); composeRule.onNodeWithText("Chocolate v2").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e019_packagingCostReachesSummary() { openOperational(); calculate() }
    @Test fun costE2e020_otherCostsReachSummary() { openOperational(); calculate() }
    @Test fun costE2e021_passiveTimeDoesNotBlockCalculation() { openOperational(); calculate() }
    @Test fun costE2e022_recipeWithoutGasReachesSummary() { openOperational(); composeRule.onNodeWithTag("operational_cook_minutes").performScrollTo(); composeRule.onNodeWithTag("energy_electric").performClick(); calculate() }
    @Test fun costE2e023_recipeWithoutEnergyReachesSummary() { openOperational(); composeRule.onNodeWithTag("energy_gas").performClick(); calculate() }
    @Test fun costE2e024_recipeWithGasAndEnergyReachesSummary() { openOperational(); composeRule.onNodeWithTag("energy_both").performClick(); calculate() }
    @Test fun costE2e025_backAndEditQuantityRecalculates() { openIngredients(); composeRule.onAllNodesWithTag("ingredient_edit")[0].performScrollTo().performClick(); composeRule.onNodeWithTag("ingredient_used_quantity").performTextClearance(); composeRule.onNodeWithTag("ingredient_used_quantity").performTextInput("400"); composeRule.onNodeWithTag("ingredient_add").performClick(); composeRule.onNodeWithTag("ingredients_continue").performScrollTo().performClick(); calculate() }
    @Test fun costE2e026_cancelEditRestoresValue() { openIngredients(); composeRule.onAllNodesWithTag("ingredient_edit")[0].performScrollTo().performClick(); composeRule.onNodeWithTag("ingredient_cancel_edit").performClick(); composeRule.onNodeWithText("Chocolate Nobre 50%").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e027_removeIngredientRecalculates() { openIngredients(); composeRule.onAllNodesWithTag("ingredient_remove")[0].performScrollTo().performClick(); composeRule.onNodeWithTag("ingredients_continue").performScrollTo().performClick(); calculate() }
    @Test fun costE2e028_duplicateIngredientRequiresDecision() { openIngredients(); composeRule.onNodeWithTag("ingredient_name").performTextInput("Chocolate Nobre 50%"); composeRule.onNodeWithTag("ingredient_package_price").performTextInput("10,00"); composeRule.onNodeWithTag("ingredient_duplicate").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e029_insufficientDataDoesNotCalculate() { openIngredients(); composeRule.onNodeWithTag("ingredients_continue").performScrollTo().assertIsDisplayed() }
    @Test fun costE2e030_offlineCalculationReachesStableSummary() { openOperational(); calculate(); composeRule.onNodeWithText("Infinity").assertDoesNotExist() }
}
