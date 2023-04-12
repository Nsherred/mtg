package io.nicronomicon.ecs.storage

import io.nicronomicon.ecs.Entity
import io.nicronomicon.ecs.ID
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

data class TestComponent(
    val name: String,
    val isLockedOut: Boolean,
    override val id: ID = ID.makeRandomID()
) : Entity

internal class InMemoryStorageTest {

    @Test
    fun find() {
        val uut = InMemoryStorage<TestComponent>()
        runBlocking {
            uut.save(TestComponent("nick", false))
            uut.save(TestComponent("jeff", true))
            uut.save(TestComponent("bob", false))
        }
        // TODO: Fix unit tests to test new storage responsibilities
        // val result = uut.find(listOf({ !it.isLockedOut }, { it.name == "nick" }))
        // assertEquals(1, result.size)
        // assertEquals("nick", result[expected]?.first()?.name)
    }

    @Test
    fun `find with multiple`() {
        val uut = InMemoryStorage<TestComponent>()
        runBlocking {
            uut.save(TestComponent("nick", false))
            uut.save(TestComponent("jeff", true))
            uut.save(TestComponent("bob", false))
        }
        // TODO: Fix unit tests to test new storage responsibilities
        // val result = uut.find(listOf { !it.isLockedOut })
        // assertEquals(2, result[expected]?.size)
    }
}