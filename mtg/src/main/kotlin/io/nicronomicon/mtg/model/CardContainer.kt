package io.nicronomicon.mtg.model

import io.nicronomicon.ecs.Entity
import io.nicronomicon.ecs.ID
import kotlinx.serialization.Serializable

@Serializable
class CardContainer(
    override val id: ID = ID.makeRandomID(),
    val name: String,
    override val cards: List<CardQuantity> = emptyList()
): Entity, WithCards {
    private val lookUp = cards.associate { it.card to it.quantity }
    override fun countOf(card: Card): Double {
        return lookUp[card]?.toDouble() ?: 0.0
    }
    val size: Int
        get() = cards.sumOf { it.quantity }
}