package com.bruleetelie.data

import com.bruleetelie.domain.*
import java.io.*
import java.util.Base64

/** Versioned quote input snapshot. Java-only encoding also works in JVM tests and backups. */
internal object QuoteDraftCodec {
    fun encode(draft: QuoteDraft): String = ByteArrayOutputStream().use { bytes ->
        DataOutputStream(bytes).use { out ->
            out.writeInt(1)
            with(draft.recipe) {
                out.writeUTF(id); out.writeUTF(versionId); out.writeInt(version)
                out.writeUTF(name); out.writeInt(yieldCount); out.writeInt(ingredients.size)
                ingredients.forEach {
                    out.writeUTF(it.id); out.writeUTF(it.name)
                    out.writeUTF(it.packageQuantity.value.toPlainString()); out.writeUTF(it.packageQuantity.unit.name)
                    out.writeLong(it.packagePrice.cents)
                    out.writeUTF(it.usedQuantity.value.toPlainString()); out.writeUTF(it.usedQuantity.unit.name)
                }
                out.writeUTF(times.prepTime.value.toPlainString()); out.writeUTF(times.cookTime.value.toPlainString())
                out.writeUTF(times.decorationTime.value.toPlainString()); out.writeUTF(times.energySource.name)
                out.writeUTF(times.complexity.name); out.writeLong(packaging.cents); out.writeLong(overhead.cents)
            }
            out.writeLong(draft.hourlyLaborRate.cents); out.writeLong(draft.gasCostPerHour.cents)
            out.writeLong(draft.electricityCostPerHour.cents); out.writeUTF(draft.targetMargin.toPlainString())
            out.writeBoolean(draft.customPrice != null); draft.customPrice?.let { out.writeLong(it.cents) }
            out.writeInt(draft.step)
        }
        Base64.getEncoder().encodeToString(bytes.toByteArray())
    }

    fun decode(encoded: String): QuoteDraft? = runCatching {
        DataInputStream(ByteArrayInputStream(Base64.getDecoder().decode(encoded))).use { input ->
            require(input.readInt() == 1)
            val id = input.readUTF(); val versionId = input.readUTF(); val version = input.readInt()
            val name = input.readUTF(); val yield = input.readInt()
            val count = input.readInt().also { require(it in 0..10000) }
            val ingredients = List(count) {
                RecipeIngredientItem(input.readUTF(), input.readUTF(),
                    Quantity(input.readUTF().toBigDecimal(), UnitOfMeasure.valueOf(input.readUTF())),
                    Money(input.readLong()), Quantity(input.readUTF().toBigDecimal(), UnitOfMeasure.valueOf(input.readUTF())))
            }
            val times = OperationalTimes(Quantity.minutes(input.readUTF().toBigDecimal()),
                Quantity.minutes(input.readUTF().toBigDecimal()), Quantity.minutes(input.readUTF().toBigDecimal()),
                EnergySource.valueOf(input.readUTF()), Complexity.valueOf(input.readUTF()))
            val recipe = SavedRecipe(id, versionId, version, name, yield, ingredients, times, Money(input.readLong()), Money(input.readLong()))
            QuoteDraft(recipe, Money(input.readLong()), Money(input.readLong()), Money(input.readLong()),
                input.readUTF().toBigDecimal(), if (input.readBoolean()) Money(input.readLong()) else null,
                input.readInt().coerceIn(0, 4))
        }
    }.getOrNull()
}
