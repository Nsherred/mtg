package io.nicronomicon.ecs
import io.nicronomicon.ecs.storage.Storage
import kotlin.reflect.KClass

class ECSystem() {
    val storage = mutableMapOf<KClass<*>, Storage<*>>()

    inline operator fun <reified T> get(id: ID):T {
//        return this.storage[T::class]?.getByID(id) as T
        TODO()
    }
}