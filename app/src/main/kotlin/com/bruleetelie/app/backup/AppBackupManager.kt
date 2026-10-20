package com.bruleetelie.app.backup

import android.content.Context
import android.net.Uri
import com.bruleetelie.data.BruleeRepository
import com.bruleetelie.data.backup.InvalidBackupException
import com.bruleetelie.data.backup.PortableBackupArchive
import com.bruleetelie.data.datastore.OperatingSettingsDataStore
import com.bruleetelie.data.db.BruleeDatabase
import com.bruleetelie.domain.OperatingSettings
import java.io.File
import java.io.OutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppBackupManager(private val context: Context, private val repository: BruleeRepository? = null) {
    private suspend fun <T> exclusively(block: suspend () -> T): T =
        if (repository == null) block() else repository.withExclusiveWrites(block)

    suspend fun export(uri: Uri) = withContext(Dispatchers.IO) {
        context.contentResolver.openOutputStream(uri, "wt")?.use { exportTo(it) }
            ?: error("Não foi possível abrir o destino do backup")
    }

    suspend fun exportTo(file: File) = withContext(Dispatchers.IO) {
        file.parentFile?.mkdirs()
        file.outputStream().use { exportTo(it) }
    }

    private suspend fun exportTo(output: OutputStream) = exclusively {
        BruleeDatabase.checkpoint(context)
        val files = linkedMapOf<String, ByteArray>()
        val databaseFile = context.getDatabasePath(DATABASE_NAME)
        check(databaseFile.isFile) { "Banco de dados ainda não foi criado" }
        files[DATABASE_ENTRY] = databaseFile.readBytes()
        val settingsFile = File(context.filesDir, SETTINGS_RELATIVE_PATH)
        if (settingsFile.isFile) files[SETTINGS_ENTRY] = settingsFile.readBytes()
        PortableBackupArchive.write(files, output)
    }

    suspend fun restore(uri: Uri) = withContext(Dispatchers.IO) {
        exclusively {
            val payload = context.contentResolver.openInputStream(uri)?.use(PortableBackupArchive::read)
                ?: error("Não foi possível abrir o backup")
            val databaseBytes = payload.files[DATABASE_ENTRY]
                ?: throw InvalidBackupException("Banco de dados ausente no backup")
            val settingsStore = OperatingSettingsDataStore(context)
            val restoredSettings = payload.files[SETTINGS_ENTRY]?.let { settingsStore.readBackup(it) } ?: OperatingSettings()
            val snapshot = File.createTempFile("brulee-restore-", ".sqlite", context.cacheDir)
            try {
                snapshot.writeBytes(databaseBytes)
                BruleeDatabase.getInstance(context).restoreFromSnapshot(context, snapshot)
                settingsStore.saveSettings(restoredSettings)
            } finally {
                snapshot.delete()
                File("${snapshot.path}-wal").delete()
                File("${snapshot.path}-shm").delete()
            }
        }
    }

    companion object {
        private const val DATABASE_NAME = "brulee_telie.db"
        private const val DATABASE_ENTRY = "database.sqlite"
        private const val SETTINGS_ENTRY = "settings.preferences_pb"
        private const val SETTINGS_RELATIVE_PATH = "datastore/operating_settings.preferences_pb"
    }
}
