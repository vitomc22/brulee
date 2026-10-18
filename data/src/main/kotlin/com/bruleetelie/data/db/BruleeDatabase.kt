package com.bruleetelie.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        IngredientEntity::class,
        CustomerEntity::class,
        RecipeEntity::class,
        RecipeVersionEntity::class,
        RecipeIngredientEntity::class,
        QuoteEntity::class,
        ProductionOrderEntity::class,
        ProductionStepEntity::class,
        SaleSnapshotEntity::class,
        PriceObservationEntity::class,
        ShoppingItemEntity::class,
        SupplierEntity::class,
        PurchaseEntity::class,
        PurchaseItemEntity::class,
        PaymentEntity::class,
        ExpenseEntity::class,
        CostResourceEntity::class,
        DocumentImportEntity::class,
        QuoteStatusHistoryEntity::class,
        ProductionStatusHistoryEntity::class,
        PaymentStatusHistoryEntity::class
    ],
    version = 4,
    exportSchema = true
)
@TypeConverters(BruleeConverters::class)
abstract class BruleeDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
    abstract fun customerDao(): CustomerDao
    abstract fun quoteDao(): QuoteDao
    abstract fun productionOrderDao(): ProductionOrderDao
    abstract fun saleSnapshotDao(): SaleSnapshotDao
    abstract fun priceObservationDao(): PriceObservationDao
    abstract fun shoppingItemDao(): ShoppingItemDao
    abstract fun supplierDao(): SupplierDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun paymentDao(): PaymentDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun costResourceDao(): CostResourceDao
    abstract fun documentImportDao(): DocumentImportDao
    abstract fun statusHistoryDao(): StatusHistoryDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE sale_snapshots ADD COLUMN activeProductionMinutes INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS shopping_items (" +
                        "id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL, quantity TEXT NOT NULL, " +
                        "isPurchased INTEGER NOT NULL DEFAULT 0, createdAtMillis INTEGER NOT NULL)"
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS suppliers (id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL, document TEXT NOT NULL, phone TEXT NOT NULL, notes TEXT NOT NULL, createdAtMillis INTEGER NOT NULL, updatedAtMillis INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS purchases (id TEXT NOT NULL PRIMARY KEY, supplierId TEXT, purchaseDateMillis INTEGER NOT NULL, totalCents INTEGER NOT NULL, documentImportId TEXT, notes TEXT NOT NULL, createdAtMillis INTEGER NOT NULL, FOREIGN KEY(supplierId) REFERENCES suppliers(id) ON UPDATE NO ACTION ON DELETE SET NULL)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_purchases_supplierId ON purchases(supplierId)")
                db.execSQL("CREATE TABLE IF NOT EXISTS purchase_items (id TEXT NOT NULL PRIMARY KEY, purchaseId TEXT NOT NULL, ingredientId TEXT, descriptionOriginal TEXT NOT NULL, quantity TEXT NOT NULL, unit TEXT NOT NULL, normalizedQuantity TEXT NOT NULL, totalCents INTEGER NOT NULL, normalizedUnitPriceCents INTEGER NOT NULL, createdAtMillis INTEGER NOT NULL, FOREIGN KEY(purchaseId) REFERENCES purchases(id) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(ingredientId) REFERENCES ingredients(id) ON UPDATE NO ACTION ON DELETE SET NULL)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_purchase_items_purchaseId ON purchase_items(purchaseId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_purchase_items_ingredientId ON purchase_items(ingredientId)")
                db.execSQL("CREATE TABLE IF NOT EXISTS payments (id TEXT NOT NULL PRIMARY KEY, saleId TEXT NOT NULL, amountCents INTEGER NOT NULL, method TEXT NOT NULL, paidAtMillis INTEGER, status TEXT NOT NULL, notes TEXT NOT NULL, createdAtMillis INTEGER NOT NULL, FOREIGN KEY(saleId) REFERENCES sale_snapshots(id) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_payments_saleId ON payments(saleId)")
                db.execSQL("CREATE TABLE IF NOT EXISTS expenses (id TEXT NOT NULL PRIMARY KEY, category TEXT NOT NULL, description TEXT NOT NULL, amountCents INTEGER NOT NULL, expenseDateMillis INTEGER NOT NULL, isFixed INTEGER NOT NULL, notes TEXT NOT NULL, createdAtMillis INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS cost_resources (id TEXT NOT NULL PRIMARY KEY, type TEXT NOT NULL, name TEXT NOT NULL, unit TEXT NOT NULL, defaultRateCents INTEGER NOT NULL, active INTEGER NOT NULL, createdAtMillis INTEGER NOT NULL, updatedAtMillis INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS document_imports (id TEXT NOT NULL PRIMARY KEY, sourceUri TEXT NOT NULL, sha256 TEXT NOT NULL, supplierName TEXT NOT NULL, documentNumber TEXT NOT NULL, importedAtMillis INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS quote_status_history (id TEXT NOT NULL PRIMARY KEY, quoteId TEXT NOT NULL, previousStatus TEXT, newStatus TEXT NOT NULL, changedAtMillis INTEGER NOT NULL, reason TEXT NOT NULL)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quote_status_history_quoteId ON quote_status_history(quoteId)")
                db.execSQL("CREATE TABLE IF NOT EXISTS production_status_history (id TEXT NOT NULL PRIMARY KEY, productionOrderId TEXT NOT NULL, previousStatus TEXT, newStatus TEXT NOT NULL, changedAtMillis INTEGER NOT NULL, reason TEXT NOT NULL)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_production_status_history_productionOrderId ON production_status_history(productionOrderId)")
                db.execSQL("CREATE TABLE IF NOT EXISTS payment_status_history (id TEXT NOT NULL PRIMARY KEY, paymentId TEXT NOT NULL, previousStatus TEXT, newStatus TEXT NOT NULL, changedAtMillis INTEGER NOT NULL, reason TEXT NOT NULL)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_payment_status_history_paymentId ON payment_status_history(paymentId)")
            }
        }

        internal val ALL_MIGRATIONS: Array<Migration> =
            arrayOf(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)

        @Volatile
        private var INSTANCE: BruleeDatabase? = null

        fun getInstance(context: Context): BruleeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BruleeDatabase::class.java,
                    "brulee_telie.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun closeInstance() {
            synchronized(this) {
                INSTANCE?.close()
                INSTANCE = null
            }
        }

        fun checkpoint(context: Context) {
            getInstance(context).openHelper.writableDatabase
                .query("PRAGMA wal_checkpoint(FULL)")
                .close()
        }
    }
}
