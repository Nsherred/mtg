package io.nicronomicon.mtg.recommendation

import io.nicronomicon.ecs.ID
import io.nicronomicon.ecs.storage.fileSystemStorage
import io.nicronomicon.mtg.CardService
import io.nicronomicon.mtg.DeckService
import io.nicronomicon.mtg.TestCollectionProvider
import io.nicronomicon.mtg.model.Deck
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.test.Ignore

@Ignore
internal class DeckClustersTest {

    @Test
    fun getRecommendations() {
        val deckId = ID("0f750987-9684-4938-b9b1-3dfa1ef5ac6f")
        val collectionProvider = TestCollectionProvider()
        val cardService = CardService(collectionProvider)
        val inputPath = Path.of("/Users/nsherred/projects/mtg-core/decks/parsed")
        val deckStorage = Deck::class.fileSystemStorage(inputPath)
        val deckService = DeckService(cardService, deckStorage)
        val decks = runBlocking {
            deckService.all().toList()
        }
        val deck = runBlocking {
            deckService.find(deckId)
        } ?: throw Exception("could not find a deck with id '$deckId'")
        val uut = DeckRecommendationProvider(decks)
        val things = uut.getMainRecommendations(deck)
        println(things.joinToString("\n"))
    }

    @Test
    fun getSideRecommendations() {
        val deckId = ID("0f750987-9684-4938-b9b1-3dfa1ef5ac6f")
        val collectionProvider = TestCollectionProvider()
        val cardService = CardService(collectionProvider)
        val inputPath = Path.of("/Users/nsherred/projects/mtg-core/decks/parsed")
        val deckStorage = Deck::class.fileSystemStorage(inputPath)
        val deckService = DeckService(cardService, deckStorage)
        val decks = runBlocking {
            deckService.all().toList()
        }
        val island =  runBlocking { cardService.findCardByName("Island")!! }
        val decksWithIsland = decks.filter { it.sideboard.countOf(island) > 0 }

        val deck = runBlocking {
            deckService.find(deckId)
        } ?: throw Exception("could not find a deck with id '$deckId'")

        println(deck.sideboard.cards.joinToString("\n") { it.card.name })
        val uut = DeckRecommendationProvider(decks)
        val things = uut.getSideRecommendations(deck)
        println(things.joinToString("\n") {
            it.card.name
        })
    }
}