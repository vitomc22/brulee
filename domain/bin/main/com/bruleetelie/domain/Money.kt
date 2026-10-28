package com.bruleetelie.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

@JvmInline
value class Money(val cents: Long) : Comparable<Money> {
    operator fun plus(other: Money) = Money(Math.addExact(cents, other.cents))
    operator fun minus(other: Money) = Money(Math.subtractExact(cents, other.cents))
    operator fun times(factor: Long) = Money(Math.multiplyExact(cents, factor))

    fun multiply(factor: BigDecimal): Money = Money(
        BigDecimal(cents).multiply(factor).setScale(0, RoundingMode.HALF_EVEN).longValueExact()
    )

    fun divide(divisor: Long): Money {
        require(divisor != 0L) { "Divisão por zero" }
        return Money(cents / divisor)
    }

    fun toBigDecimal(): BigDecimal = BigDecimal(cents).movePointLeft(2)

    fun format(locale: Locale = Locale.forLanguageTag("pt-BR")): String =
        NumberFormat.getCurrencyInstance(locale).format(toBigDecimal())

    fun abs(): Money = Money(kotlin.math.abs(cents))

    override fun compareTo(other: Money): Int = cents.compareTo(other.cents)

    companion object {
        val Zero = Money(0)

        fun fromReais(reais: BigDecimal): Money =
            Money(reais.movePointRight(2).setScale(0, RoundingMode.HALF_EVEN).longValueExact())

        fun fromReais(reaisString: String): Money {
            var s = reaisString.trim()
                .replace("R$", "")
                .replace("\u00A0", "")
                .replace(" ", "")
            if (s.contains('.') && s.contains(',')) {
                if (s.indexOf('.') < s.indexOf(',')) {
                    s = s.replace(".", "").replace(',', '.')
                } else {
                    s = s.replace(",", "")
                }
            } else if (s.contains(',')) {
                s = s.replace(',', '.')
            }
            return fromReais(BigDecimal(s))
        }

        fun fromCents(cents: Long): Money = Money(cents)
    }
}
