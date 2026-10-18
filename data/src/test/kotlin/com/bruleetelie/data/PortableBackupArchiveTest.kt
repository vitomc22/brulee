package com.bruleetelie.data

import com.bruleetelie.data.backup.InvalidBackupException
import com.bruleetelie.data.backup.PortableBackupArchive
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PortableBackupArchiveTest {
    @Test
    fun `round trip preserves every file`() {
        val source = mapOf(
            "database.sqlite" to byteArrayOf(1, 2, 3, 4),
            "settings.preferences_pb" to "settings".toByteArray()
        )
        val bytes = ByteArrayOutputStream().also {
            PortableBackupArchive.write(source, it, createdAtMillis = 1234L)
        }.toByteArray()

        val restored = PortableBackupArchive.read(ByteArrayInputStream(bytes))

        assertEquals(1234L, restored.createdAtMillis)
        assertEquals(source.keys, restored.files.keys)
        source.forEach { (name, contents) -> assertArrayEquals(contents, restored.files.getValue(name)) }
    }

    @Test
    fun `corrupted archive is rejected`() {
        assertThrows(InvalidBackupException::class.java) {
            PortableBackupArchive.read(ByteArrayInputStream("not a zip".toByteArray()))
        }
    }
}
