package io.nicronomicon.mtg

import io.nicronomicon.utils.defaultClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.random.Random
import kotlin.test.Ignore

@Ignore
internal class DeckDownloaderTest {
    @Test
    fun `download deck form mtgtop8`() {
        val id = 482305
        val uut = DeckDownloader(defaultClient)
        val deck = runBlocking {
            uut.downloadDeck(482305)
        }
        assertEquals(id.toString(), deck.name)
    }

    @Test
    fun `download decks in id range`() {
        val uut = DeckDownloader(defaultClient)

        val decks = runBlocking {
            uut.downloadDecks(482305, 482308)
        }
        assertEquals(4, decks.size)
    }

    @Test
    fun `downloads stuff`() {
        val uut = DeckDownloader(defaultClient)
        val ids = DeckIds.testIds
        val decks = runBlocking {
            for(id in ids) {
                val deck = uut.downloadDeck(id)
                val file = File("/Users/nsherred/projects/mtg-core/decks/$id.txt")
                file.bufferedWriter().use {
                    it.write(deck.data)
                }
                val delayDuration = Random.nextLong(5, 8) * 1000
                delay(delayDuration)
            }
        }

    }

}