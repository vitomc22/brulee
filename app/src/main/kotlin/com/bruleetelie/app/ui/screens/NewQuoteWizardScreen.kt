package com.bruleetelie.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.Saver
import androidx.activity.compose.BackHandler
import com.bruleetelie.app.ui.components.BruleeTransition
import com.bruleetelie.app.ui.components.BruleeMascot
import java.util.UUID
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.StatusPill
import com.bruleetelie.app.ui.components.StepProgressBar
import com.bruleetelie.app.ui.components.TactilePrimaryButton
import com.bruleetelie.app.ui.components.TactileSecondaryButton
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.domain.*
import java.math.BigDecimal

private enum class WizardStep {
    RecipeName,
    Ingredients,
    OperationalCosts,
    CostSummaryAndPricing,
    CustomerAndQuote,
    SentSuccess
}

@Composable
fun NewQuoteWizardScreen(
    operatingSettings: OperatingSettings,
    onQuoteApproved: (Quote) -> Unit,
    onSendWhatsApp: (Quote) -> Unit,
    onSharePdf: (Quote) -> Unit,
    onClose: () -> Unit,
    savedRecipes: List<SavedRecipe> = emptyList(),
    onSaveRecipe: (SavedRecipe) -> Unit = {},
    onSaveQuote: (Quote) -> Unit = {},
    whatsAppOpened: Boolean = false
) {
    var step by rememberSaveable { mutableStateOf(WizardStep.RecipeName) }
    var recipeId by rememberSaveable { mutableStateOf(UUID.randomUUID().toString()) }
    var quoteId by rememberSaveable { mutableStateOf(UUID.randomUUID().toString()) }
    val quoteCode = remember(quoteId) { "#ORC-${quoteId.take(8).uppercase()}" }
    var recipeName by rememberSaveable { mutableStateOf("") }
    var recipeYieldText by rememberSaveable { mutableStateOf("1") }
    val recipeYield = recipeYieldText.toIntOrNull()?.takeIf { it > 0 } ?: 1
    var ingredients by rememberSaveable(stateSaver = IngredientListSaver) { mutableStateOf(emptyList<RecipeIngredientItem>()) }
    var ingredientName by rememberSaveable { mutableStateOf("") }
    var packageQuantity by rememberSaveable { mutableStateOf("") }
    var packagePrice by rememberSaveable { mutableStateOf("") }
    var usedQuantity by rememberSaveable { mutableStateOf("") }
    var packageUnit by rememberSaveable { mutableStateOf(UnitOfMeasure.Gram) }
    var usedUnit by rememberSaveable { mutableStateOf(UnitOfMeasure.Gram) }
    var editingIngredientId by rememberSaveable { mutableStateOf<String?>(null) }

    var times by rememberSaveable(stateSaver = TimesSaver) {
        mutableStateOf(OperationalTimes(Quantity.minutes(0), Quantity.minutes(0), Quantity.minutes(0)))
    }
    var prepText by rememberSaveable { mutableStateOf("0") }
    var cookText by rememberSaveable { mutableStateOf("0") }
    var decorationText by rememberSaveable { mutableStateOf("0") }
    var packagingText by rememberSaveable { mutableStateOf("0") }
    var overheadText by rememberSaveable { mutableStateOf("0") }
    val packagingCost = runCatching { Money.fromReais(packagingText) }.getOrDefault(Money.Zero)
    val overheadCost = runCatching { Money.fromReais(overheadText) }.getOrDefault(Money.Zero)
    // Recalculate costs dynamically
    val calculatedCosts = remember(ingredients, times, packagingCost, overheadCost, operatingSettings) {
        RecipeCostCalculator.calculate(
            ingredients = ingredients,
            times = times,
            settings = operatingSettings,
            packaging = packagingCost,
            overhead = overheadCost
        )
    }

    // Interactive sale price
    var customPriceCents by rememberSaveable { mutableStateOf<Long?>(null) }
    val customSalePrice = customPriceCents?.let(::Money)
    var marginValue by rememberSaveable { mutableStateOf(operatingSettings.defaultMarginPercent.toPlainString()) }
    val targetMarginPercent = marginValue.toBigDecimal()
    val pricingResult = remember(calculatedCosts, customSalePrice, targetMarginPercent) {
        if (customSalePrice != null) {
            PricingEngine.fromSalePrice(calculatedCosts, customSalePrice!!)
        } else {
            PricingEngine.fromTargetMargin(calculatedCosts, targetMarginPercent)
        }
    }

    // Customer details
    var customerName by rememberSaveable { mutableStateOf("") }
    var customerPhone by rememberSaveable { mutableStateOf("") }
    var deliveryDate by rememberSaveable { mutableStateOf("") }

    fun currentQuote() = Quote(
        id = quoteId, code = quoteCode,
        customer = Customer("customer-$quoteId", customerName.trim(), customerPhone.trim()),
        productName = recipeName.trim(), productSubtitle = "Produção artesanal",
        unitsCount = recipeYield, pricing = pricingResult, deliveryDate = deliveryDate.trim()
    )
    fun saveRecipe() = onSaveRecipe(SavedRecipe(recipeId, "", 0, recipeName.trim(), recipeYield, ingredients, times, packagingCost, overheadCost))
    LaunchedEffect(whatsAppOpened) { if (whatsAppOpened) step = WizardStep.SentSuccess }
    fun goBack() {
        step = WizardStep.entries[(step.ordinal - 1).coerceAtLeast(0)]
    }
    BackHandler { if (step == WizardStep.RecipeName || step == WizardStep.SentSuccess) onClose() else goBack() }
    val scroll = rememberScrollState()
    LaunchedEffect(step) { scroll.scrollTo(0) }
    val currentStepNumber = when (step) {
        WizardStep.RecipeName -> 1
        WizardStep.Ingredients -> 2
        WizardStep.OperationalCosts -> 3
        WizardStep.CostSummaryAndPricing -> 4
        WizardStep.CustomerAndQuote -> 5
        WizardStep.SentSuccess -> 5
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
                TextButton(onClose, Modifier.testTag("wizard_close")) { Text("Fechar") }
        if (step != WizardStep.RecipeName && step != WizardStep.SentSuccess) {
            TextButton(
                onClick = {
                    step = when (step) {
                        WizardStep.Ingredients -> WizardStep.RecipeName
                        WizardStep.OperationalCosts -> WizardStep.Ingredients
                        WizardStep.CostSummaryAndPricing -> WizardStep.OperationalCosts
                        WizardStep.CustomerAndQuote -> WizardStep.CostSummaryAndPricing
                        else -> step
                    }
                },
                modifier = Modifier.testTag("wizard_back")
            ) { Text("← Voltar") }
        }
        // Progress bar (Duolingo style)
        if (step != WizardStep.SentSuccess) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BruleeSurfaceContainerLowest,
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 1.dp
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BruleeMascot(milestone = currentStepNumber, size = 54.dp)
                    Text(when (step) {
                        WizardStep.RecipeName -> "Vamos criar algo delicioso?"
                        WizardStep.Ingredients -> "Cada ingrediente conta!"
                        WizardStep.OperationalCosts -> "Seu tempo também tem valor."
                        WizardStep.CostSummaryAndPricing -> "Agora, um preço que valoriza você."
                        else -> "Falta pouco para o próximo pedido!"
                    }, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                }
                StepProgressBar(
                    currentStep = currentStepNumber,
                    totalSteps = 5,
                    modifier = Modifier.fillMaxWidth()
                )
                }
            }
        }

        BruleeTransition(step, Modifier.testTag("wizard_step_transition")) { displayedStep ->
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        when (displayedStep) {
            WizardStep.RecipeName -> {
                Text("Qual receita você vai orçar?", style = MaterialTheme.typography.headlineMedium)
                OutlinedTextField(
                    value = recipeName,
                    onValueChange = { recipeName = it },
                    label = { Text("Nome da Receita") },
                    modifier = Modifier.fillMaxWidth().testTag("recipe_name"),
                    shape = CircleShape
                )
                OutlinedTextField(
                    value = recipeYieldText, onValueChange = { recipeYieldText = it },
                    label = { Text("Rendimento (unidades)") },
                    modifier = Modifier.fillMaxWidth().testTag("recipe_yield"), singleLine = true
                )
                if (savedRecipes.isNotEmpty()) {
                    Text("Usar uma receita salva", style = MaterialTheme.typography.titleMedium)
                    savedRecipes.forEach { recipe ->
                        OutlinedButton(onClick = {
                            recipeId = recipe.id; recipeName = recipe.name; recipeYieldText = recipe.yieldCount.toString()
                            ingredients = recipe.ingredients; times = recipe.times
                            prepText = times.prepTime.value.toPlainString(); cookText = times.cookTime.value.toPlainString(); decorationText = times.decorationTime.value.toPlainString()
                            packagingText = recipe.packaging.toBigDecimal().toPlainString()
                            overheadText = recipe.overhead.toBigDecimal().toPlainString()
                        }, modifier = Modifier.fillMaxWidth().testTag("recipe_load_${recipe.id}")) { Text("${recipe.name} • versão ${recipe.version}") }
                    }
                }
                TactilePrimaryButton(
                    text = "CONTINUAR",
                    enabled = recipeName.isNotBlank() && (recipeYieldText.toIntOrNull() ?: 0) > 0,
                    onClick = { step = WizardStep.Ingredients },
                    modifier = Modifier.testTag("recipe_continue")
                )
            }

            WizardStep.Ingredients -> {
                Text("Ingredientes da Receita", style = MaterialTheme.typography.headlineMedium)
                OutlinedTextField(
                    value = ingredientName,
                    onValueChange = { ingredientName = it },
                    label = { Text("Nome do ingrediente") },
                    modifier = Modifier.fillMaxWidth().testTag("ingredient_name")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = packageQuantity,
                        onValueChange = { packageQuantity = it },
                        label = { Text("Qtd. do pacote") },
                        modifier = Modifier.weight(1f).testTag("ingredient_package_quantity")
                    )
                    OutlinedTextField(
                        value = packagePrice,
                        onValueChange = { packagePrice = it },
                        label = { Text("Preço pacote") },
                        modifier = Modifier.weight(1f).testTag("ingredient_package_price")
                    )
                }
                Text("Unidade da compra", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(UnitOfMeasure.Gram, UnitOfMeasure.Kilogram, UnitOfMeasure.Milliliter, UnitOfMeasure.Liter, UnitOfMeasure.Unit).forEach { unit ->
                        FilterChip(
                            selected = packageUnit == unit,
                            onClick = { packageUnit = unit },
                            label = { Text(unit.symbol) },
                            modifier = Modifier.testTag("package_unit_${unit.symbol.lowercase()}")
                        )
                    }
                }
                OutlinedTextField(
                    value = usedQuantity,
                    onValueChange = { usedQuantity = it },
                    label = { Text("Quantidade usada") },
                    modifier = Modifier.fillMaxWidth().testTag("ingredient_used_quantity")
                )
                Text("Unidade usada", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(UnitOfMeasure.Gram, UnitOfMeasure.Kilogram, UnitOfMeasure.Milliliter, UnitOfMeasure.Liter, UnitOfMeasure.Unit).forEach { unit ->
                        FilterChip(
                            selected = usedUnit == unit,
                            onClick = { usedUnit = unit },
                            label = { Text(unit.symbol) },
                            modifier = Modifier.testTag("used_unit_${unit.symbol.lowercase()}")
                        )
                    }
                }
                val parsedPackageQuantity = packageQuantity.replace(',', '.').toBigDecimalOrNull()
                val parsedPackagePrice = packagePrice.replace(',', '.').toBigDecimalOrNull()
                val parsedUsedQuantity = usedQuantity.replace(',', '.').toBigDecimalOrNull()
                val canAddIngredient = ingredientName.isNotBlank() &&
                    parsedPackageQuantity?.signum() == 1 &&
                    parsedPackagePrice?.signum() == 1 &&
                    parsedUsedQuantity?.signum() == 1 &&
                    packageUnit.dimension == usedUnit.dimension
                val duplicateIngredient = ingredients.any { it.name.equals(ingredientName.trim(), ignoreCase = true) && it.id != editingIngredientId }
                if (ingredientName.isNotBlank() && (parsedPackagePrice == null || parsedPackagePrice.signum() <= 0)) {
                    Text("Informe um preço de compra válido para calcular o custo.", modifier = Modifier.testTag("ingredient_validation"), color = MaterialTheme.colorScheme.error)
                } else if (duplicateIngredient) {
                    Text("Ingrediente já adicionado. Edite a linha existente ou use outro nome.", modifier = Modifier.testTag("ingredient_duplicate"), color = MaterialTheme.colorScheme.error)
                }
                TactileSecondaryButton(
                    text = if (editingIngredientId == null) "ADICIONAR INGREDIENTE" else "SALVAR INGREDIENTE",
                    enabled = canAddIngredient && !duplicateIngredient,
                    modifier = Modifier.testTag("ingredient_add"),
                    onClick = {
                        val updated = RecipeIngredientItem(
                            id = editingIngredientId ?: "ingredient-${System.currentTimeMillis()}",
                            name = ingredientName.trim(),
                            packageQuantity = Quantity(parsedPackageQuantity!!, packageUnit),
                            packagePrice = Money.fromReais(packagePrice),
                            usedQuantity = Quantity(parsedUsedQuantity!!, usedUnit)
                        )
                        val existingIndex = ingredients.indexOfFirst { it.id == editingIngredientId }
                        ingredients = if (existingIndex >= 0) ingredients.mapIndexed { i, item -> if (i == existingIndex) updated else item } else ingredients + updated
                        editingIngredientId = null
                        ingredientName = ""
                        packageQuantity = ""
                        packagePrice = ""
                        usedQuantity = ""
                        packageUnit = UnitOfMeasure.Gram
                        usedUnit = UnitOfMeasure.Gram
                    }
                )
                if (editingIngredientId != null) {
                    TextButton(
                        onClick = {
                            editingIngredientId = null
                            ingredientName = ""
                            packageQuantity = ""
                            packagePrice = ""
                            usedQuantity = ""
                        },
                        modifier = Modifier.testTag("ingredient_cancel_edit")
                    ) { Text("Cancelar edição") }
                }
                ingredients.forEach { item ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = BruleeSurfaceContainerLowest,
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(item.name, fontWeight = FontWeight.Bold)
                                Text(
                                    "Uso: ${item.usedQuantity.formatted()} (Pacote: ${item.packageQuantity.formatted()})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                item.proportionalCost.format(),
                                fontWeight = FontWeight.Bold,
                                color = BruleePrimary
                            )
                            TextButton(
                                onClick = { ingredients = ingredients.filterNot { it.id == item.id } },
                                modifier = Modifier.testTag("ingredient_remove")
                            ) { Text("Excluir") }
                            TextButton(
                                onClick = {
                                    ingredientName = item.name
                                    packageQuantity = item.packageQuantity.value.stripTrailingZeros().toPlainString()
                                    packagePrice = item.packagePrice.toBigDecimal().toPlainString()
                                    usedQuantity = item.usedQuantity.value.stripTrailingZeros().toPlainString()
                                    packageUnit = item.packageQuantity.unit
                                    usedUnit = item.usedQuantity.unit
                                    editingIngredientId = item.id
                                },
                                modifier = Modifier.testTag("ingredient_edit")
                            ) { Text("Editar") }
                        }
                    }
                }
                TactilePrimaryButton(
                    text = "CONTINUAR PARA CUSTOS OPERACIONAIS",
                    enabled = ingredients.isNotEmpty(),
                    onClick = { step = WizardStep.OperationalCosts },
                    modifier = Modifier.testTag("ingredients_continue")
                )
            }

            WizardStep.OperationalCosts -> {
                Text("Tempos e Complexidade", style = MaterialTheme.typography.headlineMedium)
                OutlinedTextField(
                    value = prepText,
                    onValueChange = { value ->
                        prepText = value
                        value.replace(',', '.').toBigDecimalOrNull()?.takeIf { it.signum() >= 0 }?.let {
                            times = times.copy(prepTime = Quantity.minutes(it))
                        }
                    },
                    label = { Text("Preparo ativo (minutos)") },
                    modifier = Modifier.fillMaxWidth().testTag("operational_prep_minutes")
                )
                OutlinedTextField(
                    value = cookText,
                    onValueChange = { value ->
                        cookText = value
                        value.replace(',', '.').toBigDecimalOrNull()?.takeIf { it.signum() >= 0 }?.let {
                            times = times.copy(cookTime = Quantity.minutes(it))
                        }
                    },
                    label = { Text("Cocção / forno (minutos)") },
                    modifier = Modifier.fillMaxWidth().testTag("operational_cook_minutes")
                )
                OutlinedTextField(
                    value = decorationText,
                    onValueChange = { value ->
                        decorationText = value
                        value.replace(',', '.').toBigDecimalOrNull()?.takeIf { it.signum() >= 0 }?.let {
                            times = times.copy(decorationTime = Quantity.minutes(it))
                        }
                    },
                    label = { Text("Decoração ativa (minutos)") },
                    modifier = Modifier.fillMaxWidth().testTag("operational_decoration_minutes")
                )
                Text("Fonte de energia", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EnergySource.entries.forEach { source ->
                        FilterChip(
                            selected = times.energySource == source,
                            onClick = { times = times.copy(energySource = source) },
                            label = { Text(source.label) },
                            modifier = Modifier.weight(1f).testTag("energy_${source.name.lowercase()}")
                        )
                    }
                }
                // Complexity Selection
                Text("Complexidade da Produção", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Complexity.entries.forEach { c ->
                        FilterChip(
                            selected = times.complexity == c,
                            onClick = { times = times.copy(complexity = c) },
                            label = { Text(c.label) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                OutlinedTextField(packagingText, { packagingText = it }, label = { Text("Embalagem (R$)") }, modifier = Modifier.fillMaxWidth().testTag("operational_packaging"))
                OutlinedTextField(overheadText, { overheadText = it }, label = { Text("Outros custos (R$)") }, modifier = Modifier.fillMaxWidth().testTag("operational_overhead"))
                TactilePrimaryButton(
                    text = "CALCULAR CUSTO TOTAL",
                    enabled = listOf(prepText, cookText, decorationText).all { it.replace(',', '.').toBigDecimalOrNull()?.signum()?.let { sign -> sign >= 0 } == true } && listOf(packagingText, overheadText).all { runCatching { Money.fromReais(it) >= Money.Zero }.getOrDefault(false) },
                    onClick = { saveRecipe(); step = WizardStep.CostSummaryAndPricing },
                    modifier = Modifier.testTag("costs_calculate")
                )
            }

            WizardStep.CostSummaryAndPricing -> {
                CostSummaryAndPricingView(
                    recipeName = recipeName,
                    yieldCount = recipeYield,
                    costs = calculatedCosts,
                    pricing = pricingResult,
                    targetMargin = targetMarginPercent,
                    onPriceChanged = { customPriceCents = it.cents },
                    onMarginChanged = {
                        marginValue = it.toPlainString()
                        customPriceCents = null
                    },
                    onAdvance = { step = WizardStep.CustomerAndQuote }
                )
            }

            WizardStep.CustomerAndQuote -> {
                CustomerAndQuoteView(
                    recipeName = recipeName,
                    quoteCode = quoteCode,
                    customerName = customerName,
                    onCustomerNameChanged = { customerName = it },
                    customerPhone = customerPhone,
                    onCustomerPhoneChanged = { customerPhone = it },
                    deliveryDate = deliveryDate,
                    onDeliveryDateChanged = { deliveryDate = it },
                    pricing = pricingResult,
                    onSendWhatsApp = { onSendWhatsApp(currentQuote()) },
                    onSharePdf = { onSharePdf(currentQuote()) },
                    onSaveDraft = { onSaveQuote(currentQuote()); onClose() },
                    onApproveOrder = { onQuoteApproved(currentQuote()) }
                )
            }

            WizardStep.SentSuccess -> {
                SentSuccessView(
                    customerName = customerName,
                    onFinish = onClose
                )
            }
        }
    }
    }
    }
}

@Composable
private fun CostSummaryAndPricingView(
    recipeName: String,
    yieldCount: Int,
    costs: RecipeCosts,
    pricing: PricingResult,
    targetMargin: BigDecimal,
    onPriceChanged: (Money) -> Unit,
    onMarginChanged: (BigDecimal) -> Unit,
    onAdvance: () -> Unit
) {
    var manualPriceText by rememberSaveable { mutableStateOf(pricing.salePrice.toBigDecimal().toPlainString()) }
    var marginText by rememberSaveable { mutableStateOf(targetMargin.setScale(2).toPlainString()) }
    var priceFocused by remember { mutableStateOf(false) }
    var marginFocused by remember { mutableStateOf(false) }
    LaunchedEffect(pricing.salePrice) { if (!priceFocused) manualPriceText = pricing.salePrice.toBigDecimal().toPlainString() }
    LaunchedEffect(targetMargin) { if (!marginFocused) marginText = targetMargin.setScale(2).toPlainString() }
    val validPrice = runCatching { Money.fromReais(manualPriceText) > Money.Zero }.getOrDefault(false)
    val validMargin = marginText.replace(',', '.').toBigDecimalOrNull()?.let { it >= BigDecimal.ZERO && it < BigDecimal(100) } == true
    // Mascot Narrative Banner
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cost_summary_screen"),
        color = BruleeSurfaceContainerLow,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(BruleeSecondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("🍰", style = MaterialTheme.typography.titleLarge)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Seu custo ficou assim ✨",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Cálculos para sua receita: $recipeName.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    // Detailed Cost Composition Breakdown
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "COMPOSIÇÃO DETALHADA",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = BruleeTertiary
            )
            Text(
                "Rend. $yieldCount unids",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        CostBreakdownRow("🍫", "Ingredientes selecionados", "Custo proporcional das quantidades informadas", costs.ingredients)
        CostBreakdownRow("👩‍🍳", "Mão de obra", "Tempo ativo × valor da hora × complexidade", costs.labor)
        CostBreakdownRow("🔥", "Gás e energia", "Custo proporcional de cocção", costs.energy)
        CostBreakdownRow("📦", "Embalagem e fita", "Valor informado para a receita", costs.packaging)
        CostBreakdownRow("＋", "Outros custos", "Despesas adicionais desta produção", costs.overhead)
    }

    // Total Cost Highlight Card
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BruleeSurfaceContainerLowest,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "CUSTO TOTAL DE PRODUÇÃO",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    costs.total.format(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            StatusPill("Base blindada", BruleePrimaryFixed, BruleePrimary)
        }
    }

    // Pricing & Interactive Margin Section
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BruleeSurfaceContainerLowest,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "PREÇO SUGERIDO",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        pricing.salePrice.format(),
                        modifier = Modifier.testTag("price_sale_value"),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = BruleePrimary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    StatusPill(
                        text = "Margem: ${pricing.marginPercent}%",
                        background = BruleeSecondaryContainer,
                        foreground = BruleeOnSecondaryContainer
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Lucro: ${pricing.profit.format()}",
                        modifier = Modifier.testTag("price_profit_value"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BruleePrimary
                    )
                }
            }

            Text(
                "Ajustar Preço de Venda (tempo real):",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = manualPriceText,
                onValueChange = { value ->
                    manualPriceText = value
                    runCatching { Money.fromReais(value) }.getOrNull()?.takeIf { it > Money.Zero }?.let(onPriceChanged)
                },
                label = { Text("Preço manual") },
                isError = !validPrice,
                modifier = Modifier.fillMaxWidth().testTag("price_manual_input").onFocusChanged { priceFocused = it.isFocused },
                singleLine = true
            )
            OutlinedTextField(
                value = marginText,
                onValueChange = { value ->
                    marginText = value
                    value.replace(',', '.').toBigDecimalOrNull()?.takeIf { it >= BigDecimal.ZERO && it < BigDecimal("100") }?.let(onMarginChanged)
                },
                label = { Text("Margem alvo (%)") },
                isError = !validMargin,
                modifier = Modifier.fillMaxWidth().testTag("price_margin_input").onFocusChanged { marginFocused = it.isFocused },
                singleLine = true,
                supportingText = { Text("Use um valor entre 0 e 99,99") }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onPriceChanged(pricing.salePrice - Money.fromReais("5.00")) },
                    enabled = pricing.salePrice > Money.fromReais("5.00"),
                    modifier = Modifier.weight(1f).testTag("price_decrease_5"),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = BruleeSurfaceContainerHigh, contentColor = BruleeOnSurface)
                ) { Text("- R$ 5") }

                Button(
                    onClick = { onPriceChanged(pricing.salePrice + Money.fromReais("5.00")) },
                    modifier = Modifier.weight(1f).testTag("price_increase_5"),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = BruleeSurfaceContainerHigh, contentColor = BruleeOnSurface)
                ) { Text("+ R$ 5") }

                Button(
                    onClick = { onPriceChanged(pricing.salePrice + Money.fromReais("10.00")) },
                    modifier = Modifier.weight(1f).testTag("price_increase_10"),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = BruleeSecondaryContainer, contentColor = BruleeOnSecondaryContainer)
                ) { Text("+ R$ 10") }
            }
        }
    }

    if (pricing.profit < Money.Zero) {
        Text("Atenção: este preço fica abaixo do custo de produção.", color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag("price_loss_warning"))
    }
    // CTA
    TactilePrimaryButton(
        text = "AVANÇAR PARA ORÇAMENTO",
        enabled = validPrice && validMargin,
        onClick = onAdvance,
        icon = "→",
        modifier = Modifier.testTag("quote_advance")
    )
}

@Composable
private fun CostBreakdownRow(icon: String, title: String, subtitle: String, amount: Money) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BruleeSurfaceContainerLowest,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(BruleeSurfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Text(icon)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(amount.format(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CustomerAndQuoteView(
    recipeName: String,
    quoteCode: String,
    customerName: String,
    onCustomerNameChanged: (String) -> Unit,
    customerPhone: String,
    onCustomerPhoneChanged: (String) -> Unit,
    deliveryDate: String,
    onDeliveryDateChanged: (String) -> Unit,
    pricing: PricingResult,
    onSendWhatsApp: () -> Unit,
    onSharePdf: () -> Unit,
    onApproveOrder: () -> Unit,
    onSaveDraft: () -> Unit
) {
    val validCustomer = customerName.isNotBlank() && deliveryDate.isNotBlank()
    val phoneDigits = customerPhone.filter(Char::isDigit)
    val validPhone = phoneDigits.length in 10..15
    Text(
        "Para quem é este orçamento?",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
    Text(
        "Vincule o orçamento ao cliente para gerar o PDF e enviar pelo WhatsApp.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    // Form Section
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BruleeSurfaceContainerLowest,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = customerName,
                onValueChange = onCustomerNameChanged,
                label = { Text("Nome do Cliente") },
                modifier = Modifier.fillMaxWidth().testTag("customer_name"),
                shape = CircleShape
            )

            OutlinedTextField(
                value = customerPhone,
                onValueChange = onCustomerPhoneChanged,
                label = { Text("Telefone / WhatsApp") },
                modifier = Modifier.fillMaxWidth().testTag("customer_phone"),
                shape = CircleShape
            )

            OutlinedTextField(
                value = deliveryDate,
                onValueChange = onDeliveryDateChanged,
                label = { Text("Data de Entrega Desejada") },
                modifier = Modifier.fillMaxWidth().testTag("delivery_date"),
                shape = CircleShape
            )
        }
    }

    // Mini-Preview of Customer Quotation Card (PDF Preview)
    Text(
        "Pré-Visualização do Cartão PDF (Visão do Cliente)",
        style = MaterialTheme.typography.labelMedium,
        color = BruleeTertiary,
        fontWeight = FontWeight.Bold
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BruleeSurfaceContainerLowest,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Brûlée Telie", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = BruleePrimary)
                    Text("CONFEITARIA ARTESANAL", style = MaterialTheme.typography.labelSmall, color = BruleeSecondary, fontWeight = FontWeight.Bold)
                }
                StatusPill(quoteCode, BruleePrimaryFixed, BruleePrimary)
            }

            HorizontalDivider(color = BruleeSurfaceContainerHigh)

            Text("Cliente: $customerName", fontWeight = FontWeight.SemiBold)
            Text(recipeName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Entrega: $deliveryDate", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("TOTAL DO ORÇAMENTO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(pricing.salePrice.format(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = BruleePrimary)
                }
                StatusPill("Válido por 3 dias", BruleeSecondaryFixed, BruleeSecondary)
            }

            Text(
                "🔒 Margens e custos internos ocultos do cliente",
                style = MaterialTheme.typography.labelSmall,
                color = BruleeTertiary
            )
        }
    }

    // Tactile Actions
    TactilePrimaryButton(
        text = "ENVIAR NO WHATSAPP",
        enabled = validCustomer && validPhone,
        modifier = Modifier.testTag("quote_send_whatsapp"),
        onClick = onSendWhatsApp,
        icon = "✉"
    )

    TactileSecondaryButton(
        text = "GERAR E COMPARTILHAR PDF",
        enabled = validCustomer,
        onClick = onSharePdf,
        icon = "▣",
        modifier = Modifier.testTag("quote_share_pdf")
    )

    TactileSecondaryButton(
        text = "Marcar como Aprovado (Adicionar à Produção)",
        enabled = validCustomer,
        onClick = onApproveOrder,
        icon = "✓",
        modifier = Modifier.testTag("quote_approve")
    )
    TactileSecondaryButton("SALVAR RASCUNHO", onSaveDraft, Modifier.testTag("quote_save_draft"), enabled = validCustomer)
}

@Composable
private fun SentSuccessView(customerName: String, onFinish: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BruleeSecondaryContainer,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("🎉", style = MaterialTheme.typography.displayMedium)
            Text(
                "Orçamento pronto no WhatsApp",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = BruleeOnSecondaryContainer
            )
            Text(
                "Confirme o envio para $customerName no WhatsApp. O orçamento ficou salvo em Pedidos.",
                style = MaterialTheme.typography.bodyMedium,
                color = BruleeOnSecondaryContainer
            )
            TactilePrimaryButton(
                text = "VER PEDIDOS",
                modifier = Modifier.testTag("quote_sent_finish"),
                onClick = onFinish
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewQuoteWizardScreenPreview() {
    BruleeTheme {
        NewQuoteWizardScreen(
            operatingSettings = OperatingSettings(),
            onQuoteApproved = {},
            onSendWhatsApp = {},
            onSharePdf = {},
            onClose = {}
        )
    }
}
