package com.bruleetelie.data

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.bruleetelie.data.db.BruleeDatabase
import com.bruleetelie.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Test

class RepositoryPersistenceRegressionTest {
    private fun recipe() = SavedRecipe("recipe", "", 0, "Bolo real", 12,
        listOf(RecipeIngredientItem("i1", "Farinha", Quantity.grams(1000), Money(800), Quantity.grams(300))), OperationalTimes(), Money(250), Money(100))
    private fun quote() = Quote("quote", "#ORC-REAL", Customer("customer", "Cliente real", "11999998888"), "Bolo", "", 12,
        PricingEngine.fromSalePrice(RecipeCosts(Money(2000), Money(1000), Money(200), Money(300)), Money(6000)), "21/10/2026 16:00")
    private suspend fun await(label: String, condition: suspend () -> Boolean) {
        assertNotNull(label, withTimeoutOrNull(10000) { while (!condition()) delay(20); true })
    }
    @Test fun recipeRevisionsQuoteChecklistAndSaleSurviveReopeningRepository() = runBlocking {
        repeat(20) {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.inMemoryDatabaseBuilder(context, BruleeDatabase::class.java).build()
        val firstScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val secondScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        try {
            val repo = BruleeRepository(db, externalScope = firstScope)
            repo.saveRecipe(recipe())
            await("first recipe revision must persist") { db.recipeDao().latestVersion("recipe") != null }
            withTimeout(10000) { repo.recipes.first { it.singleOrNull()?.version == 1 } }
            repo.saveRecipe(repo.recipes.value.single().copy(name = "Bolo revisado"))
            await("second recipe revision must persist") { db.recipeDao().latestVersion("recipe")?.versionNumber == 2 }
            repo.addQuote(quote()); repo.addQuote(quote().copy(status = QuoteStatus.Sent))
            val order = repo.approveQuote("quote")!!
            repo.approveQuote("quote")
            await("approved production order must persist") { db.productionOrderDao().getOrderById(order.id) != null }
            withTimeout(10000) { repo.productionOrders.first { list -> list.any { it.id == order.id } } }
            order.steps.forEach { repo.updateStepStatus(order.id, it.id, true) }
            await("all four step updates must persist") { db.productionOrderDao().getStepsForOrderSync(order.id).let { it.size == 4 && it.all { step -> step.isCompleted } } }
            // UI actions use the observed state, not a separate DAO read that can precede its emission.
            withTimeout(10000) { repo.productionOrders.first { it.singleOrNull()?.isAllStepsDone == true } }
            repo.completeOrder(order.id, RecipeCosts(Money(1500), Money(1000), Money(200), Money(300)))
            repo.completeOrder(order.id, null)
            await("sale must persist after checklist completion") { db.saleSnapshotDao().getAllSales().first().size == 1 }
            firstScope.cancel()
            val reopened = BruleeRepository(db, externalScope = secondScope)
            withTimeout(10000) { reopened.recipes.first { it.isNotEmpty() }; reopened.quotes.first { it.isNotEmpty() }; reopened.productionOrders.first { it.isNotEmpty() }; reopened.completedSales.first { it.isNotEmpty() } }
            assertEquals(2, reopened.recipes.value.single().version)
            assertEquals("Bolo revisado", reopened.recipes.value.single().name)
            assertEquals(300.toBigDecimal(), reopened.recipes.value.single().ingredients.single().usedQuantity.value)
            assertEquals(QuoteStatus.ConvertedToSale, reopened.quotes.value.single().status)
            assertTrue(reopened.productionOrders.value.single().isAllStepsDone)
            assertEquals(ProductionStatus.Completed, reopened.productionOrders.value.single().status)
            assertEquals(3000L, reopened.completedSales.value.single().profit.cents)
            val oldVersionCount = db.openHelper.readableDatabase.query("SELECT count(*) FROM recipe_versions").use { it.moveToFirst(); it.getInt(0) }
            assertEquals(2, oldVersionCount)
        } finally { firstScope.cancel(); secondScope.cancel(); db.close() }
        }
    }
}
