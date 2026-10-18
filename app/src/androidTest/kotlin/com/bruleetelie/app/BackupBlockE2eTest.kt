package com.bruleetelie.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class BackupBlockE2eTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun backup001To010_screenExposesPortableBackupActions() {
        composeRule.onNodeWithTag("nav_backup").performClick()
        composeRule.onNodeWithTag("backup_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("backup_export").performScrollTo().assertIsEnabled()
        composeRule.onNodeWithTag("backup_restore").performScrollTo().assertIsEnabled()
    }

    @Test
    fun backup011To020_financialSettingsCanBeEditedBeforeBackup() {
        composeRule.onNodeWithTag("nav_backup").performClick()
        composeRule.onNodeWithTag("settings_hourly_labor_rate").performTextClearance()
        composeRule.onNodeWithTag("settings_hourly_labor_rate").performTextInput("30,00")
        composeRule.onNodeWithTag("settings_gas_rate").performTextClearance()
        composeRule.onNodeWithTag("settings_gas_rate").performTextInput("4,00")
        composeRule.onNodeWithTag("settings_electricity_rate").performTextClearance()
        composeRule.onNodeWithTag("settings_electricity_rate").performTextInput("1,20")
        composeRule.onNodeWithTag("settings_revenue_goal").performTextClearance()
        composeRule.onNodeWithTag("settings_revenue_goal").performTextInput("5000,00")
        composeRule.onNodeWithTag("backup_export").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun backup021To030_preferencesRemainAvailableForRestore() {
        composeRule.onNodeWithTag("nav_backup").performClick()
        composeRule.onNodeWithTag("settings_sounds").performClick()
        composeRule.onNodeWithTag("settings_reduced_motion").performClick()
        composeRule.onNodeWithTag("settings_sounds").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_reduced_motion").assertIsDisplayed()
    }

    @Test
    fun backup031To040_navigationKeepsBackupScreenStable() {
        composeRule.onNodeWithTag("nav_backup").performClick()
        composeRule.onNodeWithTag("nav_finance").performClick()
        composeRule.onNodeWithTag("nav_backup").performClick()
        composeRule.onNodeWithTag("backup_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("backup_restore").performScrollTo().assertIsDisplayed()
    }
}
