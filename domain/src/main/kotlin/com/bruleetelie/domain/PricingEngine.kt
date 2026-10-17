package com.bruleetelie.domain

import java.math.BigDecimal
import java.math.RoundingMode

data class RecipeCosts(
    val ingredients: Money,
    val labor: Money,
    val energy: Money,
    val packaging: Money,
    val overhead: Money = Money.Zero,
) {
    val total: Money
        get() = ingredients + labor + energy + packaging + overhead
}

data class PricingResult(
    val costs: RecipeCosts,
    val salePrice: Money,
    val profit: Money,
    val marginPercent: BigDecimal,
    val markup: BigDecimal = calculateMarkup(salePrice, costs.total)
) {
    companion object {
        fun calculateMarkup(salePrice: Money, totalCost: Money): BigDecimal {
            if (totalCost.cents == 0L) return BigDecimal.ZERO
            return BigDecimal(salePrice.cents).divide(BigDecimal(totalCost.cents), 2, RoundingMode.HALF_EVEN)
        }
    }
}

data class CostComparisonResult(
    val estimatedCosts: RecipeCosts,
    val actualCosts: RecipeCosts,
    val costDifference: Money,
    val isCostHigher: Boolean,
    val salePrice: Money,
    val estimatedProfit: Money,
    val actualProfit: Money,
    val estimatedMarginPercent: BigDecimal,
    val actualMarginPercent: BigDecimal,
    val marginDifferencePercent: BigDecimal
)

object PricingEngine {
    fun fromTargetMargin(costs: RecipeCosts, targetMarginPercent: BigDecimal): PricingResult {
        require(targetMarginPercent >= BigDecimal.ZERO && targetMarginPercent < BigDecimal(100))
        val margin = targetMarginPercent.movePointLeft(2)
        val divisor = BigDecimal.ONE.subtract(margin)
        val price = Money(
            BigDecimal(costs.total.cents).divide(divisor, 0, RoundingMode.CEILING).longValueExact()
        )
        val profit = price - costs.total
        val actualMargin = if (price.cents == 0L) BigDecimal.ZERO else
            BigDecimal(profit.cents).divide(BigDecimal(price.cents), 4, RoundingMode.HALF_EVEN)
                .movePointRight(2)
        return PricingResult(costs, price, profit, actualMargin)
    }

    fun fromSalePrice(costs: RecipeCosts, salePrice: Money): PricingResult {
        val profit = salePrice - costs.total
        val marginPercent = if (salePrice.cents <= 0L) BigDecimal.ZERO else
            BigDecimal(profit.cents).divide(BigDecimal(salePrice.cents), 4, RoundingMode.HALF_EVEN)
                .movePointRight(2)
        return PricingResult(costs, salePrice, profit, marginPercent)
    }

    fun fromTargetMarkup(costs: RecipeCosts, markupMultiplier: BigDecimal): PricingResult {
        require(markupMultiplier >= BigDecimal.ONE) { "Markup deve ser pelo menos 1.0" }
        val price = costs.total.multiply(markupMultiplier)
        return fromSalePrice(costs, price)
    }

    fun compareEstimatedVsActual(
        estimated: RecipeCosts,
        actual: RecipeCosts,
        salePrice: Money
    ): CostComparisonResult {
        val diffCents = actual.total.cents - estimated.total.cents
        val isHigher = diffCents > 0
        val costDifference = Money(kotlin.math.abs(diffCents))

        val estProfit = salePrice - estimated.total
        val actProfit = salePrice - actual.total

        val estMargin = if (salePrice.cents == 0L) BigDecimal.ZERO else
            BigDecimal(estProfit.cents).divide(BigDecimal(salePrice.cents), 4, RoundingMode.HALF_EVEN).movePointRight(2)

        val actMargin = if (salePrice.cents == 0L) BigDecimal.ZERO else
            BigDecimal(actProfit.cents).divide(BigDecimal(salePrice.cents), 4, RoundingMode.HALF_EVEN).movePointRight(2)

        val marginDiff = actMargin.subtract(estMargin)

        return CostComparisonResult(
            estimatedCosts = estimated,
            actualCosts = actual,
            costDifference = costDifference,
            isCostHigher = isHigher,
            salePrice = salePrice,
            estimatedProfit = estProfit,
            actualProfit = actProfit,
            estimatedMarginPercent = estMargin,
            actualMarginPercent = actMargin,
            marginDifferencePercent = marginDiff
        )
    }
}
