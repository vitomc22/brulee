package com.bruleetelie.app.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import com.bruleetelie.app.ui.components.BruleeIcon
import com.bruleetelie.app.ui.components.BruleeSymbol
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import java.util.Locale

internal val DeliveryDateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.forLanguageTag("pt-BR"))
    .withResolverStyle(ResolverStyle.STRICT)

internal fun parseDeliveryDate(value: String): LocalDate? = runCatching {
    // Retain delivery times on historical quotes and on manually entered dates.
    if (value.length > 10) LocalDateTime.parse(value, DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
        .withResolverStyle(ResolverStyle.STRICT)).toLocalDate()
    else LocalDate.parse(value, DeliveryDateFormat)
}.getOrNull()

internal fun isValidDeliveryDate(value: String, today: LocalDate = LocalDate.now()): Boolean =
    parseDeliveryDate(value)?.let { !it.isBefore(today) } == true

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DeliveryDateField(value: String, onValueChange: (String) -> Unit) {
    var pickerOpen by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(value, onValueChange, label = { Text("Data de entrega") },
        placeholder = { Text("dd/mm/aaaa") }, singleLine = true,
        isError = value.isNotBlank() && !isValidDeliveryDate(value),
        supportingText = { if (value.isNotBlank() && !isValidDeliveryDate(value)) Text("Escolha hoje ou uma data futura válida") },
        trailingIcon = { IconButton({ pickerOpen = true }, Modifier.testTag("delivery_date_picker")) {
            BruleeIcon(BruleeSymbol.Calendar, Modifier.semantics { contentDescription = "Selecionar data de entrega" })
        } }, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().testTag("delivery_date"))
    if (pickerOpen) {
        val today = LocalDate.now()
        val picker = rememberDatePickerState(
            initialSelectedDateMillis = parseDeliveryDate(value)?.takeUnless { it.isBefore(today) }
                ?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli(),
            selectableDates = remember(today) { object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) =
                    !Instant.ofEpochMilli(utcTimeMillis).atOffset(ZoneOffset.UTC).toLocalDate().isBefore(today)
                override fun isSelectableYear(year: Int) = year >= today.year
            } })
        val selected = picker.selectedDateMillis?.let { Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC).toLocalDate() }
        DatePickerDialog(onDismissRequest = { pickerOpen = false },
            confirmButton = { TextButton({
                selected?.let { onValueChange(it.format(DeliveryDateFormat)) }; pickerOpen = false
            }, enabled = selected != null && !selected.isBefore(LocalDate.now()), modifier = Modifier.testTag("delivery_date_confirm")) { Text("Confirmar") } },
            dismissButton = { TextButton({ pickerOpen = false }, Modifier.testTag("delivery_date_cancel")) { Text("Cancelar") } }
        ) { DatePicker(picker, modifier = Modifier.testTag("delivery_date_calendar")) }
    }
}
