package io.nicronomicon.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.nicronomicon.mtg.CardResource
import io.nicronomicon.mtg.CardService


fun Application.cardPlugin(cardService: CardService) {

    routing {
        get<CardResource.Format.Search> {
            val cards = cardService.findByFormat(it.format)
                ?: return@get call.respond(HttpStatusCode.NotFound, "could not find cards with cmc ${it.format}")
            call.respond(cards)
        }
        get<CardResource.CMC.Search> {
            val cards = cardService.findByCmc(it.cmc)
                ?: return@get call.respond(HttpStatusCode.NotFound, "could not find cards with cmc ${it.cmc}")
            call.respond(cards)
        }
        get<CardResource.NameSearch> {
            val card = cardService.findCardByName(it.name)
                ?: return@get call.respond(HttpStatusCode.NotFound, "could not find a card with name ${it.name}")
            call.respond(card)
        }
    }
}
