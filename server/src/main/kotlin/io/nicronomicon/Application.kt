package io.nicronomicon

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.resources.*
import io.nicronomicon.ecs.storage.fileSystemStorage
import io.nicronomicon.mtg.CardService
import io.nicronomicon.mtg.DeckService
import io.nicronomicon.mtg.ScryFallCollectionProvider
import io.nicronomicon.mtg.model.Deck
import io.nicronomicon.mtg.recommendation.DeckRecommendationProvider
import io.nicronomicon.plugins.cardPlugin
import io.nicronomicon.plugins.configureGraphQl
import io.nicronomicon.plugins.configureSockets
import io.nicronomicon.plugins.deckPlugin
import io.nicronomicon.utils.defaultClient
import io.nicronomicon.utils.defaultJson
import kotlinx.coroutines.runBlocking
import java.nio.file.Path

fun main() {
    val inputPath = Path.of("/Users/nsherred/projects/mtg-core/decks/parsed")
    val deckStorage = Deck::class.fileSystemStorage(inputPath)
    val collectionProvider = ScryFallCollectionProvider(
        defaultClient,
        defaultJson
    )
    val cardService = CardService(collectionProvider)
    val deckService = DeckService(cardService, deckStorage)

    val decks = runBlocking {
        deckService.all()
    }
    val deckClusters = DeckRecommendationProvider(decks.toList())

    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        install(Resources)
        install(CORS) {
            allowHost("localhost:8081")
            allowHeader(HttpHeaders.ContentType)
        }
        install(ContentNegotiation) {
            json()
        }
        configureSockets()
        cardPlugin(cardService)
        deckPlugin(deckService, deckClusters)
        configureGraphQl()
    }.start(wait = true)
}
