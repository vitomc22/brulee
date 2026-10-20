package com.bruleetelie.app

import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.bruleetelie.app.backup.AppBackupManager
import com.bruleetelie.data.backup.PortableBackupArchive
import com.bruleetelie.data.db.BruleeDatabase
import com.bruleetelie.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.Test
import java.io.File

class BackupRestoreRegressionTest {
    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext
    private val repo get() = (context.applicationContext as BruleeApplication).repository
    private lateinit var directory: File
    @Before fun prepare() = runBlocking<Unit> {
        directory = File(context.cacheDir, "backup-test-${java.util.UUID.randomUUID()}").apply { mkdirs() }
        cleanData()
    }
    @After fun clean() = runBlocking<Unit> {
        try { cleanData() } finally { if (::directory.isInitialized) directory.deleteRecursively() }
    }
    private suspend fun cleanData() {
        withContext(Dispatchers.IO) { androidx.work.WorkManager.getInstance(context).cancelAllWork().result.get() }
        repo.awaitPendingWrites()
        withContext(Dispatchers.IO) {
            BruleeDatabase.getInstance(context).clearAllTables()
            File(context.filesDir, "automatic-backups").deleteRecursively()
        }
        repo.updateSettings(OperatingSettings())
        repo.awaitPendingWrites()
        withTimeout(10000) { repo.shoppingItems.first { it.isEmpty() } }
    }
    @Test fun restoredBackupRefreshesLiveDataAndAllowsMoreWrites() = runBlocking<Unit> {
        val original = repo.addShoppingItem("Farinha real", "2 kg")!!
        repo.updateSettings(OperatingSettings(hourlyLaborRate = Money(4200)))
        repo.awaitPendingWrites()
        val manager = AppBackupManager(context, repo)
        val archive = File(directory, "roundtrip.zip")
        manager.exportTo(archive)
        repo.deleteShoppingItem(original.id)
        repo.addShoppingItem("Depois do backup", "1 kg")
        repo.updateSettings(OperatingSettings())
        repo.awaitPendingWrites()
        manager.restore(Uri.fromFile(archive))
        val restoredRows = withContext(Dispatchers.IO) { BruleeDatabase.getInstance(context).shoppingItemDao().getAllItems().first() }
        assertEquals("SQLite must contain the backed-up purchase", listOf(original.id), restoredRows.map { it.id })
        val liveSettings = com.bruleetelie.data.datastore.OperatingSettingsDataStore(context).settingsFlow.first()
        assertEquals("Live DataStore must contain backed-up rate", Money(4200), liveSettings.hourlyLaborRate)
        assertNotNull("Repository must refresh restored rows; current=${repo.shoppingItems.value.map { it.name }}", withTimeoutOrNull(10000) { repo.shoppingItems.first { it.size == 1 && it.single().id == original.id } })
        assertNotNull("Repository must refresh restored settings", withTimeoutOrNull(10000) { repo.settings.first { it.hourlyLaborRate == Money(4200) } })
        repo.addShoppingItem("Compra após restaurar", "3 kg")
        repo.awaitPendingWrites()
        withTimeout(10000) { repo.shoppingItems.first { it.size == 2 } }
        assertEquals(2, withContext(Dispatchers.IO) { BruleeDatabase.getInstance(context).shoppingItemDao().getAllItems().first().size })
    }
    @Test fun invalidDatabaseWithValidArchiveChecksumDoesNotEraseCurrentData() = runBlocking<Unit> {
        val original = repo.addShoppingItem("Preservar esta compra", "1 kg")!!
        repo.awaitPendingWrites()
        val archive = File(directory, "invalid.zip")
        archive.outputStream().use { PortableBackupArchive.write(mapOf("database.sqlite" to "not a SQLite database".toByteArray()), it) }
        val result = runCatching { AppBackupManager(context, repo).restore(Uri.fromFile(archive)) }
        assertTrue(result.isFailure)
        assertEquals(original.id, repo.shoppingItems.value.single().id)
        repo.addShoppingItem("Ainda consigo salvar", "1 kg")
        repo.awaitPendingWrites()
        withTimeout(10000) { repo.shoppingItems.first { it.size == 2 } }
    }
    @Test fun invalidSettingsAreValidatedBeforeAnyDatabaseReplacement() = runBlocking<Unit> {
        val original = repo.addShoppingItem("Preservar dados e preferências", "1 kg")!!
        repo.awaitPendingWrites()
        val manager = AppBackupManager(context, repo)
        val valid = File(directory, "valid.zip")
        manager.exportTo(valid)
        val payload = valid.inputStream().use(PortableBackupArchive::read)
        val invalid = File(directory, "invalid-settings.zip")
        invalid.outputStream().use { PortableBackupArchive.write(payload.files + ("settings.preferences_pb" to byteArrayOf(127, 127, 127)), it) }
        val result = runCatching { manager.restore(Uri.fromFile(invalid)) }
        assertTrue(result.isFailure)
        assertEquals(original.id, repo.shoppingItems.value.single().id)
    }
}
