package com.bruleetelie.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.bruleetelie.domain.Money
import com.bruleetelie.domain.OperatingSettings

@Composable
fun BackupScreen(
    settings: OperatingSettings,
    onSettingsChanged: (OperatingSettings) -> Unit,
    onExport: () -> Unit,
    onRestore: () -> Unit
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
        var laborRate by remember(settings.hourlyLaborRate) {
            mutableStateOf(settings.hourlyLaborRate.toBigDecimal().toPlainString())
        }
        OutlinedTextField(
            value = laborRate,
            onValueChange = { value ->
                laborRate = value
                runCatching { Money.fromReais(value) }.getOrNull()?.let {
                    onSettingsChanged(settings.copy(hourlyLaborRate = it))
                }
            },
            label = { Text("Valor da hora de trabalho") },
            modifier = Modifier.fillMaxWidth().testTag("settings_hourly_labor_rate")
        )
        var gasRate by remember(settings.gasCostPerHour) {
            mutableStateOf(settings.gasCostPerHour.toBigDecimal().toPlainString())
        }
        OutlinedTextField(
            value = gasRate,
            onValueChange = { value ->
                gasRate = value
                runCatching { Money.fromReais(value) }.getOrNull()?.let {
                    onSettingsChanged(settings.copy(gasCostPerHour = it))
                }
            },
            label = { Text("Custo do gás por hora") },
            modifier = Modifier.fillMaxWidth().testTag("settings_gas_rate")
        )
        var electricityRate by remember(settings.electricityCostPerHour) {
            mutableStateOf(settings.electricityCostPerHour.toBigDecimal().toPlainString())
        }
        OutlinedTextField(
            value = electricityRate,
            onValueChange = { value ->
                electricityRate = value
                runCatching { Money.fromReais(value) }.getOrNull()?.let {
                    onSettingsChanged(settings.copy(electricityCostPerHour = it))
                }
            },
            label = { Text("Custo da energia por hora") },
            modifier = Modifier.fillMaxWidth().testTag("settings_electricity_rate")
        )
        var goal by remember(settings.monthlyRevenueGoal) {
            mutableStateOf(settings.monthlyRevenueGoal.toBigDecimal().toPlainString())
        }
        OutlinedTextField(
            value = goal,
            onValueChange = { value ->
                goal = value
                runCatching { Money.fromReais(value) }.getOrNull()?.let {
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
            Text("Sons de conquista")
            Switch(
                checked = settings.soundsEnabled,
                onCheckedChange = { onSettingsChanged(settings.copy(soundsEnabled = it)) },
                modifier = Modifier.testTag("settings_sounds")
            )
        }
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
        Text(
            "Exporte um arquivo portátil com banco, histórico e configurações. " +
                "A restauração valida versão e integridade antes de substituir os dados.",
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = onExport,
            modifier = Modifier.fillMaxWidth().testTag("backup_export")
        ) { Text("EXPORTAR BACKUP") }
        OutlinedButton(
            onClick = onRestore,
            modifier = Modifier.fillMaxWidth().testTag("backup_restore")
        ) { Text("RESTAURAR BACKUP") }
    }
}
