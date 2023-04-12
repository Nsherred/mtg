package io.nicronomicon.ecs

import io.nicronomicon.ecs.storage.InMemoryStorage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

data class ExampleComponent(val name: String, override val id: ID = ID.makeRandomID()): WithID
data class OtherComponent(val foo: String, override val id: ID = ID.makeRandomID()): WithID

class ECSTest {
    private val entity1 = Entity(ID("1"))
    private val entity2 = Entity(ID("2"))
    private val entity3 = Entity(ID("3"))
    private val entity4 = Entity(ID("4"))

    @Test
    fun `find entities`() {
        val uut = ECS()
            .register<ExampleComponent>(InMemoryStorage())
            .register<OtherComponent>(InMemoryStorage())
            .register(entity1) {
                with(ExampleComponent("nick"))
                with(OtherComponent("foo"))
            }
            .register(entity2) {
                with(ExampleComponent("tom"))
                with(OtherComponent("foo"))
            }
            .register(entity3) {
                with(ExampleComponent("tom"))
                with(OtherComponent("bar"))
            }.register(entity4) {
                with(OtherComponent("foo"))
            }

        with(uut.find {
            has<ExampleComponent> {
                it.name == "nick"
            }
        }) {
            val entity = firstOrNull()
            assertEquals(entity1.id, entity?.id)

            val example = entity!![ExampleComponent::class]
            assertEquals("nick", example.first().name)
        }


        with(uut.find {
            has<ExampleComponent>()
        }) {
            assertEquals(3, toList().size)
        }

        with(uut.find {
            has<OtherComponent>()
        }) {
            assertEquals(4, toList().size)
        }
    }

    @Test
    fun `find entity with multiple of the same components`() {
        val uut = ECS()
            .register<ExampleComponent>(InMemoryStorage())
            .register<OtherComponent>(InMemoryStorage())
            .register(entity1) {
                with(ExampleComponent("cat"))
                with(ExampleComponent("dog"))
                with(ExampleComponent("bat"))
            }

        with(uut.find {
            has<ExampleComponent>()
        }) {

        }
    }
}
