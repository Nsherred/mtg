package io.nicronomicon.mtg.result

import io.ktor.serialization.kotlinx.json.*
import io.nicronomicon.ecs.ID
import io.nicronomicon.ecs.storage.InMemoryStorage
import io.nicronomicon.ecs.storage.Storage
import io.nicronomicon.mtg.model.MTGResult
import io.nicronomicon.utils.createLogger
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class MTGResultsFileSystemStorage(
    private val json: Json = DefaultJson,
    filePath: String = "./decks/merged.json"
) : Storage<MTGResult> {
    private val storage = InMemoryStorage<MTGResult>()
    private val file = File(filePath)

    init {
        runBlocking {
            try {
                val fileContent = file.readText()
                val data = json.decodeFromString<List<MTGResult>>(fileContent)
                storage.loadCache(data)
            } catch (exception: FileNotFoundException) {
                log.warn(exception.message)
            }
        }
    }

    override suspend fun save(entity: MTGResult): MTGResult {
        return storage.save(entity)
    }

    override suspend fun getByID(id: ID): MTGResult {
        return storage.getByID(id)
    }

    override suspend fun findById(id: ID): MTGResult? {
        return storage.findById(id)
    }

    override suspend fun getAll(): Sequence<MTGResult> {
        return storage.getAll()
    }

    private companion object {
        val log = createLogger<MTGResultsFileSystemStorage>()
    }
}