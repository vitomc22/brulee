package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import kotlinx.coroutines.flow.first
import org.junit.Rule
import org.junit.Test

class OcrReviewE2eTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @org.junit.After fun removeTestData() { clearTestData(composeRule) }

    @org.junit.Before fun prepareRealTestData() { clearTestData(composeRule) }

    @Test
    fun reviewRequiresEveryOcrItemConfirmation_beforeReturningToProduction() {
        openOcrFixture(composeRule)
        composeRule.onNodeWithTag("ocr_process_document")
            .performScrollTo()
            .performClick()

        composeRule.onNodeWithText("Revisão Obrigatória").assertIsDisplayed()
        composeRule.onNodeWithTag("ocr_save_confirmed")
            .performScrollTo()
            .assertIsNotEnabled()

        listOf("1", "2", "3", "4").forEach { itemId ->
            composeRule.onNodeWithTag("ocr_confirm_$itemId")
                .performScrollTo()
                .performClick()
        }

        composeRule.onNodeWithTag("ocr_save_confirmed")
            .performScrollTo()
            .assertIsEnabled()
            .performClick()

        composeRule.onNodeWithTag("inventory_screen").assertIsDisplayed()
        composeRule.onNodeWithText("4 observações de preço salvas").assertIsDisplayed()
        val db = com.bruleetelie.data.db.BruleeDatabase.getInstance(composeRule.activity)
        kotlinx.coroutines.runBlocking {
            val observations = kotlinx.coroutines.withTimeout(10000) { db.priceObservationDao().getAllObservations().first { it.size == 4 } }
            val ingredients = db.ingredientDao().getAllIngredients().first()
            org.junit.Assert.assertTrue(observations.all { observation -> ingredients.any { it.id == observation.ingredientId } })
        }
    }
}
