package io.nicronomicon.ecs

import kotlin.reflect.KClass

typealias Predicate<T> = (components: T) -> Boolean

class QueryScope(
    val entityIds: Set<ID>,
    val componentToPredicate: Map<KClass<*>, List<Predicate<*>>>,
) {

    class Builder {
        //    private val components = mutableSetOf<KClass<*>>()
        private val entityIds = mutableSetOf<ID>()
        private val componentToPredicate = mutableMapOf<KClass<*>, MutableList<Predicate<*>>>()

        inline fun <reified T : WithID> has(noinline filter: Predicate<T>) {
            has(T::class, filter)
        }

        inline fun <reified T : WithID> has() {
            has(T::class) { true }
        }

        fun <T : WithID> has(componentClass: KClass<T>, filter: Predicate<T>) {
            componentToPredicate.getOrPut(componentClass) { mutableListOf() }
                .add(filter)
        }

        fun has(ids: Collection<ID>) {
            entityIds.addAll(ids)
        }

        fun build() = QueryScope(entityIds, componentToPredicate)
    }
}