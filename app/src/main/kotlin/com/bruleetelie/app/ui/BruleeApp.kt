package com.bruleetelie.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBarItem
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.IntentSenderRequest
import androidx.hilt.navigation.compose.hiltViewModel
import android.app.Activity
import com.bruleetelie.app.widget.ProductionWidgetProvider
import com.bruleetelie.app.backup.AppBackupManager
import com.bruleetelie.app.analytics.AnalyticsJsonExporter
import com.bruleetelie.app.analytics.SalesCsvExporter
import com.bruleetelie.app.documents.MlKitDocumentScannerGateway
import com.bruleetelie.app.sharing.ShareFile
import com.bruleetelie.app.ui.screens.*
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.documents.DefaultDocumentGateway
import com.bruleetelie.documents.ocr.ConfidenceLevel
import com.bruleetelie.documents.ocr.ParsedReceiptItem
import com.bruleetelie.documents.ocr.ReceiptParser
import com.bruleetelie.documents.ocr.MlKitTextRecognitionGateway
import com.bruleetelie.documents.ocr.PdfPageRenderer
import com.bruleetelie.documents.pdf.DefaultPdfGenerator
import com.bruleetelie.documents.pdf.PdfTemplateType
import com.bruleetelie.domain.CostComparisonResult
import com.bruleetelie.domain.PricingEngine
import com.bruleetelie.domain.ProductionOrder
import com.bruleetelie.domain.Money
import com.bruleetelie.domain.AchievementEngine
import java.math.BigDecimal
import java.math.RoundingMode
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

@Composable
fun BruleeApp() {
    val activityContext = LocalContext.current
    val context = activityContext.applicationContext
    val isExpandedLayout = LocalConfiguration.current.screenWidthDp >= 840
    val isMediumLayout = LocalConfiguration.current.screenWidthDp >= 600
    val appViewModel: BruleeAppViewModel = hiltViewModel()
    val repository = appViewModel.repository
    val uiState by appViewModel.uiState.collectAsState()
    val documentGateway = remember { DefaultDocumentGateway() }
    val receiptParser = remember { ReceiptParser() }
    val scannerGateway = remember { MlKitDocumentScannerGateway() }
    val textRecognitionGateway = remember { MlKitTextRecognitionGateway() }
    val pdfPageRenderer = remember { PdfPageRenderer() }
    val pdfGenerator = remember { DefaultPdfGenerator() }

    val orders by repository.productionOrders.collectAsState()
    val sales by repository.completedSales.collectAsState()
    val settings by repository.settings.collectAsState()
    val ingredients by repository.ingredients.collectAsState()
    val priceObservations by repository.priceObservations.collectAsState()
    val quotes by repository.quotes.collectAsState()
    val shoppingItems by repository.shoppingItems.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val backupManager = remember(context) { AppBackupManager(context) }
    val exportBackup = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        if (uri != null) coroutineScope.launch {
            runCatching { backupManager.export(uri) }
                .onSuccess { snackbarHostState.showSnackbar("Backup exportado com sucesso") }
                .onFailure { snackbarHostState.showSnackbar("Falha ao exportar: ${it.message}") }
        }
    }
    val restoreBackup = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) coroutineScope.launch {
            runCatching { backupManager.restore(uri) }
                .onSuccess {
                    snackbarHostState.showSnackbar("Backup restaurado; recarregando aplicativo")
                    (activityContext as? Activity)?.recreate()
                }
                .onFailure { snackbarHostState.showSnackbar("Backup inválido: ${it.message}") }
        }
    }
    val exportAnalytics = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) coroutineScope.launch {
            runCatching {
                val json = AnalyticsJsonExporter.export(
                    sales = sales,
                    observations = priceObservations,
                    ingredientNames = ingredients.associate { it.id to it.name }
                )
                context.contentResolver.openOutputStream(uri, "wt")?.bufferedWriter()?.use { it.write(json) }
                    ?: error("Não foi possível abrir o arquivo")
            }.onSuccess {
                ShareFile.shareUri(context, uri, "application/json", "Compartilhar dados analíticos")
                snackbarHostState.showSnackbar("Dados exportados para a interface Web")
            }.onFailure {
                snackbarHostState.showSnackbar("Falha ao exportar dados: ${it.message}")
            }
        }
    }
    val exportSalesCsv = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        if (uri != null) coroutineScope.launch {
            runCatching {
                val csv = SalesCsvExporter.export(sales)
                context.contentResolver.openOutputStream(uri, "wt")?.bufferedWriter()?.use { it.write(csv) }
                    ?: error("Não foi possível abrir o arquivo")
            }.onSuccess {
                ShareFile.shareUri(context, uri, "text/csv", "Compartilhar vendas em CSV")
                snackbarHostState.showSnackbar("CSV exportado com sucesso")
            }.onFailure {
                snackbarHostState.showSnackbar("Falha ao exportar CSV: ${it.message}")
            }
        }
    }

    val scannerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            coroutineScope.launch {
                runCatching {
                    val scan = scannerGateway.parseResult(result.data)
                    val imageUris = if (scan.imageUris.isNotEmpty()) {
                        scan.imageUris
                    } else if (scan.pdfUri != null) {
                        withContext(Dispatchers.IO) {
                            val cache = File(context.cacheDir, "ocr-pdf-${System.currentTimeMillis()}")
                            pdfPageRenderer.renderToFiles(context, scan.pdfUri, cache)
                        }
                    } else {
                        emptyList()
                    }
                    require(imageUris.isNotEmpty()) { "Nenhuma página de imagem ou PDF foi capturada" }
                    val recognized = imageUris.mapIndexed { index, uri ->
                        textRecognitionGateway.recognize(context, uri, index).text
                    }.joinToString("\n")
                    receiptParser.parse(recognized).items.mapIndexed { index, item ->
                        item.toOcrItem(index + 1)
                    }
                }.onSuccess { appViewModel.setOcrItems(it) }
                    .onFailure { snackbarHostState.showSnackbar("Falha ao ler documento: ${it.message}") }
            }
        }
    }

    LaunchedEffect(orders, sales) {
        ProductionWidgetProvider.updateSnapshot(context, orders, sales)
    }

    val navBackStack = rememberSaveable { mutableStateListOf(uiState.destination.name) }
    LaunchedEffect(uiState.destination, uiState.activeFlow) {
        if (uiState.activeFlow == ActiveFlow.None && navBackStack.lastOrNull() != uiState.destination.name) {
            navBackStack.clear()
            navBackStack.add(uiState.destination.name)
        }
    }

    NavDisplay(
        backStack = navBackStack,
        onBack = {
            if (navBackStack.size > 1) {
                navBackStack.removeLastOrNull()
                navBackStack.lastOrNull()
                    ?.let { name -> Destination.entries.firstOrNull { it.name == name } }
                    ?.let(appViewModel::navigate)
            }
        },
        entryProvider = { key ->
            NavEntry(key) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                BruleeTopBar(
                    Destination.entries.firstOrNull { it.name == key } ?: uiState.destination
                )
            },
            bottomBar = {
                if (uiState.activeFlow == ActiveFlow.None) {
                    if (isMediumLayout || isExpandedLayout) {
                        NavigationRail(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(if (isExpandedLayout) "navigation_rail_expanded" else "navigation_rail_medium")
                        ) {
                            Destination.entries.forEach { item ->
                                val isSelected = uiState.destination == item
                                NavigationRailItem(
                                    modifier = Modifier
                                        .testTag("nav_${item.name.lowercase()}")
                                        .semantics { contentDescription = "Navegação ${item.label}" },
                                    selected = isSelected,
                                    onClick = { appViewModel.navigate(item) },
                                    icon = { Text(item.icon, style = MaterialTheme.typography.titleLarge) },
                                    label = { Text(item.label, style = MaterialTheme.typography.labelSmall) }
                                )
                            }
                        }
                    } else {
                        NavigationBar {
                            Destination.entries.forEach { item ->
                                val isSelected = uiState.destination == item
                                NavigationBarItem(
                                    modifier = Modifier
                                        .testTag("nav_${item.name.lowercase()}")
                                        .semantics { contentDescription = "Navegação ${item.label}" },
                                    selected = isSelected,
                                    onClick = { appViewModel.navigate(item) },
                                    icon = { Text(item.icon, style = MaterialTheme.typography.titleLarge) },
                                    label = { Text(item.label, style = MaterialTheme.typography.labelSmall) }
                                )
                            }
                        }
                    }
                }
            }
        ) { padding ->
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
            when (val flow = uiState.activeFlow) {
                is ActiveFlow.OrderCompleted -> {
                    ProductionCompletionScreen(
                        productName = flow.order.productName,
                        comparison = flow.comparison,
                        salesStreak = AchievementEngine.salesStreak(sales),
                        profitableStreak = AchievementEngine.profitableSalesStreak(sales),
                        soundsEnabled = settings.soundsEnabled,
                        reducedMotion = settings.reducedMotion,
                        onContinue = { appViewModel.closeActiveFlow() }
                    )
                }
                ActiveFlow.None -> {
                    when (uiState.destination) {
                        Destination.Production -> {
                            ProductionBoardScreen(
                                orders = orders,
                                onStepChecked = { orderId, stepId, isChecked ->
                                    repository.updateStepStatus(orderId, stepId, isChecked)
                                },
                                onFinalizeOrder = { order ->
                                    // Complete order with savings comparison
                                    val comparison = PricingEngine.compareEstimatedVsActual(
                                        estimated = order.estimatedCosts,
                                        actual = order.actualCosts ?: order.estimatedCosts,
                                        salePrice = order.salePrice
                                    )
                                    repository.completeOrder(order.id, order.actualCosts)
                                    appViewModel.showOrderCompleted(order, comparison)
                                },
                                onNewQuoteClicked = { appViewModel.navigate(Destination.Wizard) }
                            )
                        }

                        Destination.Wizard -> {
                            NewQuoteWizardScreen(
                                operatingSettings = settings,
                                onQuoteApproved = { quote ->
                                    repository.addQuote(quote)
                                    repository.approveQuote(quote.id)
                                    appViewModel.navigate(Destination.Production)
                                },
                                onSendWhatsApp = { quote ->
                                    repository.addQuote(quote)
                                },
                                onSharePdf = { quote ->
                                    coroutineScope.launch {
                                        runCatching {
                                            val directory = File(context.cacheDir, "shared")
                                            directory.mkdirs()
                                            val file = File(directory, "orcamento-${quote.id}.pdf")
                                            withContext(Dispatchers.IO) {
                                                pdfGenerator.writePdf(quote, file, PdfTemplateType.CUSTOMER)
                                            }
                                            ShareFile.shareUri(
                                                context,
                                                ShareFile.uriForFile(context, file),
                                                "application/pdf",
                                                "Compartilhar orçamento em PDF"
                                            )
                                        }.onFailure {
                                            snackbarHostState.showSnackbar("Falha ao gerar PDF: ${it.message}")
                                        }
                                    }
                                },
                                onClose = { appViewModel.navigate(Destination.Production) }
                            )
                        }

                        Destination.Finance -> {
                            FinanceDashboardScreen(
                                sales = sales,
                                quotes = quotes,
                                priceObservations = priceObservations,
                                ingredientNames = ingredients.associate { it.id to it.name },
                                fixedCosts = settings.monthlyFixedCosts,
                                revenueGoal = settings.monthlyRevenueGoal,
                                onExportCsv = { exportSalesCsv.launch("brulee-telie-vendas.csv") }
                            )
                        }

                        Destination.Stock -> {
                            val itemsToReview = uiState.ocrItems
                            if (itemsToReview == null) {
                                OcrCaptureScreen(
                                    onProcessText = { rawText ->
                                        appViewModel.setOcrItems(receiptParser.parse(rawText).items.mapIndexed { index, item ->
                                            item.toOcrItem(index + 1)
                                        })
                                    },
                                    onScanRequested = {
                                        val activity = activityContext as? Activity
                                        if (activity == null) {
                                            coroutineScope.launch { snackbarHostState.showSnackbar("Câmera indisponível neste contexto") }
                                        } else {
                                            scannerGateway.createScanIntent(activity)
                                                .addOnSuccessListener { intentSender ->
                                                    scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                                                }
                                                .addOnFailureListener { error ->
                                                    coroutineScope.launch { snackbarHostState.showSnackbar("Scanner indisponível: ${error.message}") }
                                                }
                                        }
                                    }
                                )
                            } else {
                                OcrReviewScreen(
                                    initialItems = itemsToReview,
                                    onSaveConfirmedItems = { confirmedItems ->
                                    confirmedItems.forEach { item ->
                                        val ingredient = ingredients.firstOrNull { it.name == item.matchedIngredient }
                                        if (ingredient != null) {
                                            repository.addPriceObservation(
                                                ingredientId = ingredient.id,
                                                supplierName = "Importação OCR",
                                                pkgValue = item.packageQuantityValue,
                                                pkgUnit = item.packageQuantityUnit,
                                                pkgPriceCents = item.packagePriceCents,
                                                unitPriceCents = item.normalizedUnitPriceCents,
                                                source = "OCR"
                                            )
                                        }
                                    }
                                    appViewModel.setOcrItems(null)
                                    appViewModel.navigate(Destination.Production)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "${confirmedItems.size} observações de preço salvas"
                                        )
                                    }
                                }
                                )
                            }
                        }

                        Destination.Backup -> {
                            BackupScreen(
                                settings = settings,
                                onSettingsChanged = repository::updateSettings,
                                onExport = { exportBackup.launch("brulee-telie-backup.zip") },
                                onRestore = { restoreBackup.launch(arrayOf("application/zip", "application/octet-stream")) }
                            )
                        }

                        Destination.Shopping -> {
                            ShoppingListScreen(
                                items = shoppingItems,
                                onAdd = { name, quantity -> repository.addShoppingItem(name, quantity) },
                                onPurchasedChanged = repository::setShoppingItemPurchased,
                                onDelete = repository::deleteShoppingItem
                            )
                        }
                    }
                }
            }
            }
        }
    }
    }
    )
}

private fun ParsedReceiptItem.toOcrItem(index: Int): OcrItem {
    val quantity = packageQuantityValue.toBigDecimal()
    val factor = if (packageQuantityUnit == "g" || packageQuantityUnit == "ml") {
        BigDecimal("1000")
    } else {
        BigDecimal.ONE
    }
    val normalizedPrice = BigDecimal(totalPriceCents)
        .multiply(factor)
        .divide(quantity, 0, RoundingMode.HALF_UP)
        .longValueExact()
    return OcrItem(
        id = index.toString(),
        rawName = rawDescription,
        matchedIngredient = matchedIngredientName ?: rawDescription,
        packageQuantityValue = packageQuantityValue,
        packageQuantityUnit = packageQuantityUnit,
        packagePriceCents = totalPriceCents,
        normalizedUnitPriceCents = normalizedPrice,
        priceFormatted = Money.fromCents(totalPriceCents).format(),
        confidence = when (confidence) {
            ConfidenceLevel.HIGH -> "Alta"
            ConfidenceLevel.MEDIUM -> "Média"
            ConfidenceLevel.LOW -> "Baixa"
        }
    )
}

@Composable
private fun BruleeTopBar(destination: Destination) {
    Surface(
        color = BruleeSurface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BruleeSecondaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text("♨", color = BruleeOnSecondaryContainer, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "BRÛLÉE TELIE",
                    style = MaterialTheme.typography.labelSmall,
                    color = BruleeSecondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    when (destination) {
                        Destination.Production -> "Production Board"
                        Destination.Wizard -> "Novo Orçamento"
                        Destination.Finance -> "Gestão Financeira"
                        Destination.Stock -> "Estoque & OCR"
                        Destination.Backup -> "Backup"
                        Destination.Shopping -> "Lista de Compras"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Box(
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(BruleePrimaryFixed),
                contentAlignment = Alignment.Center
            ) {
                Text("bt", color = BruleePrimary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}
