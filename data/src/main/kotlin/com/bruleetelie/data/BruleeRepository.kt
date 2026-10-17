package com.bruleetelie.data

import com.bruleetelie.domain.*
import java.math.BigDecimal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BruleeRepository {

    private val _settings = MutableStateFlow(
        OperatingSettings(
            hourlyLaborRate = Money.fromReais("25.00"),
            gasCostPerHour = Money.fromReais("3.20"),
            electricityCostPerHour = Money.fromReais("2.50"),
            defaultMarginPercent = BigDecimal("35.00")
        )
    )
    val settings: StateFlow<OperatingSettings> = _settings.asStateFlow()

    private val _productionOrders = MutableStateFlow(initialOrders())
    val productionOrders: StateFlow<List<ProductionOrder>> = _productionOrders.asStateFlow()

    private val _quotes = MutableStateFlow(initialQuotes())
    val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    private val _completedSales = MutableStateFlow(initialSales())
    val completedSales: StateFlow<List<SaleSnapshot>> = _completedSales.asStateFlow()

    fun updateStepStatus(orderId: String, stepId: String, completed: Boolean) {
        _productionOrders.update { orders ->
            orders.map { order ->
                if (order.id == orderId) {
                    val updatedSteps = order.steps.map { step ->
                        if (step.id == stepId) step.copy(isCompleted = completed) else step
                    }
                    order.copy(
                        steps = updatedSteps,
                        status = if (updatedSteps.all { it.isCompleted }) ProductionStatus.Completed else ProductionStatus.InProgress
                    )
                } else order
            }
        }
    }

    fun completeOrder(orderId: String, actualCosts: RecipeCosts?): SaleSnapshot? {
        val order = _productionOrders.value.firstOrNull { it.id == orderId } ?: return null
        val finalActualCost = actualCosts ?: order.estimatedCosts
        val pricing = PricingEngine.fromSalePrice(finalActualCost, order.salePrice)

        val snapshot = SaleSnapshot(
            id = "sale-${System.currentTimeMillis()}",
            orderId = order.id,
            quoteCode = order.quoteCode,
            customerName = order.customerName,
            productName = order.productName,
            salePrice = order.salePrice,
            estimatedCost = order.estimatedCosts.total,
            actualCost = finalActualCost.total,
            profit = pricing.profit,
            marginPercent = pricing.marginPercent,
            completedAtFormatted = "Hoje"
        )

        _productionOrders.update { orders ->
            orders.map {
                if (it.id == orderId) it.copy(status = ProductionStatus.Completed, actualCosts = finalActualCost) else it
            }
        }

        _completedSales.update { it + snapshot }
        return snapshot
    }

    fun addQuote(quote: Quote) {
        _quotes.update { it + quote }
    }

    fun approveQuote(quoteId: String): ProductionOrder? {
        val quote = _quotes.value.firstOrNull { it.id == quoteId } ?: return null
        _quotes.update { list ->
            list.map { if (it.id == quoteId) it.copy(status = QuoteStatus.Approved) else it }
        }

        val newOrder = ProductionOrder(
            id = "order-${System.currentTimeMillis()}",
            quoteId = quote.id,
            quoteCode = quote.code,
            customerName = quote.customer.name,
            productName = "${quote.unitsCount}x ${quote.productName}",
            deliveryTimeFormatted = quote.deliveryDate,
            steps = listOf(
                ProductionStep("s1", "Massa cozida e pesada", isCompleted = false),
                ProductionStep("s2", "Boleamento finalizado", isCompleted = false),
                ProductionStep("s3", "Ornamentação e acabamento", isCompleted = false),
                ProductionStep("s4", "Embalagem cartonada e laço", isCompleted = false)
            ),
            status = ProductionStatus.Pending,
            estimatedCosts = quote.pricing.costs,
            salePrice = quote.pricing.salePrice
        )

        _productionOrders.update { listOf(newOrder) + it }
        return newOrder
    }

    private companion object {
        fun initialOrders(): List<ProductionOrder> {
            val brigadeiroCosts = RecipeCosts(
                ingredients = Money.fromReais("38.40"),
                labor = Money.fromReais("50.00"),
                energy = Money.fromReais("5.70"),
                packaging = Money.fromReais("4.50")
            )
            val boloCosts = RecipeCosts(
                ingredients = Money.fromReais("62.30"),
                labor = Money.fromReais("45.00"),
                energy = Money.fromReais("6.80"),
                packaging = Money.fromReais("8.00")
            )

            return listOf(
                ProductionOrder(
                    id = "ord-284",
                    quoteId = "quote-284",
                    quoteCode = "#ORC-284",
                    customerName = "Ana Souza",
                    productName = "40 Brigadeiros Gourmet (Pistache, Doce de Leite, Crème Brûlée)",
                    deliveryTimeFormatted = "16:00 (Hoje)",
                    steps = listOf(
                        ProductionStep("1", "Massa cozida e pesada (18g cada)", isCompleted = true),
                        ProductionStep("2", "Boleamento finalizado", isCompleted = true),
                        ProductionStep("3", "Ornamentação e acabamento", isCompleted = false, isCurrent = true),
                        ProductionStep("4", "Embalagem cartonada e laço", isCompleted = false)
                    ),
                    status = ProductionStatus.InProgress,
                    estimatedCosts = brigadeiroCosts,
                    salePrice = Money.fromReais("149.90")
                ),
                ProductionOrder(
                    id = "ord-282",
                    quoteId = "quote-282",
                    quoteCode = "#ORC-282",
                    customerName = "Carla Dias",
                    productName = "Bolo Brûlée Red Velvet 2kg",
                    deliveryTimeFormatted = "18:30 (Hoje)",
                    steps = listOf(
                        ProductionStep("1", "Massa assada e resfriada", isCompleted = true),
                        ProductionStep("2", "Recheio no descanso", isCompleted = true),
                        ProductionStep("3", "Montagem e prensagem", isCompleted = false, isCurrent = true),
                        ProductionStep("4", "Decoração e flores naturais", isCompleted = false)
                    ),
                    status = ProductionStatus.InProgress,
                    estimatedCosts = boloCosts,
                    salePrice = Money.fromReais("185.00")
                ),
                ProductionOrder(
                    id = "ord-285",
                    quoteId = "quote-285",
                    quoteCode = "#ORC-285",
                    customerName = "Mariana Lima",
                    productName = "Cento de Macarons Sortidos",
                    deliveryTimeFormatted = "14:00 (Amanhã)",
                    steps = listOf(
                        ProductionStep("1", "Separar insumos e peneirar farinha", isCompleted = false),
                        ProductionStep("2", "Macaronage e pingagem", isCompleted = false),
                        ProductionStep("3", "Secagem de cascas e forno", isCompleted = false),
                        ProductionStep("4", "Recheio e maturação 24h", isCompleted = false)
                    ),
                    status = ProductionStatus.Pending,
                    estimatedCosts = RecipeCosts(Money.fromReais("85.00"), Money.fromReais("70.00"), Money.fromReais("9.00"), Money.fromReais("12.00")),
                    salePrice = Money.fromReais("280.00")
                )
            )
        }

        fun initialQuotes(): List<Quote> {
            val costs = RecipeCosts(
                ingredients = Money.fromReais("38.40"),
                labor = Money.fromReais("50.00"),
                energy = Money.fromReais("5.70"),
                packaging = Money.fromReais("4.50")
            )
            val pricing = PricingEngine.fromSalePrice(costs, Money.fromReais("149.90"))

            return listOf(
                Quote(
                    id = "quote-284",
                    code = "#ORC-284",
                    customer = Customer("c1", "Ana Souza", "(11) 98765-4321"),
                    productName = "40 Brigadeiros Gourmet",
                    productSubtitle = "Pistache tostado, Doce de Leite com Flor de Sal, Belga e Crème Brûlée",
                    unitsCount = 40,
                    pricing = pricing,
                    deliveryDate = "Sábado, 14 de Outubro - 16:00",
                    validDays = 3,
                    notes = "Caixa Telie Ouro com laço acetinado e entrega inclusa.",
                    status = QuoteStatus.Sent
                )
            )
        }

        fun initialSales(): List<SaleSnapshot> {
            return listOf(
                SaleSnapshot(
                    id = "s-1",
                    orderId = "ord-270",
                    quoteCode = "#ORC-270",
                    customerName = "Beatriz Rocha",
                    productName = "Bolo Nozes & Baba de Moça 2.5kg",
                    salePrice = Money.fromReais("220.00"),
                    estimatedCost = Money.fromReais("82.10"),
                    actualCost = Money.fromReais("82.10"),
                    profit = Money.fromReais("137.90"),
                    marginPercent = BigDecimal("62.7"),
                    completedAtFormatted = "Ontem"
                ),
                SaleSnapshot(
                    id = "s-2",
                    orderId = "ord-268",
                    quoteCode = "#ORC-268",
                    customerName = "Felipe Ramos",
                    productName = "Caixa Telie Degustação 16 un",
                    salePrice = Money.fromReais("68.00"),
                    estimatedCost = Money.fromReais("26.40"),
                    actualCost = Money.fromReais("26.40"),
                    profit = Money.fromReais("41.60"),
                    marginPercent = BigDecimal("61.1"),
                    completedAtFormatted = "3 dias atrás"
                )
            )
        }
    }
}
