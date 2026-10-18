package com.bruleetelie.data.backup

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.MessageDigest
import java.util.Properties
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

data class BackupPayload(
    val schemaVersion: Int,
    val createdAtMillis: Long,
    val files: Map<String, ByteArray>
)

class InvalidBackupException(message: String) : IllegalArgumentException(message)

/**
 * Versioned, checksum-protected archive used by SAF exports and automatic backups.
 * The archive contains no device-specific absolute paths and can be restored on a
 * different Android installation.
 */
object PortableBackupArchive {
    const val CURRENT_SCHEMA_VERSION = 1
    private const val MANIFEST = "manifest.properties"

    fun write(
        files: Map<String, ByteArray>,
        output: OutputStream,
        schemaVersion: Int = CURRENT_SCHEMA_VERSION,
        createdAtMillis: Long = System.currentTimeMillis()
    ) {
        require(files.isNotEmpty()) { "O backup não pode estar vazio" }
        files.keys.forEach(::validateEntryName)

        val properties = Properties().apply {
            setProperty("format", "brulee-telie-backup")
            setProperty("schemaVersion", schemaVersion.toString())
            setProperty("createdAtMillis", createdAtMillis.toString())
            setProperty("fileCount", files.size.toString())
            files.toSortedMap().forEach { (name, bytes) ->
                setProperty("sha256.$name", sha256(bytes))
            }
        }
        val manifestBytes = ByteArrayOutputStream().use { buffer ->
            properties.store(buffer, "Brulee Telie portable backup")
            buffer.toByteArray()
        }

        ZipOutputStream(output.buffered()).use { zip ->
            zip.putNextEntry(ZipEntry(MANIFEST))
            zip.write(manifestBytes)
            zip.closeEntry()
            files.toSortedMap().forEach { (name, bytes) ->
                zip.putNextEntry(ZipEntry("data/$name"))
                zip.write(bytes)
                zip.closeEntry()
            }
        }
    }

    fun read(input: InputStream): BackupPayload {
        val entries = linkedMapOf<String, ByteArray>()
        ZipInputStream(input.buffered()).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                if (!entry.isDirectory) {
                    validateZipPath(entry.name)
                    entries[entry.name] = zip.readBytes()
                }
                zip.closeEntry()
                entry = zip.nextEntry
            }
        }

        val manifest = entries.remove(MANIFEST)
            ?: throw InvalidBackupException("Manifesto ausente")
        val properties = Properties().apply { load(ByteArrayInputStream(manifest)) }
        if (properties.getProperty("format") != "brulee-telie-backup") {
            throw InvalidBackupException("Formato de backup desconhecido")
        }
        val schemaVersion = properties.getProperty("schemaVersion")?.toIntOrNull()
            ?: throw InvalidBackupException("Versão de schema inválida")
        if (schemaVersion > CURRENT_SCHEMA_VERSION) {
            throw InvalidBackupException("Backup criado por uma versão mais nova do aplicativo")
        }
        val createdAtMillis = properties.getProperty("createdAtMillis")?.toLongOrNull()
            ?: throw InvalidBackupException("Data do backup inválida")
        val expectedCount = properties.getProperty("fileCount")?.toIntOrNull()
            ?: throw InvalidBackupException("Contagem de arquivos inválida")

        val files = entries.mapKeys { (path, _) ->
            if (!path.startsWith("data/")) throw InvalidBackupException("Entrada inesperada: $path")
            path.removePrefix("data/")
        }
        if (files.size != expectedCount) throw InvalidBackupException("Backup incompleto")
        files.forEach { (name, bytes) ->
            val expected = properties.getProperty("sha256.$name")
                ?: throw InvalidBackupException("Checksum ausente para $name")
            if (sha256(bytes) != expected) throw InvalidBackupException("Checksum inválido para $name")
        }
        return BackupPayload(schemaVersion, createdAtMillis, files)
    }

    private fun validateEntryName(name: String) {
        require(name.isNotBlank() && !name.startsWith('/') && ".." !in name.split('/')) {
            "Nome de arquivo inválido no backup: $name"
        }
    }

    private fun validateZipPath(path: String) {
        if (path.startsWith('/') || ".." in path.split('/')) {
            throw InvalidBackupException("Caminho inseguro no backup")
        }
    }

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { "%02x".format(it) }
}
