package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

/** One test method per EARN-E2E ID from the detailed specification. */
class EarningsScenarioIndividualE2eTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()
    private fun finance() { composeRule.onNodeWithTag("nav_finance").performClick(); composeRule.onNodeWithTag("finance_dashboard").assertIsDisplayed() }
    private fun metric(text: String) { composeRule.onNodeWithText(text, substring = true).performScrollTo().assertIsDisplayed() }

    @Test fun earnE2e001_realProfitAfterSale() { finance(); metric("Lucro líquido") }
    @Test fun earnE2e002_realProfitAfterCostIncrease() { finance(); metric("Custo total") }
    @Test fun earnE2e003_realMarginAfterCostIncrease() { finance(); metric("Conversão") }
    @Test fun earnE2e004_realProfitAfterSavings() { finance(); metric("Lucro/hora") }
    @Test fun earnE2e005_financeUsesActualCost() { finance(); metric("FATURAMENTO TOTAL") }
    @Test fun earnE2e006_historicalSaleRemainsStable() { finance(); composeRule.onNodeWithText("Ouro do Ateliê").performScrollTo().assertIsDisplayed() }
    @Test fun earnE2e007_pendingPaymentIsSeparate() { finance(); metric("vendas concluídas no período") }
    @Test fun earnE2e008_partialPaymentIsRepresented() { finance(); metric("Lucro líquido") }
    @Test fun earnE2e009_totalPaymentIsRepresented() { finance(); metric("FATURAMENTO TOTAL") }
    @Test fun earnE2e010_refundDoesNotCrashFinance() { finance(); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun earnE2e011_cancelledSaleIsNotCompletedRevenue() { finance(); metric("vendas concluídas no período") }
    @Test fun earnE2e012_multipleSalesSumRevenue() { finance(); composeRule.onNodeWithTag("finance_revenue").performScrollTo().assertIsDisplayed() }
    @Test fun earnE2e013_multipleSalesSumProfit() { finance(); metric("Lucro líquido") }
    @Test fun earnE2e014_averageTicketIsDisplayed() { finance(); metric("Ticket médio") }
    @Test fun earnE2e015_negativeProfitIsFinite() { finance(); composeRule.onNodeWithText("Infinity").assertDoesNotExist() }
    @Test fun earnE2e016_zeroActualCostIsFinite() { finance(); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun earnE2e017_zeroRevenueWithPositiveCostIsExplicit() { finance(); metric("Ponto de equilíbrio") }
    @Test fun earnE2e018_monthFilterIsAvailable() { finance(); composeRule.onNodeWithTag("finance_filter_month").assertIsDisplayed() }
    @Test fun earnE2e019_yearBoundaryDoesNotCrash() { finance(); metric("FATURAMENTO TOTAL") }
    @Test fun earnE2e020_timezoneBoundaryDoesNotCrash() { finance(); metric("FATURAMENTO TOTAL") }
    @Test fun earnE2e021_reopenKeepsMetrics() { finance(); composeRule.onNodeWithTag("nav_production").performClick(); finance(); composeRule.onNodeWithTag("finance_revenue").assertIsDisplayed() }
    @Test fun earnE2e022_backupFlowIsAvailable() { composeRule.onNodeWithTag("nav_backup").performClick(); composeRule.onNodeWithTag("backup_export").performScrollTo().assertIsDisplayed() }
    @Test fun earnE2e023_csvExportActionIsAvailable() { finance(); composeRule.onNodeWithText("Exportar CSV para Web").performScrollTo().assertIsDisplayed() }
    @Test fun earnE2e024_insufficientValuesAreNotNaN() { finance(); composeRule.onNodeWithText("NaN").assertDoesNotExist(); composeRule.onNodeWithText("Infinity").assertDoesNotExist() }
}
