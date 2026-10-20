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
    @androidx.room.ColumnInfo(defaultValue = "NULL") val draftData: String? = null,
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
    val activeProductionMinutes: Int = 0,
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

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val quantity: String,
    val isPurchased: Boolean = false,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "suppliers")
data class SupplierEntity(
    @PrimaryKey val id: String,
    val name: String,
    val document: String = "",
    val phone: String = "",
    val notes: String = "",
    val createdAtMillis: Long = System.currentTimeMillis(),
    val updatedAtMillis: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "purchases",
    foreignKeys = [
        ForeignKey(entity = SupplierEntity::class, parentColumns = ["id"], childColumns = ["supplierId"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index(value = ["supplierId"])]
)
data class PurchaseEntity(
    @PrimaryKey val id: String,
    val supplierId: String? = null,
    val purchaseDateMillis: Long,
    val totalCents: Long,
    val documentImportId: String? = null,
    val notes: String = "",
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "purchase_items",
    foreignKeys = [
        ForeignKey(entity = PurchaseEntity::class, parentColumns = ["id"], childColumns = ["purchaseId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = IngredientEntity::class, parentColumns = ["id"], childColumns = ["ingredientId"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index(value = ["purchaseId"]), Index(value = ["ingredientId"])]
)
data class PurchaseItemEntity(
    @PrimaryKey val id: String,
    val purchaseId: String,
    val ingredientId: String? = null,
    val descriptionOriginal: String,
    val quantity: String,
    val unit: String,
    val normalizedQuantity: String,
    val totalCents: Long,
    val normalizedUnitPriceCents: Long,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(entity = SaleSnapshotEntity::class, parentColumns = ["id"], childColumns = ["saleId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["saleId"])]
)
data class PaymentEntity(
    @PrimaryKey val id: String,
    val saleId: String,
    val amountCents: Long,
    val method: String,
    val paidAtMillis: Long? = null,
    val status: String,
    val notes: String = "",
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val category: String,
    val description: String,
    val amountCents: Long,
    val expenseDateMillis: Long,
    val isFixed: Boolean,
    val notes: String = "",
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "cost_resources")
data class CostResourceEntity(
    @PrimaryKey val id: String,
    val type: String,
    val name: String,
    val unit: String,
    val defaultRateCents: Long,
    val active: Boolean = true,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val updatedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "document_imports")
data class DocumentImportEntity(
    @PrimaryKey val id: String,
    val sourceUri: String,
    val sha256: String,
    val supplierName: String = "",
    val documentNumber: String = "",
    val importedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "quote_status_history", indices = [Index(value = ["quoteId"])] )
data class QuoteStatusHistoryEntity(
    @PrimaryKey val id: String,
    val quoteId: String,
    val previousStatus: String?,
    val newStatus: String,
    val changedAtMillis: Long = System.currentTimeMillis(),
    val reason: String = ""
)

@Entity(tableName = "production_status_history", indices = [Index(value = ["productionOrderId"])] )
data class ProductionStatusHistoryEntity(
    @PrimaryKey val id: String,
    val productionOrderId: String,
    val previousStatus: String?,
    val newStatus: String,
    val changedAtMillis: Long = System.currentTimeMillis(),
    val reason: String = ""
)

@Entity(tableName = "payment_status_history", indices = [Index(value = ["paymentId"])] )
data class PaymentStatusHistoryEntity(
    @PrimaryKey val id: String,
    val paymentId: String,
    val previousStatus: String?,
    val newStatus: String,
    val changedAtMillis: Long = System.currentTimeMillis(),
    val reason: String = ""
)
