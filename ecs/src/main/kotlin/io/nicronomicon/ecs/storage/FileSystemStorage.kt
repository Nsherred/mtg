package io.nicronomicon.ecs.storage

import io.nicronomicon.ecs.Entity
import io.nicronomicon.ecs.ID
import io.nicronomicon.utils.createLogger
import io.nicronomicon.utils.defaultJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.FileNotFoundException
import java.nio.file.Path
import kotlin.reflect.KClass


fun <T: Entity> KClass<T>.fileSystemStorage(path: Path): FileSystemStorage<T> {
    return FileSystemStorage(defaultJson, path , this)
}
@OptIn(InternalSerializationApi::class)
class FileSystemStorage<T : Entity>(
    private val json: Json,
    path: Path,
    kClass: KClass<T>
) : Storage<T> {
    private val log = createLogger("FileSystemStorage<${kClass.simpleName}>")
    private val storage = InMemoryStorage<T>()
    private val directory = path.toFile()
    private val serializer = kClass.serializer()

    init {
        runBlocking {
            try {
                directory.walkTopDown().forEach {
                    if (it.name.contains(".json")) {
                        val fileContent = it.readText()
                        val data = json.decodeFromString(serializer, fileContent)
                        storage.save(data)
                    }
                }
            } catch (exception: FileNotFoundException) {
                log.warn("could not load file")
            }
        }
    }

    override suspend fun save(entity: T): T {
        withContext(Dispatchers.IO){
            val contents = json.encodeToString(serializer, entity)
            val entityFile = directory.resolve("${entity.id.value}.json")
            entityFile.bufferedWriter().use {
                it.write(contents)
            }
        }
        return storage.save(entity)
    }

    override suspend fun getByID(id: ID): T {
        return storage.getByID(id)
    }

    override suspend fun findById(id: ID): T? {
        return storage.findById(id)
    }

    override suspend fun getAll(): Sequence<T> {
        return storage.getAll()
    }

    companion object {

    }
}