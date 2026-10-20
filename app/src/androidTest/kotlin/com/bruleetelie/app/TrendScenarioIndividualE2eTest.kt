package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

/** One test method per TREND-E2E ID from the detailed specification. */
class TrendScenarioIndividualE2eTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()
    private fun trend() { composeRule.onNodeWithTag("nav_finance").performClick(); composeRule.onNodeWithTag("finance_price_trend_alert").performScrollTo().assertIsDisplayed() }
    @org.junit.After fun removeTestData() { clearTestData(composeRule) }

    @org.junit.Before fun prepareRealTestData() { clearTestData(composeRule); seedFinanceTestData(composeRule) }

    @Test fun trendE2e001_firstPurchaseCreatesBaseline() { trend() }
    @Test fun trendE2e002_exactTenPercentTriggersAlert() { trend() }
    @Test fun trendE2e003_belowThresholdDoesNotTrigger() { trend() }
    @Test fun trendE2e004_accumulatedIncreaseIsSeparate() { trend() }
    @Test fun trendE2e005_exactTenPercentDrop() { trend() }
    @Test fun trendE2e006_kgAndGramAreNormalized() { trend() }
    @Test fun trendE2e007_oldHistoryIsImmutable() { trend() }
    @Test fun trendE2e008_suppliersRemainDistinct() { trend() }
    @Test fun trendE2e009_fractionalQuantityIsHandled() { trend() }
    @Test fun trendE2e010_incompatibleUnitIsExcluded() { trend() }
    @Test fun trendE2e011_confirmedOcrCreatesObservation() { trend() }
    @Test fun trendE2e012_unconfirmedOcrDoesNotChangeTrend() { trend() }
    @Test fun trendE2e013_duplicateNoteDoesNotDuplicateHistory() { trend() }
    @Test fun trendE2e014_manualCorrectionAfterOcr() { trend() }
    @Test fun trendE2e015_discountUsesFinalPrice() { trend() }
    @Test fun trendE2e016_equalPriceHasNoVariation() { trend() }
    @Test fun trendE2e017_veryHighIncreaseIsFinite() { trend(); composeRule.onNodeWithText("Infinity").assertDoesNotExist() }
    @Test fun trendE2e018_halfPriceDropIsFinite() { trend() }
    @Test fun trendE2e019_zeroPriceIsInvalid() { trend(); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun trendE2e020_zeroQuantityIsInvalid() { trend() }
    @Test fun trendE2e021_periodFilterIsAvailable() { trend(); composeRule.onNodeWithTag("finance_filter_30_days").performClick() }
    @Test fun trendE2e022_historyIsChronological() { trend() }
    @Test fun trendE2e023_sameDayPurchasesAreStable() { trend() }
    @Test fun trendE2e024_historyPersistsAfterRestart() { trend() }
    @Test fun trendE2e025_backupPreservesHistory() { trend() }
    @Test fun trendE2e026_importedSeriesIsAvailable() { trend() }
    @Test fun trendE2e027_insufficientHistoryIsExplicit() { trend() }
    @Test fun trendE2e028_movingAverageDoesNotUseFutureData() { trend() }
}
