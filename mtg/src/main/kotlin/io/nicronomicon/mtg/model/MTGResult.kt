package io.nicronomicon.mtg.model

import io.nicronomicon.ecs.Entity
import io.nicronomicon.ecs.ID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
class MTGResult(
    val link: String,
    val name: String,
    val player: String,
    val format: String,
    val event: String,
    val placement: String,
    val date: Instant
) : Entity {
    override val id = ID.makeRandomID()
}