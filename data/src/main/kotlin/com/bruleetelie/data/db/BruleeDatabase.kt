package com.bruleetelie.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

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
    version = 1,
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
        @Volatile
        private var INSTANCE: BruleeDatabase? = null

        fun getInstance(context: Context): BruleeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BruleeDatabase::class.java,
                    "brulee_telie.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
