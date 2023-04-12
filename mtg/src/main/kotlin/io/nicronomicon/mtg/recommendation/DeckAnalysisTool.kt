package io.nicronomicon.mtg.recommendation

import io.nicronomicon.mtg.model.Deck
import smile.clustering.HierarchicalClustering
import smile.clustering.KMeans
import smile.clustering.PartitionClustering
import smile.clustering.linkage.CompleteLinkage
import smile.math.matrix.Matrix
import smile.plot.swing.*
import smile.projection.PCA

class DeckAnalysisTool(
    private val decks: List<Deck>
) {

    val cards = decks
        .map { it.main.cards }
        .flatten()
        .map {
            it.card
        }.toSortedSet(compareBy {
            it.name
        }).toTypedArray()

    private val data: Array<DoubleArray> by lazy { getArray() }

    fun dendrogram(): Dendrogram {
        val clusters = HierarchicalClustering.fit(CompleteLinkage.of(data))
        return Dendrogram(clusters.tree, clusters.height)
    }

    fun cluster(
        numberOfClusters: Int = 20,
        runs: Int = 200
    ): Pair<Array<DoubleArray>, KMeans> {
        val clusters = PartitionClustering.run(runs) {
            KMeans.fit(data, numberOfClusters)
        }
        return data to clusters
    }

    fun pca3d(): Pair<Array<DoubleArray>, KMeans> {
        val projected = PCA.fit(data)
            .setProjection(3)
            .project(data)
        val clusters = PartitionClustering.run(10) {
            KMeans.fit(data, 20)
        }
        return projected to clusters
    }

    fun pca2d(): Pair<Array<DoubleArray>, KMeans> {
        val projected = PCA.fit(data)
            .setProjection(2)
            .project(data)
        val clusters = PartitionClustering.run(10) {
            KMeans.fit(data, 20)
        }
        return projected to clusters
    }


    private fun getArray(): Array<DoubleArray> {
        val rows = cards.size
        val columns = decks.size

        val m = Array(columns) { DoubleArray(rows) { 0.0 } }
        for (c in 0 until columns) {
            val deck = decks[c]
            for (r in 0 until rows) {
                val card = cards[r]
                m[c][r] = deck.countOf(card)
            }
        }
        return m
    }

    fun pcaScree(): Canvas {
        val pca = PCA.fit(data)
        return ScreePlot(pca).canvas()
    }

    fun matrix(): Matrix {
        return Matrix(data)
    }

    fun getRecommendations(cluster: KMeans, testDeck: Deck): List<Pair<String, Double>> {
        val groups = mutableMapOf<Int, ArrayList<DoubleArray>>()
        for (i in data.indices) {
            val group = cluster.y[i]
            groups.getOrPut(group) { arrayListOf() }.add(data[i])
        }
        val targetGroup = cluster.predict(toData(testDeck))
        val group = groups[targetGroup]!!.toTypedArray()
        val cardMeans = Matrix(group).colMeans()
        return cardMeans.mapIndexed { i, d ->
            cards[i]!! to d
        }.filter {
            it.second > 0
                    && testDeck.countOf(it.first) < 4
                    && testDeck.countOf(it.first) < it.second
        }
            .sortedByDescending { it.second }
            .take(10).map { it.first.name to it.second }
    }

    private fun toData(deck: Deck): DoubleArray {
        val rows = cards.size

        val m = DoubleArray(rows) { 0.0 }
        for (r in 0 until rows) {
            val card = cards[r]
            m[r] = deck.countOf(card)
        }
        return m
    }
}