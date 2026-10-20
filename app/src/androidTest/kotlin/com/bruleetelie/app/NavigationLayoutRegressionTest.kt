package com.bruleetelie.app

import android.content.pm.ApplicationInfo
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class NavigationLayoutRegressionTest {
    @get:Rule val rule = createAndroidComposeRule<MainActivity>()
    @org.junit.Before fun prepare() { clearTestData(rule) }
    @org.junit.After fun cleanup() { clearTestData(rule) }

    @Test fun fiveTabsHaveConsistentTouchTargetsAndOpenTheirRealScreens() {
        val destinations = listOf("quotes" to "quotes_screen", "wizard" to "recipe_name", "finance" to "finance_dashboard",
            "stock" to "inventory_screen", "production" to "production_board")
        destinations.forEach { (destination, screen) ->
            rule.onNodeWithTag("nav_$destination").assertWidthIsAtLeast(androidx.compose.ui.unit.Dp(48f))
                .assertHeightIsAtLeast(androidx.compose.ui.unit.Dp(48f)).performClick().assertIsSelected()
            rule.onNodeWithTag(screen).assertIsDisplayed()
        }
        rule.onNodeWithTag("nav_shopping").assertDoesNotExist()
        rule.onNodeWithTag("brand_mark").assertIsDisplayed()
        rule.onNodeWithTag("nav_backup").performClick()
        rule.onNodeWithTag("settings_reduced_motion").performScrollTo().assertIsDisplayed()
        rule.onNodeWithTag("settings_sounds").assertDoesNotExist()
    }

    @Test fun debugDistributionCannotAutomaticallyRestoreOldTestRegistrations() {
        assertEquals(0, rule.activity.applicationInfo.flags and ApplicationInfo.FLAG_ALLOW_BACKUP)
        rule.onNodeWithText("0 ativas").assertIsDisplayed()
        rule.onNodeWithTag("nav_quotes").performClick()
        rule.onNodeWithTag("quotes_empty").assertIsDisplayed()
        rule.onNodeWithTag("nav_stock").performClick()
        rule.onNodeWithTag("inventory_empty").performScrollTo().assertIsDisplayed()
    }
}
