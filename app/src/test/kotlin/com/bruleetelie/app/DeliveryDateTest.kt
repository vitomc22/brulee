package com.bruleetelie.app

import com.bruleetelie.app.ui.screens.isValidDeliveryDate
import com.bruleetelie.app.ui.screens.parseDeliveryDate
import java.time.LocalDate
import org.junit.Assert.*
import org.junit.Test

class DeliveryDateTest {
    @Test fun onlyTodayAndFutureRealBrazilianDatesAreAccepted() {
        val today = LocalDate.of(2026, 10, 20)
        assertTrue(isValidDeliveryDate("20/10/2026", today))
        assertTrue(isValidDeliveryDate("21/10/2026 16:00", today))
        listOf("19/10/2026", "31/02/2027", "2026-10-21", "21/10/2026 99:99", "", "21/10/2026junk").forEach {
            assertFalse(it, isValidDeliveryDate(it, today))
        }
        assertEquals(today, parseDeliveryDate("20/10/2026"))
    }
}
