package com.bruleetelie.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.*
import com.bruleetelie.domain.Money
import com.bruleetelie.domain.OperatingSettings

@Composable
fun BackupScreen(
    settings: OperatingSettings,
    onSettingsChanged: (OperatingSettings) -> Unit,
    onExport: () -> Unit,
    onRestore: () -> Unit,
    onExportAnalytics: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("backup_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Configurações e backup", style = MaterialTheme.typography.headlineMedium)
        var laborRate by rememberSaveable {
            mutableStateOf(settings.hourlyLaborRate.toBigDecimal().toPlainString())
        }
        OutlinedTextField(
            value = laborRate,
            onValueChange = { value ->
                laborRate = value
                runCatching { Money.fromReais(value) }.getOrNull()?.takeIf { it >= Money.Zero }?.let {
                    onSettingsChanged(settings.copy(hourlyLaborRate = it))
                }
            },
            label = { Text("Valor da hora de trabalho") },
            modifier = Modifier.fillMaxWidth().testTag("settings_hourly_labor_rate")
        )
        var gasRate by rememberSaveable {
            mutableStateOf(settings.gasCostPerHour.toBigDecimal().toPlainString())
        }
        OutlinedTextField(
            value = gasRate,
            onValueChange = { value ->
                gasRate = value
                runCatching { Money.fromReais(value) }.getOrNull()?.takeIf { it >= Money.Zero }?.let {
                    onSettingsChanged(settings.copy(gasCostPerHour = it))
                }
            },
            label = { Text("Custo do gás por hora") },
            modifier = Modifier.fillMaxWidth().testTag("settings_gas_rate")
        )
        var electricityRate by rememberSaveable {
            mutableStateOf(settings.electricityCostPerHour.toBigDecimal().toPlainString())
        }
        OutlinedTextField(
            value = electricityRate,
            onValueChange = { value ->
                electricityRate = value
                runCatching { Money.fromReais(value) }.getOrNull()?.takeIf { it >= Money.Zero }?.let {
                    onSettingsChanged(settings.copy(electricityCostPerHour = it))
                }
            },
            label = { Text("Custo da energia por hora") },
            modifier = Modifier.fillMaxWidth().testTag("settings_electricity_rate")
        )
        var goal by rememberSaveable {
            mutableStateOf(settings.monthlyRevenueGoal.toBigDecimal().toPlainString())
        }
        OutlinedTextField(
            value = goal,
            onValueChange = { value ->
                goal = value
                runCatching { Money.fromReais(value) }.getOrNull()?.takeIf { it >= Money.Zero }?.let {
                    onSettingsChanged(settings.copy(monthlyRevenueGoal = it))
                }
            },
            label = { Text("Meta mensal de faturamento") },
            modifier = Modifier.fillMaxWidth().testTag("settings_revenue_goal")
        )
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Reduzir animações")
            Switch(
                checked = settings.reducedMotion,
                onCheckedChange = { onSettingsChanged(settings.copy(reducedMotion = it)) },
                modifier = Modifier.testTag("settings_reduced_motion")
            )
        }
        var fixed by remember { mutableStateOf(settings.monthlyFixedCosts.toBigDecimal().toPlainString()) }
        OutlinedTextField(fixed, { value ->
            fixed = value
            runCatching { Money.fromReais(value) }.getOrNull()?.takeIf { it >= Money.Zero }?.let { onSettingsChanged(settings.copy(monthlyFixedCosts = it)) }
        }, label = { Text("Despesas fixas mensais") }, modifier = Modifier.fillMaxWidth().testTag("settings_fixed_costs"))
        var margin by remember { mutableStateOf(settings.defaultMarginPercent.toPlainString()) }
        OutlinedTextField(margin, { value ->
            margin = value
            value.replace(',', '.').toBigDecimalOrNull()?.takeIf { it >= java.math.BigDecimal.ZERO && it < java.math.BigDecimal(100) }?.let { onSettingsChanged(settings.copy(defaultMarginPercent = it)) }
        }, label = { Text("Margem padrão (%)") }, modifier = Modifier.fillMaxWidth().testTag("settings_default_margin"))
        TactileSecondaryButton("EXPORTAR DADOS PARA WEB", onExportAnalytics, Modifier.testTag("analytics_export"), BruleeSymbol.Download)
        Text(
            "Exporte um arquivo portátil com banco, histórico e configurações. " +
                "A restauração valida versão e integridade antes de substituir os dados.",
            style = MaterialTheme.typography.bodyMedium
        )
        TactilePrimaryButton("EXPORTAR BACKUP", onExport, Modifier.testTag("backup_export"), BruleeSymbol.Download)
        TactileSecondaryButton("RESTAURAR BACKUP", onRestore, Modifier.testTag("backup_restore"), BruleeSymbol.Orders)
    }
}
