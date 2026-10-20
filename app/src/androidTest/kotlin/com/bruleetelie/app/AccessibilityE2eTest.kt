package com.bruleetelie.app

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccessibilityE2eTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @org.junit.After fun removeTestData() { clearTestData(composeRule) }

    @org.junit.Before fun prepareRealTestData() { clearTestData(composeRule) }

    @Test
    fun primaryNavigationExposesTalkBackDescriptionAndClickAction() {
        composeRule.onNodeWithContentDescription("Navegação Produção")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
    }

    @Test
    fun primaryNavigationKeepsStableSemanticTagsForLargeTextLayouts() {
        composeRule.onNodeWithTag("nav_finance")
            .assertIsDisplayed()
            .assertHasClickAction()
        composeRule.onNodeWithTag("nav_wizard")
            .assertIsDisplayed()
            .assertHasClickAction()
    }
    @Test fun secondaryNavigationHasAccessibleDescriptions() {
        composeRule.onNodeWithContentDescription("Navegação Compras").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Configurações e backup").assertIsDisplayed().assertHasClickAction()
    }

}
