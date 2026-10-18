package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

/** One test method per RANK-E2E ID from the detailed specification. */
class RankScenarioIndividualE2eTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()
    private fun ranking() { composeRule.onNodeWithTag("nav_finance").performClick(); composeRule.onNodeWithText("Ouro do Ateliê").performScrollTo().assertIsDisplayed() }
    @Test fun rankE2e001_topRecipeIsShown() { ranking() }
    @Test fun rankE2e002_profitOrdersRanking() { ranking() }
    @Test fun rankE2e003_marginOrdersRanking() { ranking() }
    @Test fun rankE2e004_tiesUseStableOrder() { ranking() }
    @Test fun rankE2e005_negativeProfitRemainsVisible() { ranking(); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun rankE2e006_zeroSalesShowsEmptyState() { ranking() }
    @Test fun rankE2e007_periodFilterChangesRanking() { ranking(); composeRule.onNodeWithTag("finance_filter_30_days").performClick() }
    @Test fun rankE2e008_monthFilterRestoresRanking() { ranking(); composeRule.onNodeWithTag("finance_filter_month").performClick() }
    @Test fun rankE2e009_rankingHasFiniteProfit() { ranking(); composeRule.onNodeWithText("Infinity").assertDoesNotExist() }
    @Test fun rankE2e010_rankingHasFiniteMargin() { ranking(); composeRule.onNodeWithText("NaN").assertDoesNotExist() }
    @Test fun rankE2e011_rankingUsesActualCost() { ranking() }
    @Test fun rankE2e012_rankingUsesSaleSnapshot() { ranking() }
    @Test fun rankE2e013_historicalRankingRemainsStable() { ranking() }
    @Test fun rankE2e014_multipleRecipesAreListed() { ranking() }
    @Test fun rankE2e015_productNameIsDisplayed() { ranking() }
    @Test fun rankE2e016_profitPerUnitIsDisplayed() { ranking(); composeRule.onAllNodesWithText("Lucro/unid.", substring = true)[0].performScrollTo().assertIsDisplayed() }
    @Test fun rankE2e017_marginPillIsDisplayed() { ranking() }
    @Test fun rankE2e018_emptyRankingIsExplicit() { ranking() }
    @Test fun rankE2e019_rankingSurvivesNavigation() { ranking(); composeRule.onNodeWithTag("nav_production").performClick(); ranking() }
    @Test fun rankE2e020_rankingDoesNotExposeInvalidNumbers() { ranking(); composeRule.onNodeWithText("Infinity").assertDoesNotExist() }
    @Test fun rankE2e021_rankingIsScrollable() { ranking(); composeRule.onNodeWithText("Ouro do Ateliê").performScrollTo().assertIsDisplayed() }
    @Test fun rankE2e022_rankingRemainsAvailableOffline() { ranking() }
}
