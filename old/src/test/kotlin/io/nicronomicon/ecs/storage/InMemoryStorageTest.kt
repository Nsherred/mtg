package io.nicronomicon.ecs.storage

import io.nicronomicon.ecs.ID
import io.nicronomicon.ecs.WithID
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

data class TestComponent(
    val name: String,
    val isLockedOut: Boolean,
    override val id: ID = ID.makeRandomID()
): WithID

internal class InMemoryStorageTest {

    @Test
    fun find() {
        val expected = ID.makeRandomID()
        val uut = InMemoryStorage<TestComponent>()
        uut.update(expected, TestComponent("nick", false))
        uut.update(ID.makeRandomID(), TestComponent("jeff", true))
        uut.update(ID.makeRandomID(), TestComponent("bob", false))
        val result = uut.find(listOf({ !it.isLockedOut }, { it.name == "nick" }))
        assertEquals(1, result.size)
        assertEquals("nick", result[expected]?.first()?.name)
    }

    @Test
    fun `find with multiple`() {
        val expected = ID.makeRandomID()
        val uut = InMemoryStorage<TestComponent>()
        uut.update(expected, TestComponent("nick", false))
        uut.update(expected, TestComponent("jeff", true))
        uut.update(expected, TestComponent("bob", false))
        val result = uut.find(listOf { !it.isLockedOut })
        assertEquals(2, result[expected]?.size)
    }
}