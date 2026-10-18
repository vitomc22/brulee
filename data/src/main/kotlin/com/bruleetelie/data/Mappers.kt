package com.bruleetelie.data

import com.bruleetelie.data.db.*
import com.bruleetelie.domain.*
import java.math.BigDecimal

fun Quote.toEntity(): QuoteEntity {
    return QuoteEntity(
        id = id,
        code = code,
        customerId = customer.id,
        customerName = customer.name,
        customerPhone = customer.phone,
        productName = productName,
        productSubtitle = productSubtitle,
        unitsCount = unitsCount,
        ingredientsCostCents = pricing.costs.ingredients.cents,
        laborCostCents = pricing.costs.labor.cents,
        energyCostCents = pricing.costs.energy.cents,
        packagingCostCents = pricing.costs.packaging.cents,
        overheadCostCents = pricing.costs.overhead.cents,
        salePriceCents = pricing.salePrice.cents,
        profitCents = pricing.profit.cents,
        marginPercent = pricing.marginPercent.toPlainString(),
        deliveryDate = deliveryDate,
        validDays = validDays,
        notes = notes,
        status = status.name
    )
}

fun QuoteEntity.toDomain(): Quote {
    val costs = RecipeCosts(
        ingredients = Money(ingredientsCostCents),
        labor = Money(laborCostCents),
        energy = Money(energyCostCents),
        packaging = Money(packagingCostCents),
        overhead = Money(overheadCostCents)
    )
    val pricing = PricingResult(
        costs = costs,
        salePrice = Money(salePriceCents),
        profit = Money(profitCents),
        marginPercent = BigDecimal(marginPercent)
    )
    val quoteStatus = try {
        QuoteStatus.valueOf(status)
    } catch (_: Exception) {
        QuoteStatus.Draft
    }

    return Quote(
        id = id,
        code = code,
        customer = Customer(customerId, customerName, customerPhone),
        productName = productName,
        productSubtitle = productSubtitle,
        unitsCount = unitsCount,
        pricing = pricing,
        deliveryDate = deliveryDate,
        validDays = validDays,
        notes = notes,
        status = quoteStatus
    )
}

fun ProductionOrder.toEntity(): ProductionOrderEntity {
    return ProductionOrderEntity(
        id = id,
        quoteId = quoteId,
        quoteCode = quoteCode,
        customerName = customerName,
        productName = productName,
        deliveryTimeFormatted = deliveryTimeFormatted,
        status = status.name,
        estIngredientsCents = estimatedCosts.ingredients.cents,
        estLaborCents = estimatedCosts.labor.cents,
        estEnergyCents = estimatedCosts.energy.cents,
        estPackagingCents = estimatedCosts.packaging.cents,
        estOverheadCents = estimatedCosts.overhead.cents,
        actIngredientsCents = actualCosts?.ingredients?.cents,
        actLaborCents = actualCosts?.labor?.cents,
        actEnergyCents = actualCosts?.energy?.cents,
        actPackagingCents = actualCosts?.packaging?.cents,
        actOverheadCents = actualCosts?.overhead?.cents,
        salePriceCents = salePrice.cents
    )
}

fun ProductionOrderEntity.toDomain(steps: List<ProductionStepEntity>): ProductionOrder {
    val estCosts = RecipeCosts(
        ingredients = Money(estIngredientsCents),
        labor = Money(estLaborCents),
        energy = Money(estEnergyCents),
        packaging = Money(estPackagingCents),
        overhead = Money(estOverheadCents)
    )

    val actCosts = if (actIngredientsCents != null && actLaborCents != null && actEnergyCents != null && actPackagingCents != null) {
        RecipeCosts(
            ingredients = Money(actIngredientsCents),
            labor = Money(actLaborCents),
            energy = Money(actEnergyCents),
            packaging = Money(actPackagingCents),
            overhead = Money(actOverheadCents ?: 0L)
        )
    } else null

    val orderStatus = try {
        ProductionStatus.valueOf(status)
    } catch (_: Exception) {
        ProductionStatus.Pending
    }

    return ProductionOrder(
        id = id,
        quoteId = quoteId,
        quoteCode = quoteCode,
        customerName = customerName,
        productName = productName,
        deliveryTimeFormatted = deliveryTimeFormatted,
        steps = steps.sortedBy { it.stepOrder }.map { ProductionStep(it.id, it.title, it.isCompleted, it.isCurrent) },
        status = orderStatus,
        estimatedCosts = estCosts,
        actualCosts = actCosts,
        salePrice = Money(salePriceCents)
    )
}

fun ProductionStep.toEntity(orderId: String, index: Int): ProductionStepEntity {
    return ProductionStepEntity(
        id = id,
        orderId = orderId,
        stepOrder = index,
        title = title,
        isCompleted = isCompleted,
        isCurrent = isCurrent
    )
}

fun SaleSnapshot.toEntity(): SaleSnapshotEntity {
    return SaleSnapshotEntity(
        id = id,
        orderId = orderId,
        quoteCode = quoteCode,
        customerName = customerName,
        productName = productName,
        salePriceCents = salePrice.cents,
        estimatedCostCents = estimatedCost.cents,
        actualCostCents = actualCost.cents,
        profitCents = profit.cents,
        marginPercent = marginPercent.toPlainString(),
        completedAtFormatted = completedAtFormatted,
        paymentStatus = paymentStatus.name,
        activeProductionMinutes = activeProductionMinutes,
        completedAtMillis = completedAtMillis
    )
}

fun SaleSnapshotEntity.toDomain(): SaleSnapshot {
    val status = try {
        PaymentStatus.valueOf(paymentStatus)
    } catch (_: Exception) {
        PaymentStatus.Paid
    }

    return SaleSnapshot(
        id = id,
        orderId = orderId,
        quoteCode = quoteCode,
        customerName = customerName,
        productName = productName,
        salePrice = Money(salePriceCents),
        estimatedCost = Money(estimatedCostCents),
        actualCost = Money(actualCostCents),
        profit = Money(profitCents),
        marginPercent = BigDecimal(marginPercent),
        completedAtFormatted = completedAtFormatted,
        paymentStatus = status,
        activeProductionMinutes = activeProductionMinutes,
        completedAtMillis = completedAtMillis
    )
}

fun ShoppingItem.toEntity(): ShoppingItemEntity = ShoppingItemEntity(
    id = id,
    name = name,
    quantity = quantity,
    isPurchased = isPurchased,
    createdAtMillis = createdAtMillis
)

fun ShoppingItemEntity.toDomain(): ShoppingItem = ShoppingItem(
    id = id,
    name = name,
    quantity = quantity,
    isPurchased = isPurchased,
    createdAtMillis = createdAtMillis
)
