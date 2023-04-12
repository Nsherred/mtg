package io.nicronomicon.ecs

import io.nicronomicon.ecs.storage.InMemoryStorage
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Thing(override val id: ID) : WithID

internal class StorageProviderTest {

    @Test
    fun `storage provider test`() {
        val uut = StorageProvider()
        uut.register(Thing::class, InMemoryStorage())
        val storage = uut[Thing::class]
        val thingId = ID.makeRandomID()
        storage.update(ID.TEMP, Thing(thingId))
        val thing = storage.find(listOf { true })
        assertEquals(thingId, thing[ID.TEMP]?.first()?.id)
    }
}