package com.bruleetelie.app.ui.screens

import androidx.compose.runtime.saveable.listSaver
import com.bruleetelie.domain.*

internal val IngredientListSaver = listSaver<List<RecipeIngredientItem>, String>(
    save = { list -> list.flatMap { item -> listOf(item.id, item.name, item.packageQuantity.value.toPlainString(), item.packageQuantity.unit.name, item.packagePrice.cents.toString(), item.usedQuantity.value.toPlainString(), item.usedQuantity.unit.name) } },
    restore = { list -> list.chunked(7).map { RecipeIngredientItem(it[0], it[1], Quantity(it[2].toBigDecimal(), UnitOfMeasure.valueOf(it[3])), Money(it[4].toLong()), Quantity(it[5].toBigDecimal(), UnitOfMeasure.valueOf(it[6]))) } }
)
internal val TimesSaver = listSaver<OperationalTimes, String>(
    save = { listOf(it.prepTime.value.toPlainString(), it.cookTime.value.toPlainString(), it.decorationTime.value.toPlainString(), it.energySource.name, it.complexity.name) },
    restore = { OperationalTimes(Quantity.minutes(it[0].toBigDecimal()), Quantity.minutes(it[1].toBigDecimal()), Quantity.minutes(it[2].toBigDecimal()), EnergySource.valueOf(it[3]), Complexity.valueOf(it[4])) }
)
