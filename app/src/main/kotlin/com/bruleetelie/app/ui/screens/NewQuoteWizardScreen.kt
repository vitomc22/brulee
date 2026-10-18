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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    onClose: () -> Unit
) {
    var step by remember { mutableStateOf(WizardStep.RecipeName) }

    // State variables for the wizard
    var recipeName by remember { mutableStateOf("Brigadeiro Gourmet Tradicional") }
    var recipeYield by remember { mutableIntStateOf(30) }

    // Ingredients
    val ingredients = remember {
        mutableStateListOf(
            RecipeIngredientItem(
                id = "1",
                name = "Chocolate Nobre 50%",
                packageQuantity = Quantity.kilograms(1),
                packagePrice = Money.fromReais("60.00"),
                usedQuantity = Quantity.grams(350)
            ),
            RecipeIngredientItem(
                id = "2",
                name = "Leite Condensado Moça",
                packageQuantity = Quantity.grams(395),
                packagePrice = Money.fromReais("7.50"),
                usedQuantity = Quantity.grams(395)
            ),
            RecipeIngredientItem(
                id = "3",
                name = "Manteiga Extra s/ Sal",
                packageQuantity = Quantity.grams(200),
                packagePrice = Money.fromReais("12.00"),
                usedQuantity = Quantity.grams(40)
            )
        )
    }
    var ingredientName by remember { mutableStateOf("") }
    var packageQuantity by remember { mutableStateOf("") }
    var packagePrice by remember { mutableStateOf("") }
    var usedQuantity by remember { mutableStateOf("") }

    var times by remember {
        mutableStateOf(
            OperationalTimes(
                prepTime = Quantity.minutes(60),
                cookTime = Quantity.minutes(30),
                decorationTime = Quantity.minutes(30),
                energySource = EnergySource.Gas,
                complexity = Complexity.Normal
            )
        )
    }

    var packagingCost by remember { mutableStateOf(Money.fromReais("4.50")) }

    // Recalculate costs dynamically
    val calculatedCosts = remember(ingredients.toList(), times, packagingCost) {
        RecipeCostCalculator.calculate(
            ingredients = ingredients,
            times = times,
            settings = operatingSettings,
            packaging = packagingCost
        )
    }

    // Interactive sale price
    var customSalePrice by remember { mutableStateOf<Money?>(null) }
    val pricingResult = remember(calculatedCosts, customSalePrice) {
        if (customSalePrice != null) {
            PricingEngine.fromSalePrice(calculatedCosts, customSalePrice!!)
        } else {
            PricingEngine.fromTargetMargin(calculatedCosts, operatingSettings.defaultMarginPercent)
        }
    }

    // Customer details
    var customerName by remember { mutableStateOf("Ana Souza") }
    var customerPhone by remember { mutableStateOf("(11) 98765-4321") }
    var deliveryDate by remember { mutableStateOf("Sábado, 14 de Outubro - 16:00") }

    val currentStepNumber = when (step) {
        WizardStep.RecipeName -> 1
        WizardStep.Ingredients -> 4
        WizardStep.OperationalCosts -> 7
        WizardStep.CostSummaryAndPricing -> 9
        WizardStep.CustomerAndQuote -> 10
        WizardStep.SentSuccess -> 10
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress bar (Duolingo style)
        if (step != WizardStep.SentSuccess) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BruleeSurfaceContainerLowest,
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 1.dp
            ) {
                StepProgressBar(
                    currentStep = currentStepNumber,
                    totalSteps = 10,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        when (step) {
            WizardStep.RecipeName -> {
                Text("Qual receita você vai orçar?", style = MaterialTheme.typography.headlineMedium)
                OutlinedTextField(
                    value = recipeName,
                    onValueChange = { recipeName = it },
                    label = { Text("Nome da Receita") },
                    modifier = Modifier.fillMaxWidth().testTag("recipe_name"),
                    shape = CircleShape
                )
                TactilePrimaryButton(
                    text = "CONTINUAR",
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
                        label = { Text("Qtd. pacote (g)") },
                        modifier = Modifier.weight(1f).testTag("ingredient_package_quantity")
                    )
                    OutlinedTextField(
                        value = packagePrice,
                        onValueChange = { packagePrice = it },
                        label = { Text("Preço pacote") },
                        modifier = Modifier.weight(1f).testTag("ingredient_package_price")
                    )
                }
                OutlinedTextField(
                    value = usedQuantity,
                    onValueChange = { usedQuantity = it },
                    label = { Text("Quantidade usada (g)") },
                    modifier = Modifier.fillMaxWidth().testTag("ingredient_used_quantity")
                )
                val canAddIngredient = ingredientName.isNotBlank() &&
                    packageQuantity.toBigDecimalOrNull()?.signum() == 1 &&
                    packagePrice.replace(',', '.').toBigDecimalOrNull()?.signum() == 1 &&
                    usedQuantity.toBigDecimalOrNull()?.signum() == 1
                TactileSecondaryButton(
                    text = "ADICIONAR INGREDIENTE",
                    enabled = canAddIngredient,
                    modifier = Modifier.testTag("ingredient_add"),
                    onClick = {
                        ingredients += RecipeIngredientItem(
                            id = "ingredient-${System.currentTimeMillis()}",
                            name = ingredientName.trim(),
                            packageQuantity = Quantity.grams(packageQuantity.toBigDecimal()),
                            packagePrice = Money.fromReais(packagePrice),
                            usedQuantity = Quantity.grams(usedQuantity.toBigDecimal())
                        )
                        ingredientName = ""
                        packageQuantity = ""
                        packagePrice = ""
                        usedQuantity = ""
                    }
                )
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
                        }
                    }
                }
                TactilePrimaryButton(
                    text = "CONTINUAR PARA CUSTOS OPERACIONAIS",
                    onClick = { step = WizardStep.OperationalCosts },
                    modifier = Modifier.testTag("ingredients_continue")
                )
            }

            WizardStep.OperationalCosts -> {
                Text("Tempos e Complexidade", style = MaterialTheme.typography.headlineMedium)
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
                TactilePrimaryButton(
                    text = "CALCULAR CUSTO TOTAL",
                    onClick = { step = WizardStep.CostSummaryAndPricing },
                    modifier = Modifier.testTag("costs_calculate")
                )
            }

            WizardStep.CostSummaryAndPricing -> {
                CostSummaryAndPricingView(
                    recipeName = recipeName,
                    yieldCount = recipeYield,
                    costs = calculatedCosts,
                    pricing = pricingResult,
                    onPriceChanged = { customSalePrice = it },
                    onAdvance = { step = WizardStep.CustomerAndQuote }
                )
            }

            WizardStep.CustomerAndQuote -> {
                CustomerAndQuoteView(
                    recipeName = recipeName,
                    customerName = customerName,
                    onCustomerNameChanged = { customerName = it },
                    customerPhone = customerPhone,
                    onCustomerPhoneChanged = { customerPhone = it },
                    deliveryDate = deliveryDate,
                    onDeliveryDateChanged = { deliveryDate = it },
                    pricing = pricingResult,
                    onSendWhatsApp = {
                        val quote = Quote(
                            id = "quote-${System.currentTimeMillis()}",
                            code = "#ORC-284",
                            customer = Customer("c-${System.currentTimeMillis()}", customerName, customerPhone),
                            productName = recipeName,
                            productSubtitle = "Produzido artesanalmente com ingredientes nobres",
                            unitsCount = recipeYield,
                            pricing = pricingResult,
                            deliveryDate = deliveryDate,
                            status = QuoteStatus.Sent
                        )
                        onSendWhatsApp(quote)
                        step = WizardStep.SentSuccess
                    },
                    onApproveOrder = {
                        val quote = Quote(
                            id = "quote-${System.currentTimeMillis()}",
                            code = "#ORC-284",
                            customer = Customer("c-${System.currentTimeMillis()}", customerName, customerPhone),
                            productName = recipeName,
                            productSubtitle = "Produzido artesanalmente com ingredientes nobres",
                            unitsCount = recipeYield,
                            pricing = pricingResult,
                            deliveryDate = deliveryDate,
                            status = QuoteStatus.Approved
                        )
                        onQuoteApproved(quote)
                        onClose()
                    }
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

@Composable
private fun CostSummaryAndPricingView(
    recipeName: String,
    yieldCount: Int,
    costs: RecipeCosts,
    pricing: PricingResult,
    onPriceChanged: (Money) -> Unit,
    onAdvance: () -> Unit
) {
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

        CostBreakdownRow("🍫", "Ingredientes selecionados", "Cacau, leite condensado, manteiga", costs.ingredients)
        CostBreakdownRow("👩‍🍳", "Mão de obra", "Preparo refinado + acabamento (1h30)", costs.labor)
        CostBreakdownRow("🔥", "Gás e energia", "Custo proporcional de cocção", costs.energy)
        CostBreakdownRow("📦", "Embalagem e fita", "Forminhas 4 pétalas e fita acetinada", costs.packaging)
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onPriceChanged(pricing.salePrice - Money.fromReais("5.00")) },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = BruleeSurfaceContainerHigh, contentColor = BruleeOnSurface)
                ) { Text("- R$ 5") }

                Button(
                    onClick = { onPriceChanged(pricing.salePrice + Money.fromReais("5.00")) },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = BruleeSurfaceContainerHigh, contentColor = BruleeOnSurface)
                ) { Text("+ R$ 5") }

                Button(
                    onClick = { onPriceChanged(pricing.salePrice + Money.fromReais("10.00")) },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = BruleeSecondaryContainer, contentColor = BruleeOnSecondaryContainer)
                ) { Text("+ R$ 10") }
            }
        }
    }

    // CTA
    TactilePrimaryButton(
        text = "AVANÇAR PARA ORÇAMENTO",
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
    customerName: String,
    onCustomerNameChanged: (String) -> Unit,
    customerPhone: String,
    onCustomerPhoneChanged: (String) -> Unit,
    deliveryDate: String,
    onDeliveryDateChanged: (String) -> Unit,
    pricing: PricingResult,
    onSendWhatsApp: () -> Unit,
    onApproveOrder: () -> Unit
) {
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

            // Suggestion Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SuggestionChip(onClick = { onCustomerNameChanged("Ana Souza") }, label = { Text("Ana Souza") })
                SuggestionChip(onClick = { onCustomerNameChanged("Carla Dias") }, label = { Text("Carla Dias") })
            }

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
                StatusPill("#ORC-284", BruleePrimaryFixed, BruleePrimary)
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
        onClick = onSendWhatsApp,
        icon = "✉"
    )

    TactileSecondaryButton(
        text = "Marcar como Aprovado (Adicionar à Produção)",
        onClick = onApproveOrder,
        icon = "✓",
        modifier = Modifier.testTag("quote_approve")
    )
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
                "Orçamento Enviado!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = BruleeOnSecondaryContainer
            )
            Text(
                "O orçamento formatado foi preparado para $customerName com sucesso.",
                style = MaterialTheme.typography.bodyMedium,
                color = BruleeOnSecondaryContainer
            )
            TactilePrimaryButton(
                text = "VOLTAR À PRODUÇÃO",
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
            onClose = {}
        )
    }
}
