package io.nicronomicon.ecs

import kotlin.reflect.KClass

class Entity(override val id: ID) : WithID {
    private val components = mutableMapOf<KClass<*>, MutableList<*>>()

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(componentClass: KClass<T>): List<T> {
        return (components[componentClass] ?: emptyList<T>()) as List<T>
    }


    class Builder {
        val components = mutableListOf<WithID>()

        context(Builder)
        fun with(component: WithID): Builder {
            components.add(component)
            return this
        }
    }

    companion object {
        fun builder(build: Builder.() -> Unit): List<Any> {
            val target = Builder()
            target.build()
            return target.components
        }
    }
}