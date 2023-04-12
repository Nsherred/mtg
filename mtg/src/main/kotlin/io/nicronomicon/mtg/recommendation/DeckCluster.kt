package io.nicronomicon.mtg.recommendation

import io.nicronomicon.mtg.model.Card
import io.nicronomicon.mtg.model.CardQuantity
import io.nicronomicon.mtg.model.Deck
import io.nicronomicon.mtg.model.WithCards
import smile.clustering.KMeans
import smile.clustering.PartitionClustering
import smile.math.matrix.Matrix
import smile.plot.swing.Canvas
import smile.plot.swing.ScreePlot
import smile.projection.PCA
import kotlin.math.roundToInt

class DeckCluster(
    private val clusters: Int,
    private val decks: List<Deck>,
    private val provider: (deck: Deck) -> WithCards
) {
    private val cards = decks.map {
        provider(it).cards
    }.flatten().map {
        it.card
    }.toSortedSet(compareBy {
        it.name
    }).toTypedArray()
    private val data by lazy { cards.getData() }
    private val cluster by lazy { clusterDecks(data, clusters) }
    private val groups by lazy { buildGroups(data, cluster) }

    private fun clusterDecks(
        data: Array<DoubleArray>, numberOfClusters: Int = 20, runs: Int = 200
    ) = PartitionClustering.run(runs) {
        KMeans.fit(data, numberOfClusters)
    }

    private fun Array<Card>.getData(): Array<DoubleArray> {
        val rows = size
        val columns = decks.size

        val m = Array(columns) { DoubleArray(rows) { 0.0 } }
        for (c in 0 until columns) {
            val deck = decks[c]
            for (r in 0 until rows) {
                val card = this[r]
                m[c][r] = deck.countOf(card)
            }
        }
        return m
    }


    private fun buildGroups(
        data: Array<DoubleArray>,
        cluster: KMeans
    ): Map<Int, ArrayList<DoubleArray>> {
        val groups = mutableMapOf<Int, ArrayList<DoubleArray>>()
        for (i in data.indices) {
            val group = cluster.y[i]
            groups.getOrPut(group) { arrayListOf() }.add(data[i])
        }
        return groups
    }

    fun predictMeans(deck: Deck): List<CardQuantity> {
        val deckData = toData(deck)
        val predictedGroup = cluster.predict(deckData)
        val group = groups[predictedGroup]!!.toTypedArray()
        val cardMeans = Matrix(group).colMeans()
        return cardMeans.mapIndexed { i, d -> CardQuantity(d.roundToInt(), cards[i]!!) }
    }

    private fun toData(deck: Deck): DoubleArray {
        val rows = cards.size

        val m = DoubleArray(rows) { 0.0 }
        for (r in 0 until rows) {
            val card = cards[r]
            m[r] = provider(deck).countOf(card)
        }
        return m
    }

    fun pca(projection: Int): Pair<Array<DoubleArray>, KMeans> {
        val projected = PCA.fit(data)
            .setProjection(projection)
            .project(data)
        val clusters = PartitionClustering.run(20) {
            KMeans.fit(data, clusters)
        }
        return projected to clusters
    }

    fun pcaScree(): Canvas {
        val pca = PCA.fit(data)
        return ScreePlot(pca).canvas()
    }

}