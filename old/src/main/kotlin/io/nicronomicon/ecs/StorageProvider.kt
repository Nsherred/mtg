package io.nicronomicon.ecs

import io.nicronomicon.ecs.storage.ComponentStorage
import kotlin.reflect.KClass

class StorageProvider {
    private val componentStorage = mutableMapOf<KClass<*>, ComponentStorage<*>>()
    private val componentClasses = mutableSetOf<KClass<*>>()

    fun <T : WithID> register(
        target: KClass<T>, storage: ComponentStorage<T>
    ) {
        componentClasses.add(target)
        componentStorage[target] = storage
    }

    operator fun <T : WithID> get(
        componentClass: KClass<out T>
    ): ComponentStorage<T> = find(componentClass) ?: throw IllegalStateException(
        "could not find storage for component type: ${componentClass.simpleName}"
    )

    @Suppress("UNCHECKED_CAST")
    fun <T : WithID> find(componentClass: KClass<out T>): ComponentStorage<T>? =
        componentStorage[componentClass] as? ComponentStorage<T>
}
