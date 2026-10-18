package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class BackupScreenE2eTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun backupActionsAreAvailableFromNavigation() {
        composeRule.onNodeWithTag("nav_backup").performClick()
        composeRule.onNodeWithTag("backup_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_hourly_labor_rate").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_gas_rate").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_electricity_rate").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_revenue_goal").assertIsDisplayed()
        composeRule.onNodeWithTag("backup_export").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("backup_restore").performScrollTo().assertIsDisplayed()
    }
}
