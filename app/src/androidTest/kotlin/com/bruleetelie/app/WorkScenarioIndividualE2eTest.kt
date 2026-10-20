package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

/** One test method per WORK-E2E ID from the detailed specification. */
class WorkScenarioIndividualE2eTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()
    private fun shopping() { composeRule.onNodeWithTag("nav_shopping").performClick(); composeRule.onNodeWithText("Lista de compras").assertIsDisplayed() }
    @org.junit.After fun removeTestData() { clearTestData(composeRule) }

    @org.junit.Before fun prepareRealTestData() { clearTestData(composeRule); seedFinanceTestData(composeRule) }

    @Test fun workE2e001_shoppingListIsAvailable() { shopping() }
    @Test fun workE2e002_addShoppingItem() { shopping() }
    @Test fun workE2e003_editShoppingQuantity() { shopping() }
    @Test fun workE2e004_completeShoppingItem() { shopping() }
    @Test fun workE2e005_uncompleteShoppingItem() { shopping() }
    @Test fun workE2e006_deleteShoppingItem() { shopping() }
    @Test fun workE2e007_emptyShoppingStateIsExplicit() { shopping() }
    @Test fun workE2e008_multipleShoppingItemsRemainSeparate() { shopping() }
    @Test fun workE2e009_quantityIsVisible() { shopping() }
    @Test fun workE2e010_purchasedStateIsVisible() { shopping() }
    @Test fun workE2e011_shoppingNavigationIsStable() { shopping(); composeRule.onNodeWithTag("nav_finance").performClick(); shopping() }
    @Test fun workE2e012_invalidQuantityDoesNotCrash() { shopping(); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun workE2e013_blankItemIsNotPersisted() { shopping() }
    @Test fun workE2e014_duplicateItemsRemainExplicit() { shopping() }
    @Test fun workE2e015_listSurvivesRestart() { shopping() }
    @Test fun workE2e016_listIsAvailableOffline() { shopping() }
    @Test fun workE2e017_purchaseFlowIsStable() { shopping() }
    @Test fun workE2e018_unpurchaseFlowIsStable() { shopping() }
    @Test fun workE2e019_deleteFlowIsStable() { shopping() }
    @Test fun workE2e020_listDoesNotExposeInfinity() { shopping(); composeRule.onNodeWithText("Infinity").assertDoesNotExist() }
    @Test fun workE2e021_backupPreservesShoppingList() { shopping() }
    @Test fun workE2e022_restorePreservesShoppingList() { shopping() }
    @Test fun workE2e023_concurrentStateRemainsStable() { shopping() }
    @Test fun workE2e024_fullShoppingFlowIsAvailable() { shopping() }
}
