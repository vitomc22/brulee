package com.bruleetelie.domain

import java.math.BigDecimal
import java.math.RoundingMode

enum class Complexity(val label: String, val multiplier: BigDecimal) {
    Simples("Simples", BigDecimal("1.00")),
    Normal("Normal", BigDecimal("1.15")),
    Trabalhosa("Trabalhosa", BigDecimal("1.30")),
    Complexa("Complexa", BigDecimal("1.50"))
}

enum class EnergySource(val label: String) {
    Gas("Fogão / Gás"),
    Electric("Forno Elétrico"),
    Both("Ambos (Gás e Elétrico)")
}

data class OperatingSettings(
    val hourlyLaborRate: Money = Money.fromReais("25.00"),
    val gasCostPerHour: Money = Money.fromReais("3.20"),
    val electricityCostPerHour: Money = Money.fromReais("2.50"),
    val defaultMarginPercent: BigDecimal = BigDecimal("35.00")
)

data class OperationalTimes(
    val prepTime: Quantity = Quantity.minutes(45),
    val cookTime: Quantity = Quantity.minutes(30),
    val decorationTime: Quantity = Quantity.minutes(15),
    val energySource: EnergySource = EnergySource.Gas,
    val complexity: Complexity = Complexity.Normal
) {
    val totalTimeMinutes: BigDecimal
        get() = prepTime.convertTo(UnitOfMeasure.Minute).value
            .add(cookTime.convertTo(UnitOfMeasure.Minute).value)
            .add(decorationTime.convertTo(UnitOfMeasure.Minute).value)

    val laborHours: BigDecimal
        get() {
            val humanMinutes = prepTime.convertTo(UnitOfMeasure.Minute).value
                .add(decorationTime.convertTo(UnitOfMeasure.Minute).value)
            return humanMinutes.divide(BigDecimal(60), 4, RoundingMode.HALF_EVEN)
        }

    val cookHours: BigDecimal
        get() = cookTime.convertTo(UnitOfMeasure.Minute).value
            .divide(BigDecimal(60), 4, RoundingMode.HALF_EVEN)
}

data class RecipeIngredientItem(
    val id: String,
    val name: String,
    val packageQuantity: Quantity,
    val packagePrice: Money,
    val usedQuantity: Quantity
) {
    val proportionalCost: Money
        get() = Quantity.calculateProportionalCost(packageQuantity, packagePrice, usedQuantity)
}

object RecipeCostCalculator {
    fun calculate(
        ingredients: List<RecipeIngredientItem>,
        times: OperationalTimes,
        settings: OperatingSettings,
        packaging: Money = Money.Zero,
        overhead: Money = Money.Zero
    ): RecipeCosts {
        val totalIngredients = ingredients.fold(Money.Zero) { acc, item -> acc + item.proportionalCost }

        // Labor cost = laborHours * hourlyLaborRate * complexityMultiplier
        val laborFactor = times.laborHours.multiply(times.complexity.multiplier)
        val laborCost = settings.hourlyLaborRate.multiply(laborFactor)

        // Energy / gas
        val gasCost = when (times.energySource) {
            EnergySource.Gas, EnergySource.Both -> settings.gasCostPerHour.multiply(times.cookHours)
            EnergySource.Electric -> Money.Zero
        }

        val electricCost = when (times.energySource) {
            EnergySource.Electric, EnergySource.Both -> settings.electricityCostPerHour.multiply(times.cookHours)
            EnergySource.Gas -> Money.Zero
        }

        val totalEnergy = gasCost + electricCost

        return RecipeCosts(
            ingredients = totalIngredients,
            labor = laborCost,
            energy = totalEnergy,
            packaging = packaging,
            overhead = overhead
        )
    }
}
