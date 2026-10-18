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
        PriceObservationEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(BruleeConverters::class)
abstract class BruleeDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
    abstract fun customerDao(): CustomerDao
    abstract fun quoteDao(): QuoteDao
    abstract fun productionOrderDao(): ProductionOrderDao
    abstract fun saleSnapshotDao(): SaleSnapshotDao
    abstract fun priceObservationDao(): PriceObservationDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE sale_snapshots ADD COLUMN activeProductionMinutes INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        @Volatile
        private var INSTANCE: BruleeDatabase? = null

        fun getInstance(context: Context): BruleeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BruleeDatabase::class.java,
                    "brulee_telie.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
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
