package com.bruleetelie.app

import androidx.compose.ui.graphics.Color
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.domain.Money
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class BruleeAppLogicTest {

    @Test
    fun `test stitch design tokens are defined correctly`() {
        assertNotNull(BruleePrimary)
        assertNotNull(BruleeSecondary)
        assertNotNull(BruleeSurface)
        assertNotNull(BruleeBackground)

        assertEquals(Color(0xFF3B523E), BruleePrimary)
        assertEquals(Color(0xFFB88C42), BruleeSecondary)
        assertEquals(Color(0xFFFDF9F1), BruleeSurface)
        assertEquals(Color(0xFFFAF9F6), BruleeBackground)
    }

    @Test
    fun `test money formatting for UI display`() {
        val money = Money.fromReais("149.90")
        val formatted = money.format()
        assertEquals("R$\u00A0149,90", formatted)
    }
}
