//package io.nicronomicon.cli
//
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//import java.nio.file.Path
//
//internal class DeckDownloadCommandTest {
//
//    @Test
//    fun `handles empty ids`() {
//        val args = emptyArray<String>()
//        val uut =  DeckDownloadCommand(args, "test")
//        assertEquals(emptyList<String>(), uut.ids)
//    }
//    @Test
//    fun `parses ids from args`() {
//        val args = arrayOf("-i", "who,what,when,where")
//        val uut =  CliArguments(args, "test")
//        assertEquals(listOf("who", "what", "when", "where"), uut.ids)
//        assertEquals(null, uut.url)
//    }
//
//    @Test
//    fun `parses url from args`() {
//        val args = arrayOf("-u", "www.example.com")
//        val uut = CliArguments(args, "test")
//        assertEquals("www.example.com", uut.url)
//    }
//
//    @Test
//    fun `parses filePath from args`() {
//        val args = arrayOf("-f", "./thing.txt")
//        val uut = CliArguments(args, "test")
//        assertEquals(Path.of("./thing.txt"), uut.inputPath)
//    }
//
//    @Test
//    fun `parses output filepath`() {
//        val args = arrayOf("-o", "./thing/")
//        val uut = CliArguments(args, "test")
//        assertEquals(Path.of("./thing/"), uut.outputDirectory)
//    }
//}