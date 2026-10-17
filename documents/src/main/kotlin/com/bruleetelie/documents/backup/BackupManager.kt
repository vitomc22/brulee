package com.bruleetelie.documents.backup

import java.io.File
import java.security.MessageDigest
import java.util.Base64
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

data class BackupDataPayload(
    val quotesJson: String,
    val ordersJson: String,
    val salesJson: String,
    val ingredientsJson: String,
    val settingsJson: String
)

class BackupManager {
    fun createBackupZip(payload: BackupDataPayload, output: File) {
        val data = encode(payload).toByteArray(Charsets.UTF_8)
        val manifest = "formatVersion=1\nchecksum=${sha256(data)}\n".toByteArray(Charsets.UTF_8)
        ZipOutputStream(output.outputStream().buffered()).use { zip ->
            zip.putNextEntry(ZipEntry("manifest.txt"))
            zip.write(manifest)
            zip.closeEntry()
            zip.putNextEntry(ZipEntry("data.txt"))
            zip.write(data)
            zip.closeEntry()
        }
    }

    fun verifyAndExtractBackup(input: File): BackupDataPayload = try {
        ZipFile(input).use { zip ->
            val manifestEntry = zip.getEntry("manifest.txt") ?: throw IllegalArgumentException("Manifest ausente")
            val dataEntry = zip.getEntry("data.txt") ?: throw IllegalArgumentException("Dados ausentes")
            val manifest = zip.getInputStream(manifestEntry).bufferedReader().readText()
            val expected = manifest.lineSequence().firstOrNull { it.startsWith("checksum=") }
                ?.substringAfter('=') ?: throw IllegalArgumentException("Checksum ausente")
            val data = zip.getInputStream(dataEntry).readBytes()
            require(sha256(data) == expected) { "Checksum inválido" }
            decode(data.toString(Charsets.UTF_8))
        }
    } catch (error: IllegalArgumentException) {
        throw error
    } catch (error: Exception) {
        throw IllegalArgumentException("Backup inválido ou corrompido", error)
    }

    private fun encode(payload: BackupDataPayload): String = listOf(
        payload.quotesJson, payload.ordersJson, payload.salesJson,
        payload.ingredientsJson, payload.settingsJson
    ).joinToString("\n") { Base64.getEncoder().encodeToString(it.toByteArray(Charsets.UTF_8)) }

    private fun decode(value: String): BackupDataPayload {
        val fields = value.lineSequence().filter { it.isNotEmpty() }.map {
            Base64.getDecoder().decode(it).toString(Charsets.UTF_8)
        }.toList()
        require(fields.size == 5) { "Formato de dados inválido" }
        return BackupDataPayload(fields[0], fields[1], fields[2], fields[3], fields[4])
    }

    private fun sha256(bytes: ByteArray): String =
        MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
}
