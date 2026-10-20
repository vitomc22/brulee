package com.bruleetelie.app

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.lifecycle.ViewModelProvider
import com.bruleetelie.app.ui.BruleeAppViewModel
import com.bruleetelie.data.datastore.OperatingSettingsDataStore
import com.bruleetelie.data.*
import com.bruleetelie.data.db.*
import com.bruleetelie.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.math.BigDecimal

// Demonstration data belongs exclusively to the test APK.
object LegacyFixtures {
        fun initialIngredients(): List<IngredientEntity> {
            return listOf(
                IngredientEntity("ing-1", "Chocolate em Pó 50%", "g"),
                IngredientEntity("ing-2", "Leite Condensado 395g", "un"),
                IngredientEntity("ing-3", "Creme de Leite 200g", "un"),
                IngredientEntity("ing-4", "Manteiga Extra Sem Sal", "g"),
                IngredientEntity("ing-5", "Pasta de Pistache Puro", "g"),
                IngredientEntity("ing-6", "Farinha de Trigo Especial", "kg"),
                IngredientEntity("ing-7", "Açúcar de Confeiteiro", "kg"),
                IngredientEntity("ing-8", "Ovos Brancos Grandes", "dz")
            )
        }

        fun initialOrders(): List<ProductionOrder> {
            val brigadeiroCosts = RecipeCosts(
                ingredients = Money.fromReais("38.40"),
                labor = Money.fromReais("50.00"),
                energy = Money.fromReais("5.70"),
                packaging = Money.fromReais("4.50")
            )
            val boloCosts = RecipeCosts(
                ingredients = Money.fromReais("62.30"),
                labor = Money.fromReais("45.00"),
                energy = Money.fromReais("6.80"),
                packaging = Money.fromReais("8.00")
            )

            return listOf(
                ProductionOrder(
                    id = "ord-284",
                    quoteId = "quote-284",
                    quoteCode = "#ORC-284",
                    customerName = "Ana Souza",
                    productName = "40 Brigadeiros Gourmet (Pistache, Doce de Leite, Crème Brûlée)",
                    deliveryTimeFormatted = "16:00 (Hoje)",
                    steps = listOf(
                        ProductionStep("s1-284", "Massa cozida e pesada (18g cada)", isCompleted = true),
                        ProductionStep("s2-284", "Boleamento finalizado", isCompleted = true),
                        ProductionStep("s3-284", "Ornamentação e acabamento", isCompleted = false, isCurrent = true),
                        ProductionStep("s4-284", "Embalagem cartonada e laço", isCompleted = false)
                    ),
                    status = ProductionStatus.InProgress,
                    estimatedCosts = brigadeiroCosts,
                    salePrice = Money.fromReais("149.90")
                ),
                ProductionOrder(
                    id = "ord-282",
                    quoteId = "quote-282",
                    quoteCode = "#ORC-282",
                    customerName = "Carla Dias",
                    productName = "Bolo Brûlée Red Velvet 2kg",
                    deliveryTimeFormatted = "18:30 (Hoje)",
                    steps = listOf(
                        ProductionStep("s1-282", "Massa assada e resfriada", isCompleted = true),
                        ProductionStep("s2-282", "Recheio no descanso", isCompleted = true),
                        ProductionStep("s3-282", "Montagem e prensagem", isCompleted = false, isCurrent = true),
                        ProductionStep("s4-282", "Decoração e flores naturais", isCompleted = false)
                    ),
                    status = ProductionStatus.InProgress,
                    estimatedCosts = boloCosts,
                    salePrice = Money.fromReais("185.00")
                ),
                ProductionOrder(
                    id = "ord-285",
                    quoteId = "quote-285",
                    quoteCode = "#ORC-285",
                    customerName = "Mariana Lima",
                    productName = "Cento de Macarons Sortidos",
                    deliveryTimeFormatted = "14:00 (Amanhã)",
                    steps = listOf(
                        ProductionStep("s1-285", "Separar insumos e peneirar farinha", isCompleted = false),
                        ProductionStep("s2-285", "Macaronage e pingagem", isCompleted = false),
                        ProductionStep("s3-285", "Secagem de cascas e forno", isCompleted = false),
                        ProductionStep("s4-285", "Recheio e maturação 24h", isCompleted = false)
                    ),
                    status = ProductionStatus.Pending,
                    estimatedCosts = RecipeCosts(Money.fromReais("85.00"), Money.fromReais("70.00"), Money.fromReais("9.00"), Money.fromReais("12.00")),
                    salePrice = Money.fromReais("280.00")
                )
            )
        }

        fun initialQuotes(): List<Quote> {
            val costs = RecipeCosts(
                ingredients = Money.fromReais("38.40"),
                labor = Money.fromReais("50.00"),
                energy = Money.fromReais("5.70"),
                packaging = Money.fromReais("4.50")
            )
            val pricing = PricingEngine.fromSalePrice(costs, Money.fromReais("149.90"))

            return listOf(
                Quote(
                    id = "quote-284",
                    code = "#ORC-284",
                    customer = Customer("c1", "Ana Souza", "(11) 98765-4321"),
                    productName = "40 Brigadeiros Gourmet",
                    productSubtitle = "Pistache tostado, Doce de Leite com Flor de Sal, Belga e Crème Brûlée",
                    unitsCount = 40,
                    pricing = pricing,
                    deliveryDate = "Sábado, 14 de Outubro - 16:00",
                    validDays = 3,
                    notes = "Caixa Telie Ouro com laço acetinado e entrega inclusa.",
                    status = QuoteStatus.Sent
                )
            )
        }

        fun initialSales(): List<SaleSnapshot> {
            return listOf(
                SaleSnapshot(
                    id = "s-1",
                    orderId = "ord-270",
                    quoteCode = "#ORC-270",
                    customerName = "Beatriz Rocha",
                    productName = "Bolo Nozes & Baba de Moça 2.5kg",
                    salePrice = Money.fromReais("220.00"),
                    estimatedCost = Money.fromReais("82.10"),
                    actualCost = Money.fromReais("82.10"),
                    profit = Money.fromReais("137.90"),
                    marginPercent = BigDecimal("62.7"),
                    completedAtFormatted = "Ontem"
                ),
                SaleSnapshot(
                    id = "s-2",
                    orderId = "ord-268",
                    quoteCode = "#ORC-268",
                    customerName = "Felipe Ramos",
                    productName = "Caixa Telie Degustação 16 un",
                    salePrice = Money.fromReais("68.00"),
                    estimatedCost = Money.fromReais("26.40"),
                    actualCost = Money.fromReais("26.40"),
                    profit = Money.fromReais("41.60"),
                    marginPercent = BigDecimal("61.1"),
                    completedAtFormatted = "3 dias atrás"
                )
            )
        }
    }

fun clearTestData(rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    val repository = ViewModelProvider(rule.activity)[BruleeAppViewModel::class.java].repository
    runBlocking(Dispatchers.IO) {
        androidx.work.WorkManager.getInstance(rule.activity).cancelAllWork().result.get()
        repository.awaitPendingWrites()
        BruleeDatabase.getInstance(rule.activity).clearAllTables()
        repository.updateSettings(OperatingSettings())
        repository.awaitPendingWrites()
        java.io.File(rule.activity.filesDir, "automatic-backups").deleteRecursively()
        java.io.File(rule.activity.cacheDir, "shared").deleteRecursively()
    }
    rule.waitUntil(10000) {
        repository.quotes.value.isEmpty() && repository.productionOrders.value.isEmpty() &&
        repository.completedSales.value.isEmpty() && repository.recipes.value.isEmpty() &&
        repository.ingredients.value.isEmpty() && repository.priceObservations.value.isEmpty() && repository.shoppingItems.value.isEmpty() &&
        repository.settings.value == OperatingSettings()
    }
    runBlocking(Dispatchers.IO) {
        val database = BruleeDatabase.getInstance(rule.activity).openHelper.readableDatabase
        val tables = database.query("SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'room_%' AND name != 'android_metadata'").use { cursor ->
            buildList { while (cursor.moveToNext()) add(cursor.getString(0)) }
        }
        tables.forEach { table ->
            database.query("SELECT COUNT(*) FROM \"${table.replace("\"", "\"\"")}\"").use { cursor ->
                check(cursor.moveToFirst())
                org.junit.Assert.assertEquals("Test cleanup left rows in $table", 0L, cursor.getLong(0))
            }
        }
    }
}

fun seedProductionTestData(rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    runBlocking(Dispatchers.IO) {
        val db = BruleeDatabase.getInstance(rule.activity)
        LegacyFixtures.initialOrders().forEach { order ->
            db.productionOrderDao().insertOrderWithSteps(order.toEntity(), order.steps.mapIndexed { i, step -> step.toEntity(order.id, i) })
        }
    }
    rule.waitUntil(10000) { rule.onAllNodesWithTag("production_order_ord-284").fetchSemanticsNodes().isNotEmpty() }
}

fun seedFinanceTestData(rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    runBlocking(Dispatchers.IO) {
        val db = BruleeDatabase.getInstance(rule.activity)
        db.saleSnapshotDao().insertAllSales(LegacyFixtures.initialSales().map { it.toEntity() })
        db.quoteDao().insertAll(LegacyFixtures.initialQuotes().map { it.toEntity() })
    }
    val repo = ViewModelProvider(rule.activity)[BruleeAppViewModel::class.java].repository
    rule.waitUntil(10000) { repo.completedSales.value.size == 2 }
}

fun seedRecipeTestData(rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    val repo = ViewModelProvider(rule.activity)[BruleeAppViewModel::class.java].repository
    repo.saveRecipe(SavedRecipe("test-recipe", "", 0, "Brigadeiro Gourmet Tradicional", 30, listOf(
        RecipeIngredientItem("1", "Chocolate Nobre 50%", Quantity.kilograms(1), Money(6000), Quantity.grams(350)),
        RecipeIngredientItem("2", "Leite Condensado Moça", Quantity.grams(395), Money(750), Quantity.grams(395)),
        RecipeIngredientItem("3", "Manteiga Extra s/ Sal", Quantity.grams(200), Money(1200), Quantity.grams(40))
    ), OperationalTimes(Quantity.minutes(60), Quantity.minutes(30), Quantity.minutes(30)), Money(450)))
    runBlocking { repo.awaitPendingWrites() }
}

fun openTestRecipe(rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    seedRecipeTestData(rule)
    rule.onNodeWithTag("nav_wizard").performClick()
    rule.waitUntil(10000) { rule.onAllNodesWithTag("recipe_load_test-recipe").fetchSemanticsNodes().isNotEmpty() }
    rule.onNodeWithTag("recipe_load_test-recipe").performScrollTo().performClick()
}

fun openOcrFixture(rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    rule.onNodeWithTag("nav_stock").performClick()
    rule.onNodeWithTag("stock_import").performScrollTo().performClick()
    rule.onNodeWithTag("ocr_raw_text").performScrollTo().performTextInput("""
        SUPERMERCADO DOCE AÇÚCAR
        CNPJ: 12.345.678/0001-90
        DATA: 15/10/2026
        1 CHOC NOBRE LEITE 1KG R$ 34,90
        2 LEITE CONDENS MOCA TP 395G R$ 7,49
        3 MANTEIGA EXTRA S SAL 200G R$ 11,89
        4 CACAU PO 100% 500G R$ 29,90
        TOTAL R$ 84,18
    """.trimIndent())
}
