package com.bruleetelie.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bruleetelie.domain.Money
import com.bruleetelie.domain.OperatingSettings
import java.math.BigDecimal
import java.io.File
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "operating_settings")

class OperatingSettingsDataStore(private val context: Context) {

    private object Keys {
        val HOURLY_LABOR_RATE_CENTS = longPreferencesKey("hourly_labor_rate_cents")
        val GAS_COST_PER_HOUR_CENTS = longPreferencesKey("gas_cost_per_hour_cents")
        val ELECTRICITY_COST_PER_HOUR_CENTS = longPreferencesKey("electricity_cost_per_hour_cents")
        val DEFAULT_MARGIN_PERCENT = stringPreferencesKey("default_margin_percent")
        val MONTHLY_FIXED_COSTS_CENTS = longPreferencesKey("monthly_fixed_costs_cents")
        val MONTHLY_REVENUE_GOAL_CENTS = longPreferencesKey("monthly_revenue_goal_cents")
        val SOUNDS_ENABLED = booleanPreferencesKey("sounds_enabled")
        val REDUCED_MOTION = booleanPreferencesKey("reduced_motion")
    }

    val settingsFlow: Flow<OperatingSettings> = context.dataStore.data.map(::decode)

    private fun decode(prefs: Preferences): OperatingSettings {
        val laborCents = prefs[Keys.HOURLY_LABOR_RATE_CENTS] ?: 2500L
        val gasCents = prefs[Keys.GAS_COST_PER_HOUR_CENTS] ?: 320L
        val elecCents = prefs[Keys.ELECTRICITY_COST_PER_HOUR_CENTS] ?: 250L
        val marginStr = prefs[Keys.DEFAULT_MARGIN_PERCENT] ?: "35.00"

        return OperatingSettings(
            hourlyLaborRate = Money(laborCents),
            gasCostPerHour = Money(gasCents),
            electricityCostPerHour = Money(elecCents),
            defaultMarginPercent = BigDecimal(marginStr),
            monthlyFixedCosts = Money(prefs[Keys.MONTHLY_FIXED_COSTS_CENTS] ?: 0L),
            monthlyRevenueGoal = Money(prefs[Keys.MONTHLY_REVENUE_GOAL_CENTS] ?: 0L),
            soundsEnabled = prefs[Keys.SOUNDS_ENABLED] ?: true,
            reducedMotion = prefs[Keys.REDUCED_MOTION] ?: false
        )
    }

    /** Decode in a temporary store before changing live data; never replace an active DataStore file. */
    suspend fun readBackup(bytes: ByteArray): OperatingSettings = withContext(Dispatchers.IO) {
        val file = File.createTempFile("brulee-settings-", ".preferences_pb", context.cacheDir)
        val job = SupervisorJob()
        try {
            file.writeBytes(bytes)
            val temporary = PreferenceDataStoreFactory.create(scope = CoroutineScope(job + Dispatchers.IO), produceFile = { file })
            decode(temporary.data.first())
        } finally {
            job.cancelAndJoin()
            file.delete()
            File("${file.path}.tmp").delete()
        }
    }

    suspend fun saveSettings(settings: OperatingSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.HOURLY_LABOR_RATE_CENTS] = settings.hourlyLaborRate.cents
            prefs[Keys.GAS_COST_PER_HOUR_CENTS] = settings.gasCostPerHour.cents
            prefs[Keys.ELECTRICITY_COST_PER_HOUR_CENTS] = settings.electricityCostPerHour.cents
            prefs[Keys.DEFAULT_MARGIN_PERCENT] = settings.defaultMarginPercent.toPlainString()
            prefs[Keys.MONTHLY_FIXED_COSTS_CENTS] = settings.monthlyFixedCosts.cents
            prefs[Keys.MONTHLY_REVENUE_GOAL_CENTS] = settings.monthlyRevenueGoal.cents
            prefs[Keys.SOUNDS_ENABLED] = settings.soundsEnabled
            prefs[Keys.REDUCED_MOTION] = settings.reducedMotion
        }
    }
}
