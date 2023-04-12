package io.nicronomicon.mtg.model

import io.nicronomicon.ecs.Entity
import io.nicronomicon.ecs.ID
import io.nicronomicon.ecs.ID.Companion.makeRandomID
import kotlinx.serialization.Serializable

@Serializable
data class Deck(
    override val id: ID = makeRandomID(),
    val name: String = "Temp",
    val version: DeckVersion = DeckVersion.default,
    val main: CardContainer,
    val sideboard: CardContainer
) : Entity, WithCards {
    override val cards = main.cards + sideboard.cards
    override fun countOf(card: Card) = main.countOf(card) + sideboard.countOf(card)
}

