package com.bruleetelie.domain

import java.math.BigDecimal
import java.math.RoundingMode

enum class Dimension {
    Mass,
    Volume,
    Count,
    Time
}

enum class UnitOfMeasure(
    val symbol: String,
    val dimension: Dimension,
    val baseMultiplier: BigDecimal
) {
    // Mass (base: gram)
    Gram("g", Dimension.Mass, BigDecimal.ONE),
    Kilogram("kg", Dimension.Mass, BigDecimal(1000)),

    // Volume (base: milliliter)
    Milliliter("ml", Dimension.Volume, BigDecimal.ONE),
    Liter("L", Dimension.Volume, BigDecimal(1000)),

    // Count (base: unit)
    Unit("un", Dimension.Count, BigDecimal.ONE),
    Dozen("dz", Dimension.Count, BigDecimal(12)),

    // Time (base: minute)
    Minute("min", Dimension.Time, BigDecimal.ONE),
    Hour("h", Dimension.Time, BigDecimal(60));

    companion object {
        fun fromSymbol(symbol: String): UnitOfMeasure? =
            entries.firstOrNull { it.symbol.equals(symbol.trim(), ignoreCase = true) }
    }
}

data class Quantity(
    val value: BigDecimal,
    val unit: UnitOfMeasure
) : Comparable<Quantity> {

    init {
        require(value >= BigDecimal.ZERO) { "Quantidade não pode ser negativa: $value" }
    }

    val baseValue: BigDecimal
        get() = value.multiply(unit.baseMultiplier)

    fun convertTo(targetUnit: UnitOfMeasure): Quantity {
        require(unit.dimension == targetUnit.dimension) {
            "Não é possível converter entre dimensões incompatíveis: ${unit.dimension} para ${targetUnit.dimension}"
        }
        if (unit == targetUnit) return this

        val inBase = baseValue
        val converted = inBase.divide(targetUnit.baseMultiplier, 6, RoundingMode.HALF_EVEN)
        return Quantity(converted.stripTrailingZeros(), targetUnit)
    }

    operator fun plus(other: Quantity): Quantity {
        require(unit.dimension == other.unit.dimension) { "Dimensões incompatíveis" }
        val otherInSameUnit = other.convertTo(unit)
        return Quantity(value.add(otherInSameUnit.value), unit)
    }

    operator fun minus(other: Quantity): Quantity {
        require(unit.dimension == other.unit.dimension) { "Dimensões incompatíveis" }
        val otherInSameUnit = other.convertTo(unit)
        val result = value.subtract(otherInSameUnit.value)
        require(result >= BigDecimal.ZERO) { "Resultado da subtração de quantidade não pode ser negativo" }
        return Quantity(result, unit)
    }

    override fun compareTo(other: Quantity): Int {
        require(unit.dimension == other.unit.dimension) { "Não é possível comparar dimensões distintas" }
        return baseValue.compareTo(other.baseValue)
    }

    fun formatted(): String {
        val display = value.stripTrailingZeros().toPlainString()
        return "$display ${unit.symbol}"
    }

    companion object {
        fun grams(amount: Number) = Quantity(BigDecimal(amount.toString()), UnitOfMeasure.Gram)
        fun kilograms(amount: Number) = Quantity(BigDecimal(amount.toString()), UnitOfMeasure.Kilogram)
        fun milliliters(amount: Number) = Quantity(BigDecimal(amount.toString()), UnitOfMeasure.Milliliter)
        fun liters(amount: Number) = Quantity(BigDecimal(amount.toString()), UnitOfMeasure.Liter)
        fun units(amount: Number) = Quantity(BigDecimal(amount.toString()), UnitOfMeasure.Unit)
        fun minutes(amount: Number) = Quantity(BigDecimal(amount.toString()), UnitOfMeasure.Minute)
        fun hours(amount: Number) = Quantity(BigDecimal(amount.toString()), UnitOfMeasure.Hour)

        /**
         * Calcula o custo proporcional utilizado:
         * custo = (precoTotalCompra * (quantidadeUsada / quantidadeComprada))
         */
        fun calculateProportionalCost(
            packageQuantity: Quantity,
            packagePrice: Money,
            usedQuantity: Quantity
        ): Money {
            require(packageQuantity.baseValue > BigDecimal.ZERO) { "Quantidade da embalagem deve ser positiva" }
            require(packageQuantity.unit.dimension == usedQuantity.unit.dimension) {
                "Unidades incompatíveis entre compra (${packageQuantity.unit.symbol}) e uso (${usedQuantity.unit.symbol})"
            }

            val usedBase = usedQuantity.baseValue
            val packageBase = packageQuantity.baseValue
            val ratio = usedBase.divide(packageBase, 10, RoundingMode.HALF_EVEN)

            return packagePrice.multiply(ratio)
        }
    }
}
