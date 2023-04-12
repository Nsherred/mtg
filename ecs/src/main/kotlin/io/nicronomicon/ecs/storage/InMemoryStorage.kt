package io.nicronomicon.ecs.storage

import io.nicronomicon.ecs.Entity
import io.nicronomicon.ecs.ID

class InMemoryStorage<T: Entity>: Storage<T> {
    private val cache = mutableMapOf<ID, T>()

    suspend fun loadCache(items: List<T>) {
        items.forEach {
            save(it)
        }
    }
    override suspend fun save(entity: T): T {
        cache[entity.id] = entity
        return entity
    }

    override suspend fun getByID(id: ID): T {
        val entity = findById(id)
        if(entity != null) return entity
        throw Exception("could not find entity with id: '${id}'")
    }

    override suspend fun findById(id: ID) = cache[id]
    override suspend fun getAll(): Sequence<T> {
       return cache.values.asSequence()
    }
}