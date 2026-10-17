package com.bruleetelie.documents.backup

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

data class BackupManifest(
    val formatVersion: Int = 1,
    val appVersion: String = "0.1.0",
    val createdAtMillis: Long = System.currentTimeMillis(),
    val checksumSha256: String
)

data class BackupDataPayload(
    val quotesJson: String,
    val ordersJson: String,
    val salesJson: String,
    val ingredientsJson: String,
    val settingsJson: String
)

class BackupManager {

    fun createBackupZip(
        payload: BackupDataPayload,
        outputZipFile: File
    ): File {
        val dataCombined = "${payload.quotesJson}\n${payload.ordersJson}\n${payload.salesJson}\n${payload.ingredientsJson}\n${payload.settingsJson}"
        val checksum = calculateSha256(dataCombined)

        val manifestContent = """
            {
              "formatVersion": 1,
              "appVersion": "0.1.0",
              "createdAtMillis": ${System.currentTimeMillis()},
              "checksumSha256": "$checksum"
            }
        """.trimIndent()

        outputZipFile.parentFile?.mkdirs()

        ZipOutputStream(FileOutputStream(outputZipFile)).use { zos ->
            // Add manifest.json
            zos.putNextEntry(ZipEntry("manifest.json"))
            zos.write(manifestContent.toByteArray(Charsets.UTF_8))
            zos.closeEntry()

            // Add data.json
            zos.putNextEntry(ZipEntry("data.json"))
            zos.write(dataCombined.toByteArray(Charsets.UTF_8))
            zos.closeEntry()
        }

        return outputZipFile
    }

    fun verifyAndExtractBackup(zipFile: File): BackupDataPayload {
        require(zipFile.exists() && zipFile.length() > 0) { "Arquivo de backup não encontrado ou vazio" }

        var manifestJson = ""
        var dataCombined = ""

        ZipInputStream(zipFile.inputStream()).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val bytes = zis.readBytes()
                val content = String(bytes, Charsets.UTF_8)
                when (entry.name) {
                    "manifest.json" -> manifestJson = content
                    "data.json" -> dataCombined = content
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }

        require(manifestJson.isNotBlank()) { "Arquivo manifest.json inválido no backup" }
        require(dataCombined.isNotBlank()) { "Arquivo data.json inválido no backup" }

        // Extract checksum from manifest
        val expectedChecksumMatch = "\"checksumSha256\": \"([a-f0-9]+)\"".toRegex().find(manifestJson)
        requireNotNull(expectedChecksumMatch) { "Checksum ausente no manifest.json do backup" }

        val expectedChecksum = expectedChecksumMatch.groupValues[1]
        val actualChecksum = calculateSha256(dataCombined)

        require(expectedChecksum == actualChecksum) {
            "Falha de integridade no backup: checksum não corresponde (esperado: $expectedChecksum, real: $actualChecksum)"
        }

        val lines = dataCombined.lines()
        val quotes = lines.getOrElse(0) { "[]" }
        val orders = lines.getOrElse(1) { "[]" }
        val sales = lines.getOrElse(2) { "[]" }
        val ingredients = lines.getOrElse(3) { "[]" }
        val settings = lines.getOrElse(4) { "{}" }

        return BackupDataPayload(
            quotesJson = quotes,
            ordersJson = orders,
            salesJson = sales,
            ingredientsJson = ingredients,
            settingsJson = settings
        )
    }

    private fun calculateSha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }
}
