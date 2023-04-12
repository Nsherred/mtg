package io.nicronomicon.mtg.recommendation

import io.nicronomicon.mtg.model.CardQuantity
import io.nicronomicon.mtg.model.Deck

class DeckRecommendationProvider(
    decks: List<Deck>
) {

    private val main by lazy { DeckCluster(25, decks) { it.main } }
    private val side by lazy { DeckCluster(100, decks) { it.sideboard } }

    fun getSideRecommendations(deck: Deck) = getRecommendations(side, deck)

    fun getMainRecommendations(deck: Deck) = getRecommendations(main, deck)

    private fun getRecommendations(cluster: DeckCluster, deck: Deck): List<CardQuantity> {
        val cards = cluster.predictMeans(deck)
        return cards.filter {
            it.quantity > 0
                    && deck.countOf(it.card) < 4
                    && deck.countOf(it.card) < it.quantity
        }.sortedByDescending { it.quantity }.take(20)
    }


}