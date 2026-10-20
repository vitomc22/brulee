package com.bruleetelie.data

import com.bruleetelie.domain.*
import org.junit.Assert.*
import org.junit.Test

class QuoteDraftCodecTest {
    @Test fun snapshotRoundTripRetainsEveryInputWithoutDependingOnSavedRecipeRevisions() {
        val recipe = SavedRecipe("r", "v", 3, "Bolo de café ☕", 12, listOf(
            RecipeIngredientItem("i", "Café; chocolate", Quantity.grams(1000), Money(2900), Quantity.grams(150))),
            OperationalTimes(Quantity.minutes(45), Quantity.minutes(30), Quantity.minutes(15), EnergySource.Both, Complexity.Complexa), Money(350), Money(250))
        val draft = QuoteDraft(recipe, Money(2500), Money(320), Money(250), "42.5".toBigDecimal(), Money(18000), 4)
        assertEquals(draft, QuoteDraftCodec.decode(QuoteDraftCodec.encode(draft)))
        assertEquals(draft.copy(customPrice = null, step = 1), QuoteDraftCodec.decode(QuoteDraftCodec.encode(draft.copy(customPrice = null, step = 1))))
    }
    @Test fun corruptOrUnknownSnapshotFallsBackToLegacyQuote() {
        assertNull(QuoteDraftCodec.decode("broken"))
        assertNull(QuoteDraftCodec.decode("AAAAAg=="))
    }
    @Test fun quoteMappingRetainsSnapshotAndAcceptsLegacyNull() {
        val costs = RecipeCosts(Money(1000), Money.Zero, Money.Zero, Money(100))
        val quote = Quote("q", "#ORC-Q", Customer("c", "Cliente", "11999999999"), "Receita", "", 1,
            PricingEngine.fromSalePrice(costs, Money(3000)), "20/10/2026")
        assertEquals(quote, quote.toEntity().toDomain())
        val draft = QuoteDraft(SavedRecipe("r", "", 0, "Receita", 1, emptyList(), OperationalTimes()), Money(2500), Money(320), Money(250), "35".toBigDecimal())
        assertEquals(quote.copy(draft = draft), quote.copy(draft = draft).toEntity().toDomain())
    }
}
