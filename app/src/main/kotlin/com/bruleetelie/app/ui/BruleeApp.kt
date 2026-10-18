package com.bruleetelie.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import com.bruleetelie.app.widget.ProductionWidgetProvider
import com.bruleetelie.app.backup.AppBackupManager
import com.bruleetelie.app.analytics.AnalyticsJsonExporter
import com.bruleetelie.app.ui.screens.*
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.data.BruleeRepository
import com.bruleetelie.data.datastore.OperatingSettingsDataStore
import com.bruleetelie.data.db.BruleeDatabase
import com.bruleetelie.documents.DefaultDocumentGateway
import com.bruleetelie.documents.ocr.ConfidenceLevel
import com.bruleetelie.documents.ocr.ParsedReceiptItem
import com.bruleetelie.documents.ocr.ReceiptParser
import com.bruleetelie.domain.CostComparisonResult
import com.bruleetelie.domain.PricingEngine
import com.bruleetelie.domain.ProductionOrder
import com.bruleetelie.domain.Money
import com.bruleetelie.domain.AchievementEngine
import java.math.BigDecimal
import java.math.RoundingMode
import kotlinx.coroutines.launch

private enum class Destination(val label: String, val icon: String) {
    Production("Produção", "▤"),
    Wizard("Novo Orçamento", "➕"),
    Finance("Finanças", "▣"),
    Stock("Estoque", "📷"),
    Backup("Backup", "↥"),
    Shopping("Compras", "🛒")
}

private sealed interface ActiveFlow {
    data object None : ActiveFlow
    data class OrderCompleted(val order: ProductionOrder, val comparison: CostComparisonResult) : ActiveFlow
}

@Composable
fun BruleeApp() {
    val activityContext = LocalContext.current
    val context = activityContext.applicationContext
    val repository = remember(context) {
        val db = BruleeDatabase.getInstance(context)
        val ds = OperatingSettingsDataStore(context)
        BruleeRepository(database = db, settingsDataStore = ds)
    }
    val documentGateway = remember { DefaultDocumentGateway() }
    val receiptParser = remember { ReceiptParser() }

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
                snackbarHostState.showSnackbar("Dados exportados para a interface Web")
            }.onFailure {
                snackbarHostState.showSnackbar("Falha ao exportar dados: ${it.message}")
            }
        }
    }

    var destination by remember { mutableStateOf(Destination.Production) }
    var activeFlow by remember { mutableStateOf<ActiveFlow>(ActiveFlow.None) }
    var ocrItems by remember { mutableStateOf<List<OcrItem>?>(null) }

    LaunchedEffect(orders, sales) {
        ProductionWidgetProvider.updateSnapshot(context, orders, sales)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { BruleeTopBar(destination) },
        bottomBar = {
            if (activeFlow == ActiveFlow.None) {
                NavigationBar(
                    containerColor = BruleeSurfaceContainerLowest,
                    tonalElevation = 4.dp
                ) {
                    Destination.entries.forEach { item ->
                        val isSelected = destination == item
                        NavigationBarItem(
                            modifier = Modifier.testTag("nav_${item.name.lowercase()}"),
                            selected = isSelected,
                            onClick = { destination = item },
                            icon = {
                                Text(
                                    item.icon,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = if (isSelected) BruleePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            label = {
                                Text(
                                    item.label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = BruleePrimaryFixed,
                                selectedIconColor = BruleePrimary,
                                selectedTextColor = BruleePrimary
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val flow = activeFlow) {
                is ActiveFlow.OrderCompleted -> {
                    ProductionCompletionScreen(
                        productName = flow.order.productName,
                        comparison = flow.comparison,
                        salesStreak = AchievementEngine.salesStreak(sales),
                        profitableStreak = AchievementEngine.profitableSalesStreak(sales),
                        soundsEnabled = settings.soundsEnabled,
                        reducedMotion = settings.reducedMotion,
                        onContinue = { activeFlow = ActiveFlow.None }
                    )
                }
                ActiveFlow.None -> {
                    when (destination) {
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
                                    activeFlow = ActiveFlow.OrderCompleted(order, comparison)
                                },
                                onNewQuoteClicked = { destination = Destination.Wizard }
                            )
                        }

                        Destination.Wizard -> {
                            NewQuoteWizardScreen(
                                operatingSettings = settings,
                                onQuoteApproved = { quote ->
                                    repository.addQuote(quote)
                                    repository.approveQuote(quote.id)
                                    destination = Destination.Production
                                },
                                onSendWhatsApp = { quote ->
                                    repository.addQuote(quote)
                                },
                                onClose = { destination = Destination.Production }
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
                                onExportPdf = { exportAnalytics.launch("brulee-telie-analytics.json") }
                            )
                        }

                        Destination.Stock -> {
                            val itemsToReview = ocrItems
                            if (itemsToReview == null) {
                                OcrCaptureScreen(
                                    onProcessText = { rawText ->
                                        ocrItems = receiptParser.parse(rawText).items.mapIndexed { index, item ->
                                            item.toOcrItem(index + 1)
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
                                    ocrItems = null
                                    destination = Destination.Production
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
