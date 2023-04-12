package io.nicronomicon.mtg

import io.ktor.resources.*
import io.nicronomicon.ecs.ID
import kotlinx.serialization.Serializable

@Serializable
@Resource("/deck")
class DeckResource {
    @Serializable
    @Resource("{id}")
    class Id(val parent: DeckResource = DeckResource(), val id: ID)
}