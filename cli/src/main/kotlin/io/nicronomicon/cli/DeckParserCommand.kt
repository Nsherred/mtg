package io.nicronomicon.cli

import io.nicronomicon.ecs.storage.fileSystemStorage
import io.nicronomicon.mtg.CardService
import io.nicronomicon.mtg.DeckService
import io.nicronomicon.mtg.ScryFallCollectionProvider
import io.nicronomicon.mtg.model.Deck
import io.nicronomicon.utils.createLogger
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required
import kotlinx.coroutines.runBlocking
import java.nio.file.Path

@OptIn(ExperimentalCli::class)
class DeckParserCommand : Subcommand("parse", "parse decks") {

    private val inputPath by option(FilePath, shortName = "i", description = "path to deck").required()
    private val outputDirectory by option(
        FilePath,
        shortName = "o",
        description = "path to download output directory"
    ).default(Path.of("./"))

    override fun execute() {
        val collectionProvider = ScryFallCollectionProvider()
        val cardService = CardService(collectionProvider)

        val deckStorage = Deck::class.fileSystemStorage(outputDirectory)
        val deckService = DeckService(cardService, deckStorage)
        val inputDirectory = inputPath.toFile()
        log.info("parsing decks from $inputDirectory into $outputDirectory")
        runBlocking {
            inputDirectory.walkTopDown().forEach {
                if (it.name.contains(".txt")) {
                    val fileContent = it.readText()
                    deckService.createDeckFromText(fileContent)
                }
            }
        }
    }

    private companion object {
        val log = createLogger<DeckParserCommand>()
    }
}


