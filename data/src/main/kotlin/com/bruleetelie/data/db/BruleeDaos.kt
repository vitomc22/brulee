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
