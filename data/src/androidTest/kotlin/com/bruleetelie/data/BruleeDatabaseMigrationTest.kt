package com.bruleetelie.data

import android.content.Context
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bruleetelie.data.db.BruleeDatabase
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BruleeDatabaseMigrationTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        BruleeDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @org.junit.Before fun cleanBefore() { context.deleteDatabase("migration-test") }
    @org.junit.After fun cleanAfter() { context.deleteDatabase("migration-test") }

    @Test
    fun migrate3To4_preservesSaleSnapshotAndCreatesFinancialTables() {
        helper.createDatabase("migration-test", 3).apply {
            execSQL("INSERT INTO sale_snapshots (id, orderId, quoteCode, customerName, productName, salePriceCents, estimatedCostCents, actualCostCents, profitCents, marginPercent, completedAtFormatted, paymentStatus, activeProductionMinutes, completedAtMillis) VALUES ('sale-1', 'order-1', '#ORC-1', 'Ana', 'Bolo', 14990, 9000, 9500, 5490, '36.63', '18/10/2026', 'Paid', 60, 1000)")
            close()
        }

        helper.runMigrationsAndValidate("migration-test", 4, true, *BruleeDatabase.ALL_MIGRATIONS)
            .use { database ->
                database.query("SELECT actualCostCents, profitCents FROM sale_snapshots WHERE id = 'sale-1'").use { cursor ->
                    assertEquals(true, cursor.moveToFirst())
                    assertEquals(9500L, cursor.getLong(0))
                    assertEquals(5490L, cursor.getLong(1))
                }
                database.query("SELECT name FROM sqlite_master WHERE type = 'table' AND name IN ('suppliers', 'payments', 'expenses', 'cost_resources') ORDER BY name").use { cursor ->
                    assertEquals(4, cursor.count)
                }
            }
    }

}
