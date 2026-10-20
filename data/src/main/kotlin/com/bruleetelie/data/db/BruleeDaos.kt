package com.bruleetelie.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients ORDER BY name ASC")
    fun getAllIngredients(): Flow<List<IngredientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: IngredientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ingredients: List<IngredientEntity>)

    @Delete
    suspend fun deleteIngredient(ingredient: IngredientEntity)
}

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customers: List<CustomerEntity>)
}

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes ORDER BY createdAtMillis DESC")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE id = :id")
    suspend fun getQuoteById(id: String): QuoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<QuoteEntity>)

    @Query("UPDATE quotes SET status = :status WHERE id = :quoteId")
    suspend fun updateStatus(quoteId: String, status: String)
}

@Dao
interface ProductionOrderDao {
    @Query("SELECT * FROM production_orders ORDER BY createdAtMillis DESC")
    fun getAllOrders(): Flow<List<ProductionOrderEntity>>

    @Query("SELECT * FROM production_orders WHERE id = :id")
    suspend fun getOrderById(id: String): ProductionOrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: ProductionOrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOrders(orders: List<ProductionOrderEntity>)

    @Query("UPDATE production_orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Query("SELECT * FROM production_steps WHERE orderId = :orderId ORDER BY stepOrder ASC")
    fun getStepsForOrder(orderId: String): Flow<List<ProductionStepEntity>>

    @Query("SELECT * FROM production_steps WHERE orderId = :orderId ORDER BY stepOrder ASC")
    suspend fun getStepsForOrderSync(orderId: String): List<ProductionStepEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<ProductionStepEntity>)

    @Query("UPDATE production_steps SET isCompleted = :isCompleted WHERE id = :stepId")
    suspend fun updateStepCompletion(stepId: String, isCompleted: Boolean)

    @Transaction
    suspend fun insertOrderWithSteps(order: ProductionOrderEntity, steps: List<ProductionStepEntity>) {
        insertOrder(order)
        insertSteps(steps)
    }
}

@Dao
interface SaleSnapshotDao {
    @Query("SELECT * FROM sale_snapshots ORDER BY completedAtMillis DESC")
    fun getAllSales(): Flow<List<SaleSnapshotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleSnapshotEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSales(sales: List<SaleSnapshotEntity>)
}

@Dao
interface PriceObservationDao {
    @Query("SELECT * FROM price_observations ORDER BY purchaseDateMillis DESC")
    fun getAllObservations(): Flow<List<PriceObservationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservation(observation: PriceObservationEntity)
}

@Dao
interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_items ORDER BY isPurchased ASC, createdAtMillis DESC")
    fun getAllItems(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity)

    @Query("UPDATE shopping_items SET isPurchased = :isPurchased WHERE id = :id")
    suspend fun updatePurchased(id: String, isPurchased: Boolean)

    @Query("DELETE FROM shopping_items WHERE id = :id")
    suspend fun deleteItem(id: String)
}

@Dao
interface SupplierDao {
    @Query("SELECT * FROM suppliers ORDER BY name ASC")
    fun getAll(): Flow<List<SupplierEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: SupplierEntity)
}

@Dao
interface PurchaseDao {
    @Query("SELECT * FROM purchases ORDER BY purchaseDateMillis DESC")
    fun getAll(): Flow<List<PurchaseEntity>>
    @Query("SELECT * FROM purchase_items WHERE purchaseId = :purchaseId ORDER BY createdAtMillis ASC")
    fun getItems(purchaseId: String): Flow<List<PurchaseItemEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(value: PurchaseEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(values: List<PurchaseItemEntity>)
}

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments WHERE saleId = :saleId ORDER BY createdAtMillis ASC")
    fun getForSale(saleId: String): Flow<List<PaymentEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: PaymentEntity)
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY expenseDateMillis DESC")
    fun getAll(): Flow<List<ExpenseEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: ExpenseEntity)
}

@Dao
interface CostResourceDao {
    @Query("SELECT * FROM cost_resources WHERE active = 1 ORDER BY name ASC")
    fun getActive(): Flow<List<CostResourceEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: CostResourceEntity)
}

@Dao
interface DocumentImportDao {
    @Query("SELECT * FROM document_imports ORDER BY importedAtMillis DESC")
    fun getAll(): Flow<List<DocumentImportEntity>>
    @Query("SELECT * FROM document_imports WHERE sha256 = :sha256 LIMIT 1")
    suspend fun findBySha256(sha256: String): DocumentImportEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: DocumentImportEntity)
}

@Dao
interface StatusHistoryDao {
    @Query("SELECT * FROM quote_status_history WHERE quoteId = :quoteId ORDER BY changedAtMillis ASC")
    fun quoteHistory(quoteId: String): Flow<List<QuoteStatusHistoryEntity>>
    @Query("SELECT * FROM production_status_history WHERE productionOrderId = :orderId ORDER BY changedAtMillis ASC")
    fun productionHistory(orderId: String): Flow<List<ProductionStatusHistoryEntity>>
    @Query("SELECT * FROM payment_status_history WHERE paymentId = :paymentId ORDER BY changedAtMillis ASC")
    fun paymentHistory(paymentId: String): Flow<List<PaymentStatusHistoryEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(value: QuoteStatusHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduction(value: ProductionStatusHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(value: PaymentStatusHistoryEntity)
}

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getAll(): Flow<List<RecipeEntity>>
    @Query("SELECT * FROM recipe_versions WHERE recipeId = :recipeId ORDER BY versionNumber DESC LIMIT 1")
    suspend fun latestVersion(recipeId: String): RecipeVersionEntity?
    @Query("SELECT * FROM recipe_ingredients WHERE recipeVersionId = :versionId ORDER BY rowid")
    suspend fun ingredients(versionId: String): List<RecipeIngredientEntity>
    @Upsert
    suspend fun saveRecipe(recipe: RecipeEntity)
    @Insert
    suspend fun insertVersion(version: RecipeVersionEntity)
    @Insert
    suspend fun insertIngredients(ingredients: List<RecipeIngredientEntity>)
    @Transaction
    suspend fun saveRevision(recipe: RecipeEntity, version: RecipeVersionEntity, ingredients: List<RecipeIngredientEntity>) {
        saveRecipe(recipe)
        insertVersion(version)
        insertIngredients(ingredients)
    }
}
