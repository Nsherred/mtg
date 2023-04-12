package io.nicronomicon.mtg

import io.mockk.mockk
import io.nicronomicon.utils.defaultJson
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DeckServiceTest {
    @Test
    fun `parses main board from text`() {
        val deckText = """
            2 Archmage's Charm
            1 Brazen Borrower // Petty Theft
            4 Consider
            4 Counterspell
            3 Dragon's Rage Channeler
            4 Expressive Iteration
            1 Fiery Islet
            2 Flooded Strand
            3 Island
            2 Ledger Shredder
            3 Lightning Bolt
            4 Mishra's Bauble
            1 Misty Rainforest
            3 Murktide Regent
            1 Otawara, Soaring City
            2 Polluted Delta
            4 Ragavan, Nimble Pilferer
            2 Scalding Tarn
            2 Spell Pierce
            1 Spell Snare
            4 Spirebluff Canal
            3 Steam Vents
            4 Unholy Heat

            1 Blood Moon
            2 Dress Down
            3 Engineered Explosives
            2 Flusterstorm
            1 Jace, the Mind Sculptor
            2 Mystical Dispute
            2 Subtlety
            1 Tormod's Crypt
            1 Unlicensed Hearse
        """.trimIndent()
        val collectionProvider = TestCollectionProvider()
        val cardService = CardService(collectionProvider)
        val uut = DeckService(cardService, mockk(relaxed = true))
        val deck = runBlocking {
            uut.createDeckFromText(deckText)
        }
        Assertions.assertEquals(60, deck.main.size)
        Assertions.assertEquals(15, deck.sideboard.size)
        val json = defaultJson.encodeToString(deck)
        println(json)
    }
}