package io.nicronomicon.mtg

import io.nicronomicon.ecs.ID
import io.nicronomicon.ecs.storage.Storage
import io.nicronomicon.mtg.model.CardContainer
import io.nicronomicon.mtg.model.CardQuantity
import io.nicronomicon.mtg.model.Deck

class DeckService(
    private val cardService: CardService,
    private val storage: Storage<Deck>
) {

    suspend fun createDeck(deck: Deck): Deck {
        val validated = if (deck.id == ID.TEMP) {
            deck.copy(id = ID.makeRandomID())
        } else deck
        storage.save(validated)
        return validated
    }

    suspend fun find(id: ID) = storage.findById(id)
    suspend fun all() = storage.getAll()

    suspend fun createDeckFromText(deckText: String): Deck {
        val deck = parseDeckFromText(deckText)
        createDeck(deck)
        return deck
    }

    suspend fun parseDeckFromText(deckText: String): Deck {
        val lines = deckText.lines()
        var isMainBoard = true
        val mainBoard = mutableListOf<CardQuantity>()
        val sideBoard = mutableListOf<CardQuantity>()
        lines.forEach {
            if (it.isEmpty() || it == "Sideboard") {
                isMainBoard = false
            } else {
                val match = Regex("(\\d*) (.*)").find(it) ?: throw Exception()
                val (quantity, name) = match.destructured
                val card = cardService.findCardByName(name)
                    ?: throw Exception("could not find a card with name '${name}'")

                val container = if (isMainBoard) mainBoard else sideBoard
                container.add(CardQuantity(quantity.toInt(), card))
            }
        }
        return Deck(
            main = CardContainer(name = "main", cards = mainBoard),
            sideboard = CardContainer(name = "side", cards = sideBoard)
        )
    }
}
