package com.bruleetelie.data

import com.bruleetelie.domain.*
import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BruleeRepositoryTest {

    @Test
    fun `test repository initializes with sample orders and quotes`() {
        val repo = BruleeRepository()
        val orders = repo.productionOrders.value
        val quotes = repo.quotes.value
        val sales = repo.completedSales.value

        assertTrue(orders.isNotEmpty())
        assertTrue(quotes.isNotEmpty())
        assertTrue(sales.isNotEmpty())

        val order = orders.first()
        assertEquals("#ORC-284", order.quoteCode)
        assertEquals("Ana Souza", order.customerName)
        assertEquals(4, order.totalStepsCount)
    }

    @Test
    fun `test updateStepStatus updates step completion and marks order completed when all done`() {
        val repo = BruleeRepository()
        val order = repo.productionOrders.value.first()

        // Complete remaining steps 3 and 4
        repo.updateStepStatus(order.id, "3", true)
        repo.updateStepStatus(order.id, "4", true)

        val updatedOrder = repo.productionOrders.value.first { it.id == order.id }
        assertTrue(updatedOrder.isAllStepsDone)
        assertEquals(ProductionStatus.Completed, updatedOrder.status)
    }

    @Test
    fun `test completeOrder creates immutable SaleSnapshot with correct profit`() {
        val repo = BruleeRepository()
        val order = repo.productionOrders.value.first()

        val actualCosts = RecipeCosts(
            ingredients = Money.fromReais("35.00"),
            labor = Money.fromReais("50.00"),
            energy = Money.fromReais("5.00"),
            packaging = Money.fromReais("4.00")
        ) // R$ 94,00

        val snapshot = repo.completeOrder(order.id, actualCosts)
        assertNotNull(snapshot)
        assertEquals(order.quoteCode, snapshot!!.quoteCode)
        assertEquals(9400L, snapshot.actualCost.cents)
        assertEquals(Money.fromReais("149.90"), snapshot.salePrice)

        // Profit = 149.90 - 94.00 = 55.90
        assertEquals(5590L, snapshot.profit.cents)

        // Sale is added to completed sales list
        assertTrue(repo.completedSales.value.any { it.id == snapshot.id })
    }

    @Test
    fun `test approving a quote generates a new production order`() {
        val repo = BruleeRepository()
        val quote = Quote(
            id = "q-test-1",
            code = "#ORC-999",
            customer = Customer("c-new", "Juliana Paes", "(21) 99999-8888"),
            productName = "Torta Holandesa 2kg",
            productSubtitle = "Com ganache de chocolate meio amargo",
            unitsCount = 1,
            pricing = PricingResult(
                costs = RecipeCosts(Money.fromReais("40.00"), Money.fromReais("30.00"), Money.fromReais("5.00"), Money.fromReais("5.00")),
                salePrice = Money.fromReais("120.00"),
                profit = Money.fromReais("40.00"),
                marginPercent = BigDecimal("33.33")
            ),
            deliveryDate = "Domingo, 10:00",
            status = QuoteStatus.Sent
        )

        repo.addQuote(quote)
        val order = repo.approveQuote(quote.id)

        assertNotNull(order)
        assertEquals("#ORC-999", order!!.quoteCode)
        assertEquals("Juliana Paes", order.customerName)
        assertEquals(ProductionStatus.Pending, order.status)
        assertTrue(repo.productionOrders.value.any { it.quoteCode == "#ORC-999" })
    }

    @Test
    fun `test addIngredient adds ingredient to repository state`() {
        val repo = BruleeRepository()
        val initialCount = repo.ingredients.value.size
        repo.addIngredient("Farinha de Amêndoas", "g")

        val updatedIngredients = repo.ingredients.value
        assertEquals(initialCount + 1, updatedIngredients.size)
        assertTrue(updatedIngredients.any { it.name == "Farinha de Amêndoas" })
    }

    @Test
    fun `test updateSettings updates operating settings`() {
        val repo = BruleeRepository()
        val newSettings = OperatingSettings(
            hourlyLaborRate = Money.fromReais("30.00"),
            gasCostPerHour = Money.fromReais("4.00"),
            electricityCostPerHour = Money.fromReais("3.00"),
            defaultMarginPercent = BigDecimal("40.00")
        )
        repo.updateSettings(newSettings)
        assertEquals(3000L, repo.settings.value.hourlyLaborRate.cents)
        assertEquals(BigDecimal("40.00"), repo.settings.value.defaultMarginPercent)
    }
}
