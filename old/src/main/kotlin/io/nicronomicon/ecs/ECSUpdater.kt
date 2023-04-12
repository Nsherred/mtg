package io.nicronomicon.ecs

interface ECSUpdater {
    fun <T : Any> update(id: String, component: T)
}
