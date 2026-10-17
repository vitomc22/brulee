package com.bruleetelie.domain

import java.math.BigDecimal
import java.math.RoundingMode

data class FinancialSummaryMetrics(
    val totalRevenue: Money,
    val totalCost: Money,
    val totalProfit: Money,
    val averageMarginPercent: BigDecimal,
    val averageTicket: Money,
    val salesCount: Int,
    val conversionRatePercent: BigDecimal,
    val profitPerHour: Money
)

data class BreakEvenResult(
    val fixedCosts: Money,
    val contributionMarginRatio: BigDecimal,
    val breakEvenRevenue: Money
)

data class PriceAlert(
    val ingredientName: String,
    val oldPrice: Money,
    val newPrice: Money,
    val variationPercent: BigDecimal,
    val isIncrease: Boolean
)

object FinancialAnalysisEngine {
    fun calculateSummary(
        sales: List<SaleSnapshot>,
        totalProductionHours: BigDecimal,
        approvedQuotesCount: Int,
        totalEligibleQuotesCount: Int
    ): FinancialSummaryMetrics {
        if (sales.isEmpty()) {
            return FinancialSummaryMetrics(
                totalRevenue = Money.Zero,
                totalCost = Money.Zero,
                totalProfit = Money.Zero,
                averageMarginPercent = BigDecimal.ZERO,
                averageTicket = Money.Zero,
                salesCount = 0,
                conversionRatePercent = BigDecimal.ZERO,
                profitPerHour = Money.Zero
            )
        }

        var totalRevCents = 0L
        var totalCostCents = 0L
        var totalProfitCents = 0L
        var marginSum = BigDecimal.ZERO

        for (sale in sales) {
            totalRevCents += sale.salePrice.cents
            totalCostCents += sale.actualCost.cents
            totalProfitCents += sale.profit.cents
            marginSum = marginSum.add(sale.marginPercent)
        }

        val count = sales.size
        val totalRevenue = Money(totalRevCents)
        val totalCost = Money(totalCostCents)
        val totalProfit = Money(totalProfitCents)
        val averageMargin = marginSum.divide(BigDecimal(count), 2, RoundingMode.HALF_EVEN)
        val averageTicket = Money(totalRevCents / count)

        val conversion = if (totalEligibleQuotesCount == 0) BigDecimal.ZERO else
            BigDecimal(approvedQuotesCount).divide(BigDecimal(totalEligibleQuotesCount), 4, RoundingMode.HALF_EVEN).movePointRight(2)

        val profitPerHour = if (totalProductionHours.compareTo(BigDecimal.ZERO) == 0) Money.Zero else
            Money((BigDecimal(totalProfitCents).divide(totalProductionHours, 0, RoundingMode.HALF_EVEN)).longValueExact())

        return FinancialSummaryMetrics(
            totalRevenue = totalRevenue,
            totalCost = totalCost,
            totalProfit = totalProfit,
            averageMarginPercent = averageMargin,
            averageTicket = averageTicket,
            salesCount = count,
            conversionRatePercent = conversion,
            profitPerHour = profitPerHour
        )
    }

    fun calculateBreakEven(
        fixedCosts: Money,
        totalRevenue: Money,
        totalVariableCosts: Money
    ): BreakEvenResult {
        if (totalRevenue.cents <= 0L) {
            return BreakEvenResult(fixedCosts, BigDecimal.ZERO, Money.Zero)
        }
        val contributionMargin = totalRevenue.cents - totalVariableCosts.cents
        if (contributionMargin <= 0L) {
            return BreakEvenResult(fixedCosts, BigDecimal.ZERO, Money.Zero)
        }
        val cmRatio = BigDecimal(contributionMargin).divide(BigDecimal(totalRevenue.cents), 4, RoundingMode.HALF_EVEN)
        val breakEvenCents = BigDecimal(fixedCosts.cents).divide(cmRatio, 0, RoundingMode.CEILING).longValueExact()

        return BreakEvenResult(
            fixedCosts = fixedCosts,
            contributionMarginRatio = cmRatio.movePointRight(2),
            breakEvenRevenue = Money(breakEvenCents)
        )
    }

    fun analyzePriceVariation(
        ingredientName: String,
        oldPrice: Money,
        newPrice: Money
    ): PriceAlert {
        val diffCents = newPrice.cents - oldPrice.cents
        val isIncrease = diffCents > 0
        val variation = if (oldPrice.cents == 0L) BigDecimal.ZERO else
            BigDecimal(kotlin.math.abs(diffCents)).divide(BigDecimal(oldPrice.cents), 4, RoundingMode.HALF_EVEN).movePointRight(2)

        return PriceAlert(
            ingredientName = ingredientName,
            oldPrice = oldPrice,
            newPrice = newPrice,
            variationPercent = variation,
            isIncrease = isIncrease
        )
    }
}
