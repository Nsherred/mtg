package io.nicronomicon.ecs.storage

import io.nicronomicon.ecs.ID
import io.nicronomicon.ecs.Predicate
import io.nicronomicon.ecs.WithID


interface ComponentStorage<T : WithID> {
    fun find(predicates: List<Predicate<T>>): Map<ID, List<T>>
    fun update(id: ID, component: T)
}

