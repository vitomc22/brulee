package com.bruleetelie.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bruleetelie.domain.Money
import com.bruleetelie.domain.OperatingSettings
import java.math.BigDecimal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "operating_settings")

class OperatingSettingsDataStore(private val context: Context) {

    private object Keys {
        val HOURLY_LABOR_RATE_CENTS = longPreferencesKey("hourly_labor_rate_cents")
        val GAS_COST_PER_HOUR_CENTS = longPreferencesKey("gas_cost_per_hour_cents")
        val ELECTRICITY_COST_PER_HOUR_CENTS = longPreferencesKey("electricity_cost_per_hour_cents")
        val DEFAULT_MARGIN_PERCENT = stringPreferencesKey("default_margin_percent")
    }

    val settingsFlow: Flow<OperatingSettings> = context.dataStore.data.map { prefs ->
        val laborCents = prefs[Keys.HOURLY_LABOR_RATE_CENTS] ?: 2500L
        val gasCents = prefs[Keys.GAS_COST_PER_HOUR_CENTS] ?: 320L
        val elecCents = prefs[Keys.ELECTRICITY_COST_PER_HOUR_CENTS] ?: 250L
        val marginStr = prefs[Keys.DEFAULT_MARGIN_PERCENT] ?: "35.00"

        OperatingSettings(
            hourlyLaborRate = Money(laborCents),
            gasCostPerHour = Money(gasCents),
            electricityCostPerHour = Money(elecCents),
            defaultMarginPercent = BigDecimal(marginStr)
        )
    }

    suspend fun saveSettings(settings: OperatingSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.HOURLY_LABOR_RATE_CENTS] = settings.hourlyLaborRate.cents
            prefs[Keys.GAS_COST_PER_HOUR_CENTS] = settings.gasCostPerHour.cents
            prefs[Keys.ELECTRICITY_COST_PER_HOUR_CENTS] = settings.electricityCostPerHour.cents
            prefs[Keys.DEFAULT_MARGIN_PERCENT] = settings.defaultMarginPercent.toPlainString()
        }
    }
}
