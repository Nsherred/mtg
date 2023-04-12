package io.nicronomicon.cli

import io.nicronomicon.ecs.storage.fileSystemStorage
import io.nicronomicon.mtg.CardService
import io.nicronomicon.mtg.DeckService
import io.nicronomicon.mtg.ScryFallCollectionProvider
import io.nicronomicon.mtg.model.Deck
import io.nicronomicon.mtg.model.WithCards
import io.nicronomicon.mtg.recommendation.DeckCluster
import io.nicronomicon.utils.createLogger
import kotlinx.cli.*
import kotlinx.coroutines.runBlocking
import smile.plot.swing.ScatterPlot

private enum class Type {
    Main,
    Side,
    Both
}

@OptIn(ExperimentalCli::class)
class DeckAnalysisCommand : Subcommand("cluster", "cluster decks") {

    private val inputPath by option(
        FilePath,
        shortName = "i",
        description = "path to deck"
    ).required()

    private val projection by option(
        ArgType.Int,
        shortName = "p",
        description = "type of graph"
    ).default(2)

    private val clusters by option(
        ArgType.Int,
        shortName = "c",
        description = "number of clusters"
    ).default(20)

    private val type by option(
        ArgType.Choice<Type>(),
        shortName = "t",
        description = "type"
    ).default(Type.Main)

    override fun execute() {
        val collectionProvider = ScryFallCollectionProvider()
        val cardService = CardService(collectionProvider)
        val deckStorage = Deck::class.fileSystemStorage(inputPath)
        val deckService = DeckService(cardService, deckStorage)
        val decks = runBlocking {
            deckService.all().toList()
        }
        log.info("clustering '${decks.size}' decks from $inputPath")

        val provider: (deck: Deck) -> WithCards = when (type) {
            Type.Main -> { it -> it.main }
            Type.Side -> { it -> it.sideboard }
            Type.Both -> { it -> it }
        }
        val deckCluster = DeckCluster(clusters, decks, provider)

        val (data, cluster) = deckCluster.pca(projection)
        val canvas = ScatterPlot.of(data, cluster.y, '.').canvas()
        canvas.window()

    }

    private companion object {
        val log = createLogger<DeckRecommendationCommand>()
    }
}
