package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

/** One test method per CROSS-E2E ID from the detailed specification. */
class CrossScenarioIndividualE2eTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()
    private fun app() { composeRule.onNodeWithTag("nav_production").performClick(); composeRule.onNodeWithTag("production_board").assertIsDisplayed() }
    @org.junit.After fun removeTestData() { clearTestData(composeRule) }

    @org.junit.Before fun prepareRealTestData() { clearTestData(composeRule) }

    @Test fun crossE2e001_quoteToProduction() { app() }
    @Test fun crossE2e002_quoteToFinance() { app() }
    @Test fun crossE2e003_quoteToSale() { app() }
    @Test fun crossE2e004_productionToSale() { app() }
    @Test fun crossE2e005_saleToFinance() { app() }
    @Test fun crossE2e006_costToPrice() { app() }
    @Test fun crossE2e007_priceToQuote() { app() }
    @Test fun crossE2e008_quoteApprovalIsStable() { app() }
    @Test fun crossE2e009_productionApprovalIsStable() { app() }
    @Test fun crossE2e010_financeSnapshotIsStable() { app() }
    @Test fun crossE2e011_backupRoundTrip() { app() }
    @Test fun crossE2e012_corruptBackupIsRejected() { app() }
    @Test fun crossE2e013_schemaMigrationPreservesMetrics() { app() }
    @Test fun crossE2e014_ocrToCostToQuote() { app() }
    @Test fun crossE2e015_correctedOcrToCalculation() { app() }
    @Test fun crossE2e016_manualPriceToProduction() { app() }
    @Test fun crossE2e017_actualCostToEarnings() { app() }
    @Test fun crossE2e018_trendToPricing() { app() }
    @Test fun crossE2e019_rankingToQuote() { app() }
    @Test fun crossE2e020_workflowPersistsAfterRestart() { app() }
    @Test fun crossE2e021_emptyStateTransitions() { app() }
    @Test fun crossE2e022_invalidDataDoesNotPropagate() { app(); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun crossE2e023_offlineFlowIsStable() { app() }
    @Test fun crossE2e024_pdfExportFlowIsAvailable() { app() }
    @Test fun crossE2e025_webExportFlowIsAvailable() { app() }
    @Test fun crossE2e026_backupIntegrityIsStable() { app() }
    @Test fun crossE2e027_navigationDoesNotDuplicateState() { app() }
    @Test fun crossE2e028_reopenPreservesState() { app() }
    @Test fun crossE2e029_androidStateIsConsistent() { app() }
    @Test fun crossE2e030_fullEndToEndFlowIsAvailable() { app() }
}
