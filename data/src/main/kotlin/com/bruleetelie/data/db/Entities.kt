package com.bruleetelie.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val defaultUnitSymbol: String,
    val aliasesJson: String = "[]",
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val notes: String = ""
)

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String = "",
    val defaultServings: Int = 1,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "recipe_versions",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["recipeId"])]
)
data class RecipeVersionEntity(
    @PrimaryKey val id: String,
    val recipeId: String,
    val versionNumber: Int,
    val prepMinutes: String,
    val cookMinutes: String,
    val decorationMinutes: String,
    val energySource: String,
    val complexity: String,
    val packagingCostCents: Long = 0L,
    val overheadCostCents: Long = 0L,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "recipe_ingredients",
    foreignKeys = [
        ForeignKey(
            entity = RecipeVersionEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeVersionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["recipeVersionId"])]
)
data class RecipeIngredientEntity(
    @PrimaryKey val id: String,
    val recipeVersionId: String,
    val name: String,
    val packageQuantityValue: String,
    val packageQuantityUnit: String,
    val packagePriceCents: Long,
    val usedQuantityValue: String,
    val usedQuantityUnit: String
)

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey val id: String,
    val code: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val productName: String,
    val productSubtitle: String,
    val unitsCount: Int,
    val ingredientsCostCents: Long,
    val laborCostCents: Long,
    val energyCostCents: Long,
    val packagingCostCents: Long,
    val overheadCostCents: Long = 0L,
    val salePriceCents: Long,
    val profitCents: Long,
    val marginPercent: String,
    val deliveryDate: String,
    val validDays: Int = 3,
    val notes: String = "",
    val status: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "production_orders")
data class ProductionOrderEntity(
    @PrimaryKey val id: String,
    val quoteId: String,
    val quoteCode: String,
    val customerName: String,
    val productName: String,
    val deliveryTimeFormatted: String,
    val status: String,
    val estIngredientsCents: Long,
    val estLaborCents: Long,
    val estEnergyCents: Long,
    val estPackagingCents: Long,
    val estOverheadCents: Long = 0L,
    val actIngredientsCents: Long? = null,
    val actLaborCents: Long? = null,
    val actEnergyCents: Long? = null,
    val actPackagingCents: Long? = null,
    val actOverheadCents: Long? = null,
    val salePriceCents: Long,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "production_steps",
    foreignKeys = [
        ForeignKey(
            entity = ProductionOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["orderId"])]
)
data class ProductionStepEntity(
    @PrimaryKey val id: String,
    val orderId: String,
    val stepOrder: Int,
    val title: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

@Entity(tableName = "sale_snapshots")
data class SaleSnapshotEntity(
    @PrimaryKey val id: String,
    val orderId: String,
    val quoteCode: String,
    val customerName: String,
    val productName: String,
    val salePriceCents: Long,
    val estimatedCostCents: Long,
    val actualCostCents: Long,
    val profitCents: Long,
    val marginPercent: String,
    val completedAtFormatted: String,
    val paymentStatus: String,
    val completedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "price_observations")
data class PriceObservationEntity(
    @PrimaryKey val id: String,
    val ingredientId: String,
    val supplierName: String = "",
    val packageQuantityValue: String,
    val packageQuantityUnit: String,
    val packagePriceCents: Long,
    val normalizedUnitPriceCents: Long,
    val purchaseDateMillis: Long = System.currentTimeMillis(),
    val source: String = "MANUAL"
)
