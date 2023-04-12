package io.nicronomicon.cli

import io.nicronomicon.mtg.DeckDownloader
import io.nicronomicon.mtg.RawDeck
import io.nicronomicon.utils.createLogger
import kotlinx.cli.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import java.nio.file.Path

@OptIn(ExperimentalCli::class)
class DeckDownloadCommand : Subcommand("download", "download decks") {
    private val ids by option(
        ArgType.String,
        shortName = "i",
        description = "list of ids"
    ).multiple().delimiter(",")

    private val inputPath by option(FilePath, shortName = "f", description = "path to newline delimited id file")
    private val outputDirectory by option(FilePath, shortName = "o", description = "path to download output directory")

    override fun execute() {
        val downloader = DeckDownloader()

        val outputPath = outputDirectory ?: Path.of("./")
        val idChannel = Channel<String>()
        runBlocking {
            val job = Job()
            job.invokeOnCompletion {
                idChannel.close()
            }
            val idList = launch {
                ids.forEach { idChannel.send(it) }
            }

            val idFileJob = launch {
                inputPath?.toFile()?.readLines()?.forEach {
                    idChannel.send(it)
                }
            }

            val downloaderJob = launch {
                downloadDecks(idChannel, downloader).saveDecks(outputPath)
            }

            listOf(idList, idFileJob).joinAll()
            job.complete()
            downloaderJob.join()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun CoroutineScope.downloadDecks(
        ids: ReceiveChannel<String>,
        downloader: DeckDownloader
    ) = produce {
        ids.consumeEach {
            send(downloader.downloadDeck(it.toInt()))
            delay(1000)
        }
        log.info("down downloading")
    }

    suspend fun ReceiveChannel<RawDeck>.saveDecks(output: Path) = consumeEach { deck ->
        val deckPath = output.resolve("${deck.name}.txt")
        val outFile = deckPath.toFile()
        withContext(Dispatchers.IO) {
            outFile.bufferedWriter().use {
                it.write(deck.data)
            }
        }
    }

    private companion object {
        val log = createLogger<DeckDownloadCommand>()
    }
}


