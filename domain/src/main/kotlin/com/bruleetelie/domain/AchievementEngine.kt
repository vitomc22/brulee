package com.bruleetelie.domain

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object AchievementEngine {
    fun salesStreak(
        sales: List<SaleSnapshot>,
        today: LocalDate = LocalDate.now(),
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Int {
        val saleDays = sales
            .map { Instant.ofEpochMilli(it.completedAtMillis).atZone(zoneId).toLocalDate() }
            .filter { !it.isAfter(today) }
            .toSet()
        if (saleDays.isEmpty()) return 0

        var cursor = if (today in saleDays) today else today.minusDays(1)
        var streak = 0
        while (cursor in saleDays) {
            streak++
            cursor = cursor.minusDays(1)
        }
        return streak
    }

    fun profitableSalesStreak(sales: List<SaleSnapshot>): Int = sales
        .sortedByDescending { it.completedAtMillis }
        .takeWhile { it.profit.cents >= 0L }
        .size
}
