package io.nicronomicon.ecs.storage

import io.nicronomicon.ecs.Entity
import io.nicronomicon.ecs.ID
import io.nicronomicon.utils.defaultJson
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.io.path.createTempDirectory

internal class FileSystemStorageTest {

    @Serializable
    data class TestData(val name: String, override val id: ID) : Entity

    @Test
    fun `saves file to disk and can read it back`() {
        val dir = createTempDirectory()
        val uut = FileSystemStorage(defaultJson, dir, TestData::class)
        val entity = TestData("name", ID.makeRandomID())
        runBlocking {
            uut.save(entity)
            val saved = uut.getByID(entity.id)

            assertEquals(entity, saved)
        }
        val savedText = dir.resolve("${entity.id.value}.json").toFile().readText()
        val fromText = defaultJson.decodeFromString<TestData>(savedText)
        assertEquals(entity, fromText)
    }

    @Test
    fun `load data from disk when instantiated`() {
        val dir = createTempDirectory()
        val entity = TestData("name", ID.makeRandomID())
        val file = dir.resolve("${entity.id.value}.json").toFile()
        file.writer().use {
            val fromText = defaultJson.encodeToString(entity)
            it.write(fromText)
        }

        val uut = FileSystemStorage(defaultJson, dir, TestData::class)
        runBlocking {
            val retrieved = uut.getByID(entity.id)
            assertEquals(entity, retrieved)
        }

    }
}