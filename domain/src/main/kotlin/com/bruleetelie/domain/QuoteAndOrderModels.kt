package com.bruleetelie.domain

import java.math.BigDecimal

enum class QuoteStatus(val label: String) {
    Draft("Rascunho"),
    Sent("Enviado no WhatsApp"),
    Approved("Aprovado"),
    Rejected("Recusado"),
    Expired("Expirado"),
    ConvertedToSale("Convertido em Venda")
}

enum class ProductionStatus(val label: String) {
    Pending("Pendente"),
    InProgress("Em Produção"),
    Completed("Concluído"),
    Cancelled("Cancelado")
}

enum class PaymentStatus(val label: String) {
    Pending("Pendente"),
    Partial("Parcial"),
    Paid("Pago"),
    Refunded("Estornado")
}

data class Customer(
    val id: String,
    val name: String,
    val phone: String,
    val notes: String = ""
)

data class Quote(
    val id: String,
    val code: String, // ex: "#ORC-284"
    val customer: Customer,
    val productName: String,
    val productSubtitle: String,
    val unitsCount: Int,
    val pricing: PricingResult,
    val deliveryDate: String, // ex: "Sábado, 14 de Outubro - 16:00"
    val validDays: Int = 3,
    val notes: String = "",
    val status: QuoteStatus = QuoteStatus.Draft
)

data class ProductionStep(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false
)

data class ProductionOrder(
    val id: String,
    val quoteId: String,
    val quoteCode: String,
    val customerName: String,
    val productName: String,
    val deliveryTimeFormatted: String, // ex: "16:00 (Hoje)"
    val steps: List<ProductionStep>,
    val status: ProductionStatus = ProductionStatus.Pending,
    val estimatedCosts: RecipeCosts,
    val actualCosts: RecipeCosts? = null,
    val salePrice: Money,
    val activeProductionMinutes: Int = 0
) {
    val completedStepsCount: Int
        get() = steps.count { it.isCompleted }

    val totalStepsCount: Int
        get() = steps.size

    val isAllStepsDone: Boolean
        get() = steps.isNotEmpty() && steps.all { it.isCompleted }
}

data class SaleSnapshot(
    val id: String,
    val orderId: String,
    val quoteCode: String,
    val customerName: String,
    val productName: String,
    val salePrice: Money,
    val estimatedCost: Money,
    val actualCost: Money,
    val profit: Money,
    val marginPercent: BigDecimal,
    val completedAtFormatted: String,
    val paymentStatus: PaymentStatus = PaymentStatus.Paid,
    val activeProductionMinutes: Int = 0,
    val completedAtMillis: Long = System.currentTimeMillis()
)

data class ShoppingItem(
    val id: String,
    val name: String,
    val quantity: String,
    val isPurchased: Boolean = false,
    val createdAtMillis: Long = System.currentTimeMillis()
)
