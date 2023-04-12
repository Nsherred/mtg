package io.nicronomicon.mtg.result

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MTGResultsFileSystemStorageTest {

    @Test
    fun `handles file not existing`() {
        val file = "./src/test/resources/not-there.json"
        val uut = MTGResultsFileSystemStorage(filePath = file)
        runBlocking { uut.getAll() }
    }

    @Test
    fun `loads mtg results during instantiation`() {
        val file = "./src/test/resources/test-results.json"
        val uut = MTGResultsFileSystemStorage(filePath = file)
        val results = runBlocking { uut.getAll() }
        assertEquals(2, results.count())
        val firstPlace = results.find { it.placement == "1" }
        assertEquals("1", firstPlace?.placement)
        assertEquals("UR Aggro", firstPlace?.name)
    }
}