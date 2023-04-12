package io.nicronomicon.ecs.storage

import io.nicronomicon.ecs.ID
import kotlin.reflect.KClass

class StorageResult(val id: ID, components: List<Any>) {
    private val components = components.groupBy { it::class }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(componentClass: KClass<T>): List<T> {
        return (components[componentClass] ?: emptyList<T>()) as List<T>
    }

}