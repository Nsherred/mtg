package io.nicronomicon.ecs.storage

import io.nicronomicon.ecs.ID

interface Storage<T> {
    suspend fun save(entity: T): T
    suspend fun getByID(id: ID): T
    suspend fun findById(id: ID): T?
    suspend fun getAll(): Sequence<T>
}