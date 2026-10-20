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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineStart
import androidx.room.withTransaction
import com.bruleetelie.data.db.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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

    private val _productionOrders = MutableStateFlow<List<ProductionOrder>>(emptyList())
    val productionOrders: StateFlow<List<ProductionOrder>> = _productionOrders.asStateFlow()

    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    private val _completedSales = MutableStateFlow<List<SaleSnapshot>>(emptyList())
    val completedSales: StateFlow<List<SaleSnapshot>> = _completedSales.asStateFlow()

    private val _ingredients = MutableStateFlow<List<IngredientEntity>>(emptyList())
    val ingredients: StateFlow<List<IngredientEntity>> = _ingredients.asStateFlow()

    private val _priceObservations = MutableStateFlow<List<PriceObservationEntity>>(emptyList())
    val priceObservations: StateFlow<List<PriceObservationEntity>> = _priceObservations.asStateFlow()

    private val _shoppingItems = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val shoppingItems: StateFlow<List<ShoppingItem>> = _shoppingItems.asStateFlow()

    private val writes = Mutex()
    private val _recipes = MutableStateFlow<List<SavedRecipe>>(emptyList())
    val recipes: StateFlow<List<SavedRecipe>> = _recipes.asStateFlow()

    /** A barrier for backup/export and callers that must observe committed data. */
    suspend fun awaitPendingWrites() { writes.withLock { } }

    suspend fun <T> withExclusiveWrites(block: suspend () -> T): T = writes.withLock { block() }

    private fun persist(block: suspend (BruleeDatabase) -> Unit) {
        val db = database ?: return
        scope.launch(start = CoroutineStart.UNDISPATCHED) { writes.withLock { block(db) } }
    }

    fun saveRecipe(recipe: SavedRecipe) {
        val current = _recipes.value.firstOrNull { it.id == recipe.id }
        val revision = recipe.copy(versionId = UUID.randomUUID().toString(), version = (current?.version ?: 0) + 1)
        _recipes.update { list -> list.filterNot { it.id == revision.id } + revision }
        database?.let { db -> scope.launch(start = CoroutineStart.UNDISPATCHED) {
            writes.withLock {
                db.recipeDao().saveRevision(
                    RecipeEntity(revision.id, revision.name, defaultServings = revision.yieldCount),
                    RecipeVersionEntity(revision.versionId, revision.id, revision.version,
                        revision.times.prepTime.value.toPlainString(), revision.times.cookTime.value.toPlainString(),
                        revision.times.decorationTime.value.toPlainString(), revision.times.energySource.name,
                        revision.times.complexity.name, revision.packaging.cents, revision.overhead.cents),
                    revision.ingredients.map { item -> RecipeIngredientEntity(
                        UUID.randomUUID().toString(), revision.versionId, item.name,
                        item.packageQuantity.value.toPlainString(), item.packageQuantity.unit.symbol, item.packagePrice.cents,
                        item.usedQuantity.value.toPlainString(), item.usedQuantity.unit.symbol
                    ) }
                )
            }
        } }
        recipe.ingredients.forEach { addIngredient(it.name, it.packageQuantity.unit.symbol) }
    }

    init {
        if (database != null) {
            scope.launch {
                observeDatabaseAndDataStore()
            }
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

        scope.launch {
            db.recipeDao().getAll().collectLatest { entities ->
                _recipes.value = entities.mapNotNull { recipe ->
                    db.recipeDao().latestVersion(recipe.id)?.let { version ->
                        SavedRecipe(recipe.id, version.id, version.versionNumber, recipe.name, recipe.defaultServings,
                            db.recipeDao().ingredients(version.id).map { item -> RecipeIngredientItem(
                                item.id, item.name,
                                Quantity(item.packageQuantityValue.toBigDecimal(), UnitOfMeasure.entries.first { it.symbol == item.packageQuantityUnit }),
                                Money(item.packagePriceCents),
                                Quantity(item.usedQuantityValue.toBigDecimal(), UnitOfMeasure.entries.first { it.symbol == item.usedQuantityUnit })
                            ) },
                            OperationalTimes(Quantity.minutes(version.prepMinutes.toBigDecimal()),
                                Quantity.minutes(version.cookMinutes.toBigDecimal()), Quantity.minutes(version.decorationMinutes.toBigDecimal()),
                                EnergySource.valueOf(version.energySource), Complexity.valueOf(version.complexity)),
                            Money(version.packagingCostCents), Money(version.overheadCostCents))
                    }
                }
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
                    val order = entity.toDomain(steps)
                    val current = order.steps.indexOfFirst { !it.isCompleted }
                    order.copy(steps = order.steps.mapIndexed { index, step -> step.copy(isCurrent = index == current) })
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
            scope.launch(start = CoroutineStart.UNDISPATCHED) { writes.withLock { it.saveSettings(newSettings) } }
        }
    }

    fun updateStepStatus(orderId: String, stepId: String, completed: Boolean) {
        val order = _productionOrders.value.firstOrNull { it.id == orderId } ?: return
        if (order.status == ProductionStatus.Completed || order.status == ProductionStatus.Cancelled) return
        if (order.steps.none { it.id == stepId }) return
        val changed = order.steps.map { if (it.id == stepId) it.copy(isCompleted = completed) else it }
        val firstIncomplete = changed.indexOfFirst { !it.isCompleted }
        val steps = changed.mapIndexed { index, step -> step.copy(isCurrent = index == firstIncomplete) }
        val updated = order.copy(steps = steps, status = ProductionStatus.InProgress)
        _productionOrders.update { list -> list.map { if (it.id == orderId) updated else it } }
        database?.let { db -> scope.launch(start = CoroutineStart.UNDISPATCHED) { writes.withLock {
            db.withTransaction {
                db.productionOrderDao().updateStepCompletion(stepId, completed)
                db.productionOrderDao().updateOrderStatus(orderId, ProductionStatus.InProgress.name)
            }
        } } }
    }

    fun completeOrder(orderId: String, actualCosts: RecipeCosts?, activeMinutes: Int = 0): SaleSnapshot? {
        val order = _productionOrders.value.firstOrNull { it.id == orderId } ?: return null
        _completedSales.value.firstOrNull { it.orderId == orderId }?.let { return it }
        if (!order.isAllStepsDone || order.status in listOf(ProductionStatus.Completed, ProductionStatus.Cancelled)) return null
        val finalActualCost = actualCosts ?: order.estimatedCosts
        val pricing = PricingEngine.fromSalePrice(finalActualCost, order.salePrice)
        require(activeMinutes >= 0)
        val snapshot = SaleSnapshot(
            id = "sale-$orderId", orderId = orderId, quoteCode = order.quoteCode,
            customerName = order.customerName, productName = order.productName,
            salePrice = order.salePrice, estimatedCost = order.estimatedCosts.total,
            actualCost = finalActualCost.total, profit = pricing.profit, marginPercent = pricing.marginPercent,
            activeProductionMinutes = activeMinutes,
            completedAtFormatted = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        )
        val updated = order.copy(status = ProductionStatus.Completed, actualCosts = finalActualCost)
        _productionOrders.update { list -> list.map { if (it.id == orderId) updated else it } }
        _completedSales.update { it + snapshot }
        _quotes.update { list -> list.map { if (it.id == order.quoteId) it.copy(status = QuoteStatus.ConvertedToSale) else it } }
        database?.let { db -> scope.launch(start = CoroutineStart.UNDISPATCHED) { writes.withLock { db.withTransaction {
            db.productionOrderDao().insertOrderWithSteps(updated.toEntity(), updated.steps.mapIndexed { i, step -> step.toEntity(orderId, i) })
            db.saleSnapshotDao().insertSale(snapshot.toEntity())
            db.quoteDao().updateStatus(order.quoteId, QuoteStatus.ConvertedToSale.name)
        } } } }
        return snapshot
    }

    fun addQuote(quote: Quote) {
        val existing = _quotes.value.firstOrNull { it.id == quote.id }
        // Sharing a previously approved quote must not downgrade its lifecycle.
        val saved = if (existing?.status in listOf(QuoteStatus.Approved, QuoteStatus.ConvertedToSale)) quote.copy(status = existing!!.status) else quote
        _quotes.update { listOf(saved) + it.filterNot { item -> item.id == saved.id } }
        database?.let { db -> scope.launch(start = CoroutineStart.UNDISPATCHED) { writes.withLock { db.withTransaction {
            db.customerDao().insertCustomer(CustomerEntity(saved.customer.id, saved.customer.name, saved.customer.phone, saved.customer.notes))
            db.quoteDao().insertQuote(saved.toEntity())
        } } } }
    }

    fun updateQuoteStatus(quoteId: String, status: QuoteStatus) {
        val quote = _quotes.value.firstOrNull { it.id == quoteId } ?: return
        if (quote.status in listOf(QuoteStatus.Approved, QuoteStatus.ConvertedToSale)) return
        addQuote(quote.copy(status = status))
    }

    fun approveQuote(quoteId: String): ProductionOrder? {
        _productionOrders.value.firstOrNull { it.quoteId == quoteId }?.let { return it }
        val quote = _quotes.value.firstOrNull { it.id == quoteId } ?: return null
        if (quote.status in listOf(QuoteStatus.Approved, QuoteStatus.Rejected, QuoteStatus.Expired, QuoteStatus.ConvertedToSale)) return null
        val orderId = "order-$quoteId"
        val newOrder = ProductionOrder(
            id = orderId, quoteId = quote.id, quoteCode = quote.code,
            customerName = quote.customer.name, productName = "${quote.unitsCount}x ${quote.productName}",
            deliveryTimeFormatted = quote.deliveryDate,
            steps = listOf("Separar e pesar ingredientes", "Preparar e cozinhar a receita", "Finalizar e decorar", "Embalar e conferir entrega")
                .mapIndexed { index, title -> ProductionStep("$orderId-step-$index", title, isCurrent = index == 0) },
            estimatedCosts = quote.pricing.costs, salePrice = quote.pricing.salePrice
        )
        _quotes.update { list -> list.map { if (it.id == quoteId) it.copy(status = QuoteStatus.Approved) else it } }
        _productionOrders.update { listOf(newOrder) + it }
        database?.let { db -> scope.launch(start = CoroutineStart.UNDISPATCHED) { writes.withLock { db.withTransaction {
            // Upsert the quote here as well so approval is atomic even before a preceding save finishes.
            db.quoteDao().insertQuote(quote.copy(status = QuoteStatus.Approved).toEntity())
            db.productionOrderDao().insertOrderWithSteps(newOrder.toEntity(), newOrder.steps.mapIndexed { i, step -> step.toEntity(orderId, i) })
        } } } }
        return newOrder
    }

    fun addIngredient(name: String, unitSymbol: String): IngredientEntity {
        _ingredients.value.firstOrNull { it.name.equals(name.trim(), ignoreCase = true) }?.let { return it }
        val newIng = IngredientEntity(
            id = "ing-${UUID.randomUUID()}",
            name = name.trim(),
            defaultUnitSymbol = unitSymbol
        )
        _ingredients.update { it + newIng }
        persist { it.ingredientDao().insertIngredient(newIng) }
        return newIng
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
        persist { it.priceObservationDao().insertObservation(obs) }
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
        persist { it.shoppingItemDao().insertItem(item.toEntity()) }
        return item
    }

    fun setShoppingItemPurchased(id: String, purchased: Boolean) {
        _shoppingItems.update { items ->
            items.map { if (it.id == id) it.copy(isPurchased = purchased) else it }
        }
        persist { it.shoppingItemDao().updatePurchased(id, purchased) }
    }

    fun deleteShoppingItem(id: String) {
        _shoppingItems.update { items -> items.filterNot { it.id == id } }
        persist { it.shoppingItemDao().deleteItem(id) }
    }

}
