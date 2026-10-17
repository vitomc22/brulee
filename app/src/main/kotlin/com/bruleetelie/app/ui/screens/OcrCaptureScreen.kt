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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.TactilePrimaryButton

const val SAMPLE_RECEIPT_OCR_TEXT = """SUPERMERCADO DOCE AÇÚCAR
CNPJ: 12.345.678/0001-90
DATA: 15/10/2026
1 CHOC NOBRE LEITE 1KG R$ 34,90
2 LEITE CONDENS MOCA TP 395G R$ 7,49
3 MANTEIGA EXTRA S SAL 200G R$ 11,89
4 CACAU PO 100% 500G R$ 29,90
TOTAL R$ 84,18"""

@Composable
fun OcrCaptureScreen(onProcessText: (String) -> Unit) {
    var rawText by remember { mutableStateOf(SAMPLE_RECEIPT_OCR_TEXT) }

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
            enabled = rawText.isNotBlank(),
            onClick = { onProcessText(rawText) },
            modifier = Modifier.testTag("ocr_process_document")
        )
    }
}
