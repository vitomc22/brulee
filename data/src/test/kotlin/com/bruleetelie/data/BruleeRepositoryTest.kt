package com.bruleetelie.data

import com.bruleetelie.domain.*
import org.junit.Assert.*
import org.junit.Test

class BruleeRepositoryTest {
    private fun quote(id: String = "q1") = Quote(id, "#ORC-$id", Customer("c1", "Cliente real", "11987654321"), "Bolo", "", 1,
        PricingEngine.fromSalePrice(RecipeCosts(Money(4000), Money(3000), Money(500), Money(500)), Money(12000)), "21/10/2026 16:00")
    private fun order(repo: BruleeRepository): ProductionOrder {
        repo.addQuote(quote())
        return repo.approveQuote("q1")!!
    }
    private fun checkAll(repo: BruleeRepository, order: ProductionOrder) {
        order.steps.forEach { repo.updateStepStatus(order.id, it.id, true) }
    }
    @Test fun `fresh repository has no demonstration data`() {
        val repo = BruleeRepository()
        assertTrue(repo.productionOrders.value.isEmpty()); assertTrue(repo.quotes.value.isEmpty())
        assertTrue(repo.completedSales.value.isEmpty()); assertTrue(repo.ingredients.value.isEmpty()); assertTrue(repo.recipes.value.isEmpty())
    }
    @Test fun `saving and sharing same quote upserts rather than duplicates`() {
        val repo = BruleeRepository()
        repo.addQuote(quote()); repo.addQuote(quote().copy(status = QuoteStatus.Sent))
        assertEquals(1, repo.quotes.value.size); assertEquals(QuoteStatus.Sent, repo.quotes.value.single().status)
    }
    @Test fun `repeated approval returns same production order`() {
        val repo = BruleeRepository(); val first = order(repo)
        assertEquals(first.id, repo.approveQuote("q1")!!.id)
        assertEquals(1, repo.productionOrders.value.size)
    }
    @Test fun `checking all steps does not finalize or book revenue`() {
        val repo = BruleeRepository(); val order = order(repo); checkAll(repo, order)
        assertTrue(repo.productionOrders.value.single().isAllStepsDone)
        assertEquals(ProductionStatus.InProgress, repo.productionOrders.value.single().status)
        assertTrue(repo.completedSales.value.isEmpty())
    }
    @Test fun `cannot finalize incomplete order`() {
        val repo = BruleeRepository(); val order = order(repo)
        assertNull(repo.completeOrder(order.id, null)); assertTrue(repo.completedSales.value.isEmpty())
    }
    @Test fun `finalization is idempotent preserves estimate and computes actual profit`() {
        val repo = BruleeRepository(); val order = order(repo); checkAll(repo, order)
        val actual = RecipeCosts(Money(3500), Money(3000), Money(500), Money(400))
        val sale = repo.completeOrder(order.id, actual, 90)!!
        assertEquals(sale, repo.completeOrder(order.id, order.estimatedCosts))
        assertEquals(90, sale.activeProductionMinutes)
        assertEquals(1, repo.completedSales.value.size); assertEquals(7400L, sale.actualCost.cents)
        assertEquals(8000L, sale.estimatedCost.cents); assertEquals(4600L, sale.profit.cents)
        assertEquals(QuoteStatus.ConvertedToSale, repo.quotes.value.single().status)
        assertEquals(order.estimatedCosts, repo.productionOrders.value.single().estimatedCosts)
    }
    @Test fun `sharing approved or completed quote cannot downgrade status`() {
        val repo = BruleeRepository(); val order = order(repo)
        repo.addQuote(quote().copy(status = QuoteStatus.Sent))
        assertEquals(QuoteStatus.Approved, repo.quotes.value.single().status)
        checkAll(repo, order); repo.completeOrder(order.id, null)
        repo.addQuote(quote())
        assertEquals(QuoteStatus.ConvertedToSale, repo.quotes.value.single().status)
    }
    @Test fun `rejecting a quote prevents approval`() {
        val repo = BruleeRepository(); repo.addQuote(quote()); repo.updateQuoteStatus("q1", QuoteStatus.Rejected)
        assertNull(repo.approveQuote("q1")); assertTrue(repo.productionOrders.value.isEmpty())
    }
    @Test fun `checklist changes are isolated and current step follows progress`() {
        val repo = BruleeRepository(); val order = order(repo)
        repo.addQuote(quote("q2")); val second = repo.approveQuote("q2")!!
        repo.updateStepStatus(order.id, order.steps.first().id, true)
        assertTrue(repo.productionOrders.value.first { it.id == order.id }.steps[1].isCurrent)
        assertEquals(0, repo.productionOrders.value.first { it.id == second.id }.completedStepsCount)
        repo.updateStepStatus(order.id, order.steps.first().id, false)
        assertTrue(repo.productionOrders.value.first { it.id == order.id }.steps[0].isCurrent)
    }
    @Test fun `completed order checklist is immutable`() {
        val repo = BruleeRepository(); val order = order(repo); checkAll(repo, order); repo.completeOrder(order.id, null)
        repo.updateStepStatus(order.id, order.steps.first().id, false)
        assertTrue(repo.productionOrders.value.single().isAllStepsDone)
    }
    @Test fun `new OCR ingredient is created and duplicate spelling reuses identity`() {
        val repo = BruleeRepository()
        val ingredient = repo.addIngredient("Farinha", "g")
        assertEquals(ingredient.id, repo.addIngredient(" farinha ", "kg").id)
        repo.addPriceObservation(ingredient.id, "OCR", "1000", "g", 800, 800, "OCR")
        assertEquals(1, repo.ingredients.value.size); assertEquals(800L, repo.priceObservations.value.single().packagePriceCents)
    }
    @Test fun `recipe revisions preserve older snapshots`() {
        val repo = BruleeRepository()
        val recipe = SavedRecipe("r1", "", 0, "Bolo", 8, listOf(RecipeIngredientItem("i1", "Farinha", Quantity.grams(1000), Money(500), Quantity.grams(200))), OperationalTimes())
        repo.saveRecipe(recipe); val first = repo.recipes.value.single()
        repo.saveRecipe(first.copy(name = "Bolo revisado"))
        assertEquals(2, repo.recipes.value.single().version); assertEquals(1, first.version)
        assertEquals("Bolo", first.name); assertNotEquals(first.versionId, repo.recipes.value.single().versionId)
    }
    @Test fun `settings are applied`() {
        val repo = BruleeRepository(); val settings = OperatingSettings(hourlyLaborRate = Money(3000))
        repo.updateSettings(settings); assertEquals(settings, repo.settings.value)
    }
    @Test fun `shopping list supports add check and delete`() {
        val repo = BruleeRepository(); val item = repo.addShoppingItem("Chocolate", "2 kg")!!
        repo.setShoppingItemPurchased(item.id, true); assertTrue(repo.shoppingItems.value.single().isPurchased)
        repo.deleteShoppingItem(item.id); assertTrue(repo.shoppingItems.value.isEmpty())
    }
    @Test fun `shopping list rejects incomplete items`() {
        val repo = BruleeRepository(); assertNull(repo.addShoppingItem("", "2 kg")); assertNull(repo.addShoppingItem("Chocolate", " "))
    }
}
