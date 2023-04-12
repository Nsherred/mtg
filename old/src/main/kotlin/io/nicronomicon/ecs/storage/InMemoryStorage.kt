package io.nicronomicon.ecs.storage

import io.nicronomicon.ecs.ID
import io.nicronomicon.ecs.WithID

class InMemoryStorage<T : WithID> : ComponentStorage<T> {
    private val entityToComponent = mutableMapOf<ID, MutableSet<ID>>()
    private val componentToEntity = mutableMapOf<ID, ID>()
    private val componentMap = mutableMapOf<ID, T>()

    override fun update(id: ID, component: T) {
        val components = entityToComponent.getOrPut(id) { mutableSetOf() }
        if (!components.contains(component.id)) {
            components.add(component.id)
        }
        componentMap[component.id] = component
        componentToEntity[component.id] = id
    }

    override fun find(
        predicates: List<(component: T) -> Boolean>
    ) = componentMap.filter { pair ->
            predicates.forEach {
                if (!it(pair.value)) return@filter false
            }
            true
        }.map {
            (componentToEntity[it.key] ?: throw Exception("what ${it.key}")) to it.value
        }.groupBy({ it.first }) { it.second }

}

