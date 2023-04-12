package io.nicronomicon.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.nicronomicon.mtg.DeckResource
import io.nicronomicon.mtg.DeckService
import io.nicronomicon.mtg.model.Deck
import io.nicronomicon.mtg.recommendation.DeckRecommendationProvider


fun Application.deckPlugin(
    service: DeckService,
    deckClusters: DeckRecommendationProvider
) {
    routing {
        get<DeckResource.Id> {
            val deck = service.find(it.id) ?: throw Exception(
                "Could not find a deck with id: '${it.id}'"
            )
            call.respond(deck)
        }
        post<DeckResource> {
            val deck = call.receive<Deck>()
            call.respond(service.createDeck(deck))
        }
        post("/deck/recommendation") {
            val text = call.receiveText()
            val deck = service.parseDeckFromText(text)
            val recommendations = deckClusters.getMainRecommendations(deck)
            call.respond(recommendations)
        }

        post("/deck/recommendation/side") {
            val text = call.receiveText()
            val deck = service.parseDeckFromText(text)
            val recommendations = deckClusters.getSideRecommendations(deck)
            call.respond(recommendations)
        }
    }
}

