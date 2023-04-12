package io.nicronomicon.mtg.result

import io.mockk.coEvery
import io.mockk.mockk
import io.nicronomicon.ecs.storage.Storage
import io.nicronomicon.mtg.model.MTGResult
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MTGResultsRepositoryTest {

    @Test
    fun find() {
        val storage = mockk<Storage<MTGResult>>()
        coEvery { storage.getAll() } returns sequenceOf(
            MTGResult(
                "",
                "Best Deck Ever", "player 1", "Modern", "FFFF", "1", Clock.System.now()
            ),
            MTGResult(
                "",
                "Best Legacy Deck Ever", "player 2", "Legacy", "FFFB", "2", Clock.System.now()
            )
        )
        val uut = MTGResultsRepository(storage)
        val results = runBlocking { uut.find { it.format == "Legacy" } }
        assertEquals(1, results.count())
    }

    @Test
    fun `finds results with multiple predicates`() {
        val storage = mockk<Storage<MTGResult>>()
        coEvery { storage.getAll() } returns sequenceOf(
            MTGResult(
                "",
                "Best Deck Ever", "player 1", "Modern", "FFFF", "1", Clock.System.now()
            ),
            MTGResult(
                "",
                "Best Second Deck Ever", "player 5", "Modern", "FFFF", "2", Clock.System.now()
            ),
            MTGResult(
                "",
                "Best Legacy Deck Ever", "player 2", "Legacy", "FFFB", "10", Clock.System.now()
            )
        )
        val uut = MTGResultsRepository(storage)
        val results = runBlocking { uut.find(IsTop8.and(IsTop8)) }
        assertEquals(2, results.count())
    }
}