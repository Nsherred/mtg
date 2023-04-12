package io.nicronomicon.ecs

import io.nicronomicon.ecs.storage.ComponentStorage
import io.nicronomicon.ecs.storage.StorageResult
import kotlin.reflect.KClass

class ECS {
    private val entities = mutableListOf<Entity>()
    private val storageProvider = StorageProvider()

    fun register(entity: Entity, build: Entity.Builder.() -> Unit): ECS {
        val builder = Entity.Builder()
        builder.build()
        builder.components.forEach {
            update(entity.id, it)
        }
        entities.add(entity)
        return this
    }

    inline fun <reified T : WithID> register(storage: ComponentStorage<T>): ECS {
        register(T::class, storage)
        return this
    }

    fun <T : WithID> register(target: KClass<T>, storage: ComponentStorage<T>) {
        storageProvider.register(target, storage)
    }

    private fun <T : WithID> update(id: ID, component: T) {
        val componentClass =  component::class

        val storage = storageProvider[componentClass]
        storage.update(id, component)
    }

    @Suppress("UNCHECKED_CAST")
    fun find(
        configure: QueryScope.Builder.() -> Unit
    ): Sequence<StorageResult> {
        val query = QueryScope.Builder().apply(configure).build()
        val entityToComponents = mutableMapOf<ID, MutableList<Any>>()

        for ((componentClass, predicates) in query.componentToPredicate) {
            val storage = storageProvider[componentClass as KClass<out WithID>]
            val results = storage.find(predicates as List<Predicate<Any>>)
            for (result in results) {
                val entity = entityToComponents.getOrPut(result.key) { mutableListOf() }
                result.value.forEach { entity.add(it) }
            }
        }
        return entityToComponents.map { StorageResult(it.key, it.value) }.asSequence()
    }

}

