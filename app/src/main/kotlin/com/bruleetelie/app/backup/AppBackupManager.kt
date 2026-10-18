package com.bruleetelie.app.backup

import android.content.Context
import android.net.Uri
import com.bruleetelie.data.backup.InvalidBackupException
import com.bruleetelie.data.backup.PortableBackupArchive
import com.bruleetelie.data.db.BruleeDatabase
import java.io.File
import java.io.OutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppBackupManager(private val context: Context) {
    suspend fun export(uri: Uri) = withContext(Dispatchers.IO) {
        context.contentResolver.openOutputStream(uri, "wt")?.use { exportTo(it) }
            ?: error("Não foi possível abrir o destino do backup")
    }

    suspend fun exportTo(file: File) = withContext(Dispatchers.IO) {
        file.parentFile?.mkdirs()
        file.outputStream().use { exportTo(it) }
    }

    private fun exportTo(output: OutputStream) {
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
        val payload = context.contentResolver.openInputStream(uri)?.use(PortableBackupArchive::read)
            ?: error("Não foi possível abrir o backup")
        val databaseBytes = payload.files[DATABASE_ENTRY]
            ?: throw InvalidBackupException("Banco de dados ausente no backup")

        BruleeDatabase.closeInstance()
        val databaseFile = context.getDatabasePath(DATABASE_NAME)
        databaseFile.parentFile?.mkdirs()
        replaceAtomically(databaseFile, databaseBytes)
        File("${databaseFile.path}-wal").delete()
        File("${databaseFile.path}-shm").delete()

        payload.files[SETTINGS_ENTRY]?.let { bytes ->
            replaceAtomically(File(context.filesDir, SETTINGS_RELATIVE_PATH), bytes)
        }
    }

    private fun replaceAtomically(destination: File, bytes: ByteArray) {
        destination.parentFile?.mkdirs()
        val temporary = File(destination.parentFile, "${destination.name}.restore")
        temporary.writeBytes(bytes)
        if (!temporary.renameTo(destination)) {
            temporary.copyTo(destination, overwrite = true)
            temporary.delete()
        }
    }

    companion object {
        private const val DATABASE_NAME = "brulee_telie.db"
        private const val DATABASE_ENTRY = "database.sqlite"
        private const val SETTINGS_ENTRY = "settings.preferences_pb"
        private const val SETTINGS_RELATIVE_PATH = "datastore/operating_settings.preferences_pb"
    }
}
