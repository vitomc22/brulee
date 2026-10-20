package com.bruleetelie.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.bruleetelie.app.ui.components.TactileSecondaryButton
import com.bruleetelie.app.ui.components.BruleeMascot
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.TactilePrimaryButton

@Composable
fun OcrCaptureScreen(
    onProcessText: (String) -> Unit,
    onScanRequested: () -> Unit = {},
    onPdfRequested: () -> Unit = {},
    processing: Boolean = false
) {
    var rawText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Importar Nota Fiscal", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Cole o texto reconhecido pelo scanner ou por um PDF para revisar os itens antes de atualizar preços.",
            style = MaterialTheme.typography.bodyMedium
        )
        TactilePrimaryButton(
            text = "ESCANEAR NOTA",
            enabled = !processing,
            onClick = onScanRequested,
            modifier = Modifier.testTag("ocr_scan_document")
        )
        TactileSecondaryButton("IMPORTAR PDF", onPdfRequested, Modifier.testTag("ocr_import_pdf"), enabled = !processing)
        if (processing) {
            BruleeMascot(milestone = true)
            Text("Lendo o documento…", Modifier.testTag("ocr_processing"))
            androidx.compose.material3.LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        OutlinedTextField(
            value = rawText,
            onValueChange = { rawText = it },
            label = { Text("Texto reconhecido (OCR)") },
            minLines = 10,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("ocr_raw_text")
        )
        TactilePrimaryButton(
            text = "PROCESSAR DOCUMENTO",
            enabled = rawText.isNotBlank() && !processing,
            onClick = { onProcessText(rawText) },
            modifier = Modifier.testTag("ocr_process_document")
        )
    }
}
