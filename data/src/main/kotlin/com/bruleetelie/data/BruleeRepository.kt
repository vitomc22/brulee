package com.bruleetelie.data

import com.bruleetelie.data.datastore.OperatingSettingsDataStore
import com.bruleetelie.data.db.BruleeDatabase
import com.bruleetelie.data.db.IngredientEntity
import com.bruleetelie.data.db.PriceObservationEntity
import com.bruleetelie.data.db.ShoppingItemEntity
import com.bruleetelie.domain.*
import java.math.BigDecimal
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BruleeRepository(
    private val database: BruleeDatabase? = null,
    private val settingsDataStore: OperatingSettingsDataStore? = null,
    private val externalScope: CoroutineScope? = null
) {
    private val scope = externalScope ?: CoroutineScope(Dispatchers.IO)

    private val _settings = MutableStateFlow(
        OperatingSettings(
            hourlyLaborRate = Money.fromReais("25.00"),
            gasCostPerHour = Money.fromReais("3.20"),
            electricityCostPerHour = Money.fromReais("2.50"),
            defaultMarginPercent = BigDecimal("35.00")
        )
    )
    val settings: StateFlow<OperatingSettings> = _settings.asStateFlow()

    private val _productionOrders = MutableStateFlow(initialOrders())
    val productionOrders: StateFlow<List<ProductionOrder>> = _productionOrders.asStateFlow()

    private val _quotes = MutableStateFlow(initialQuotes())
    val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    private val _completedSales = MutableStateFlow(initialSales())
    val completedSales: StateFlow<List<SaleSnapshot>> = _completedSales.asStateFlow()

    private val _ingredients = MutableStateFlow(initialIngredients())
    val ingredients: StateFlow<List<IngredientEntity>> = _ingredients.asStateFlow()

    private val _priceObservations = MutableStateFlow<List<PriceObservationEntity>>(emptyList())
    val priceObservations: StateFlow<List<PriceObservationEntity>> = _priceObservations.asStateFlow()

    private val _shoppingItems = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val shoppingItems: StateFlow<List<ShoppingItem>> = _shoppingItems.asStateFlow()

    init {
        if (database != null) {
            scope.launch {
                seedDatabaseIfEmpty()
                observeDatabaseAndDataStore()
            }
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val db = database ?: return

        // Seed Quotes if empty
        val existingQuotes = db.quoteDao().getAllQuotes().first()
        if (existingQuotes.isEmpty()) {
            db.quoteDao().insertAll(initialQuotes().map { it.toEntity() })
        }

        // Seed Production Orders & Steps if empty
        val existingOrders = db.productionOrderDao().getAllOrders().first()
        if (existingOrders.isEmpty()) {
            initialOrders().forEach { order ->
                db.productionOrderDao().insertOrderWithSteps(
                    order = order.toEntity(),
                    steps = order.steps.mapIndexed { idx, step -> step.toEntity(order.id, idx) }
                )
            }
        }

        // Seed Sales if empty
        val existingSales = db.saleSnapshotDao().getAllSales().first()
        if (existingSales.isEmpty()) {
            db.saleSnapshotDao().insertAllSales(initialSales().map { it.toEntity() })
        }

        // Seed Ingredients if empty
        val existingIngredients = db.ingredientDao().getAllIngredients().first()
        if (existingIngredients.isEmpty()) {
            db.ingredientDao().insertAll(initialIngredients())
        }
    }

    private suspend fun observeDatabaseAndDataStore() {
        val db = database ?: return

        // Observe DataStore Settings
        scope.launch {
            settingsDataStore?.settingsFlow?.collectLatest { newSettings ->
                _settings.value = newSettings
            }
        }

        // Observe Quotes
        scope.launch {
            db.quoteDao().getAllQuotes().collectLatest { entities ->
                _quotes.value = entities.map { it.toDomain() }
            }
        }

        // Observe Orders
        scope.launch {
            db.productionOrderDao().getAllOrders().collectLatest { orderEntities ->
                val orders = orderEntities.map { entity ->
                    val steps = db.productionOrderDao().getStepsForOrderSync(entity.id)
                    entity.toDomain(steps)
                }
                _productionOrders.value = orders
            }
        }

        // Observe Sales
        scope.launch {
            db.saleSnapshotDao().getAllSales().collectLatest { salesEntities ->
                _completedSales.value = salesEntities.map { it.toDomain() }
            }
        }

        // Observe Ingredients
        scope.launch {
            db.ingredientDao().getAllIngredients().collectLatest { ingEntities ->
                _ingredients.value = ingEntities
            }
        }

        scope.launch {
            db.priceObservationDao().getAllObservations().collectLatest { observations ->
                _priceObservations.value = observations
            }
        }

        scope.launch {
            db.shoppingItemDao().getAllItems().collectLatest { items ->
                _shoppingItems.value = items.map { it.toDomain() }
            }
        }
    }

    fun updateSettings(newSettings: OperatingSettings) {
        _settings.value = newSettings
        settingsDataStore?.let {
            scope.launch { it.saveSettings(newSettings) }
        }
    }

    fun updateStepStatus(orderId: String, stepId: String, completed: Boolean) {
        // Update local state first for immediate UI response
        _productionOrders.update { orders ->
            orders.map { order ->
                if (order.id == orderId) {
                    val updatedSteps = order.steps.map { step ->
                        val matchesId = step.id == stepId ||
                            step.id.substringBefore('-').removePrefix("s") == stepId
                        if (matchesId) step.copy(isCompleted = completed) else step
                    }
                    order.copy(
                        steps = updatedSteps,
                        status = if (updatedSteps.all { it.isCompleted }) ProductionStatus.Completed else ProductionStatus.InProgress
                    )
                } else order
            }
        }

        // Update Room if database is attached
        database?.let { db ->
            scope.launch {
                db.productionOrderDao().updateStepCompletion(stepId, completed)
                val order = _productionOrders.value.firstOrNull { it.id == orderId }
                if (order != null) {
                    val newStatus = if (order.steps.all { it.isCompleted }) ProductionStatus.Completed.name else ProductionStatus.InProgress.name
                    db.productionOrderDao().updateOrderStatus(orderId, newStatus)
                }
            }
        }
    }

    fun completeOrder(orderId: String, actualCosts: RecipeCosts?): SaleSnapshot? {
        val order = _productionOrders.value.firstOrNull { it.id == orderId } ?: return null
        val finalActualCost = actualCosts ?: order.estimatedCosts
        val pricing = PricingEngine.fromSalePrice(finalActualCost, order.salePrice)

        val snapshot = SaleSnapshot(
            id = "sale-${System.currentTimeMillis()}",
            orderId = order.id,
            quoteCode = order.quoteCode,
            customerName = order.customerName,
            productName = order.productName,
            salePrice = order.salePrice,
            estimatedCost = order.estimatedCosts.total,
            actualCost = finalActualCost.total,
            profit = pricing.profit,
            marginPercent = pricing.marginPercent,
            completedAtFormatted = "Hoje"
        )

        _productionOrders.update { orders ->
            orders.map {
                if (it.id == orderId) it.copy(status = ProductionStatus.Completed, actualCosts = finalActualCost) else it
            }
        }

        _completedSales.update { it + snapshot }

        // Persist to Room
        database?.let { db ->
            scope.launch {
                val updatedOrder = order.copy(status = ProductionStatus.Completed, actualCosts = finalActualCost)
                db.productionOrderDao().insertOrder(updatedOrder.toEntity())
                db.saleSnapshotDao().insertSale(snapshot.toEntity())
            }
        }

        return snapshot
    }

    fun addQuote(quote: Quote) {
        _quotes.update { it + quote }
        database?.let { db ->
            scope.launch {
                db.quoteDao().insertQuote(quote.toEntity())
            }
        }
    }

    fun approveQuote(quoteId: String): ProductionOrder? {
        val quote = _quotes.value.firstOrNull { it.id == quoteId } ?: return null
        _quotes.update { list ->
            list.map { if (it.id == quoteId) it.copy(status = QuoteStatus.Approved) else it }
        }

        val newOrder = ProductionOrder(
            id = "order-${System.currentTimeMillis()}",
            quoteId = quote.id,
            quoteCode = quote.code,
            customerName = quote.customer.name,
            productName = "${quote.unitsCount}x ${quote.productName}",
            deliveryTimeFormatted = quote.deliveryDate,
            steps = listOf(
                ProductionStep("s1-${System.currentTimeMillis()}", "Massa cozida e pesada", isCompleted = false),
                ProductionStep("s2-${System.currentTimeMillis()}", "Boleamento finalizado", isCompleted = false),
                ProductionStep("s3-${System.currentTimeMillis()}", "Ornamentação e acabamento", isCompleted = false),
                ProductionStep("s4-${System.currentTimeMillis()}", "Embalagem cartonada e laço", isCompleted = false)
            ),
            status = ProductionStatus.Pending,
            estimatedCosts = quote.pricing.costs,
            salePrice = quote.pricing.salePrice
        )

        _productionOrders.update { listOf(newOrder) + it }

        database?.let { db ->
            scope.launch {
                db.quoteDao().updateStatus(quoteId, QuoteStatus.Approved.name)
                db.productionOrderDao().insertOrderWithSteps(
                    order = newOrder.toEntity(),
                    steps = newOrder.steps.mapIndexed { idx, step -> step.toEntity(newOrder.id, idx) }
                )
            }
        }

        return newOrder
    }

    fun addIngredient(name: String, unitSymbol: String) {
        val newIng = IngredientEntity(
            id = "ing-${System.currentTimeMillis()}",
            name = name,
            defaultUnitSymbol = unitSymbol
        )
        _ingredients.update { it + newIng }
        database?.let { db ->
            scope.launch { db.ingredientDao().insertIngredient(newIng) }
        }
    }

    fun addPriceObservation(
        ingredientId: String,
        supplierName: String,
        pkgValue: String,
        pkgUnit: String,
        pkgPriceCents: Long,
        unitPriceCents: Long,
        source: String = "MANUAL"
    ): PriceObservationEntity {
        val obs = PriceObservationEntity(
            id = "obs-${UUID.randomUUID()}",
            ingredientId = ingredientId,
            supplierName = supplierName,
            packageQuantityValue = pkgValue,
            packageQuantityUnit = pkgUnit,
            packagePriceCents = pkgPriceCents,
            normalizedUnitPriceCents = unitPriceCents,
            source = source
        )
        _priceObservations.update { listOf(obs) + it }
        database?.let { db ->
            scope.launch { db.priceObservationDao().insertObservation(obs) }
        }
        return obs
    }

    fun addShoppingItem(name: String, quantity: String): ShoppingItem? {
        val cleanName = name.trim()
        val cleanQuantity = quantity.trim()
        if (cleanName.isEmpty() || cleanQuantity.isEmpty()) return null
        val item = ShoppingItem(
            id = "shopping-${UUID.randomUUID()}",
            name = cleanName,
            quantity = cleanQuantity
        )
        _shoppingItems.update { listOf(item) + it }
        database?.let { db -> scope.launch { db.shoppingItemDao().insertItem(item.toEntity()) } }
        return item
    }

    fun setShoppingItemPurchased(id: String, purchased: Boolean) {
        _shoppingItems.update { items ->
            items.map { if (it.id == id) it.copy(isPurchased = purchased) else it }
        }
        database?.let { db -> scope.launch { db.shoppingItemDao().updatePurchased(id, purchased) } }
    }

    fun deleteShoppingItem(id: String) {
        _shoppingItems.update { items -> items.filterNot { it.id == id } }
        database?.let { db -> scope.launch { db.shoppingItemDao().deleteItem(id) } }
    }

    private companion object {
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
}
