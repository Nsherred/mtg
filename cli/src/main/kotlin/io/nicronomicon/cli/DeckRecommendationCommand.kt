package io.nicronomicon.cli

import io.nicronomicon.ecs.ID
import io.nicronomicon.ecs.storage.fileSystemStorage
import io.nicronomicon.mtg.CardService
import io.nicronomicon.mtg.DeckService
import io.nicronomicon.mtg.ScryFallCollectionProvider
import io.nicronomicon.mtg.model.Deck
import io.nicronomicon.mtg.recommendation.DeckRecommendationProvider
import io.nicronomicon.utils.createLogger
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.required
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalCli::class)
class DeckRecommendationCommand : Subcommand("recommend", "get recommendations for a deck") {

    private val inputPath by option(
        FilePath,
        shortName = "i",
        description = "path to deck"
    ).required()

    private val deckId by option(
        ArgType.String,
        shortName = "d",
        description = "id of deck"
    ).required()

    override fun execute() {

        val collectionProvider = ScryFallCollectionProvider()
        val cardService = CardService(collectionProvider)
        val deckStorage = Deck::class.fileSystemStorage(inputPath)
        val deckService = DeckService(cardService, deckStorage)
        val decks = runBlocking {
            deckService.all().toList()
        }
        val deck = runBlocking {
            deckService.find(ID(deckId))
        } ?: throw Exception("could not find a deck with id '$deckId'")

        log.info("clustering '${decks.size}' decks from $inputPath")
        val recommender = DeckRecommendationProvider(decks)

        val recommendations = recommender.getMainRecommendations(deck)
        println(recommendations.joinToString("\n"))

    }

    private companion object {
        val log = createLogger<DeckRecommendationCommand>()
    }
}

//
//val collectionProvider = TestCollectionProvider()
//val cardService = CardService(collectionProvider)
//val deckStorage = Deck::class.fileSystemStorage(Path.of("/Users/nsherred/projects/mtg-core/decks/parsed"))
//val deckService = DeckService(cardService, deckStorage)
//val decks = runBlocking {
//    deckService.all().toList()
//}
//val uut = Recommender(decks)
//val data = uut.getArray()
////        val clusters = PartitionClustering.run(200) {
////            KMeans.fit(data, 20)
////        }
//
//val clusters = HierarchicalClustering.fit(CompleteLinkage.of(data));
//Dendrogram(clusters.tree, clusters.height).canvas().window();