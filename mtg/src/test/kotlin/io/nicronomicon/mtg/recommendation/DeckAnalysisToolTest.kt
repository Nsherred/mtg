package io.nicronomicon.mtg.recommendation

import io.nicronomicon.ecs.storage.fileSystemStorage
import io.nicronomicon.mtg.CardService
import io.nicronomicon.mtg.DeckService
import io.nicronomicon.mtg.TestCollectionProvider
import io.nicronomicon.mtg.model.Deck
import io.nicronomicon.utils.createLogger
import io.nicronomicon.utils.defaultJson
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test
import smile.data.DataFrame
import smile.data.formula.Formula
import smile.math.MathEx.mean
import smile.math.MathEx.sum
import smile.math.matrix.Matrix
import smile.plot.swing.Histogram
import smile.plot.swing.LinePlot
import smile.regression.OLS
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.createTempFile
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.test.Ignore

@Ignore
internal class DeckAnalysisToolTest {

    private fun testRegression(
        builder: (data: Array<DoubleArray>) -> Transform
    ): Pair<Array<DoubleArray>, Array<DoubleArray>> {
        val text = this::class.java.getResource("/clustering-data.json")!!.readText()
        val data = defaultJson.decodeFromString<List<DoubleArray>>(text).toTypedArray()
        val model = builder(data)
        val real = data.map { model.transform(it[0], it[1]) }.toTypedArray()
        return data to real
    }

    @Test
    fun logarithmicRegressionTest() {
        val (real, predicted) = testRegression {
            object : Transform {
                override fun transform(x: Double, y: Double): DoubleArray {
                    val pY = x.pow(2) / ((x).pow(2) - 2)
                    return doubleArrayOf(x, y)
                }
            }
        }

//        val original = ScatterPlot.of(real.take(30).toTypedArray()).canvas()
        val fitted = LinePlot.of(predicted.take(400).toTypedArray()).canvas()
//        original.add(fitted)
        val file = createTempFile("image", ".png")
        ImageIO.write(fitted.toBufferedImage(1000, 1000), "png", file.toFile())
        Runtime.getRuntime().exec("open $file")
    }

    @Test
    fun `OLS fit test`() {
        val text = this::class.java.getResource("/clustering-data.json")!!.readText()
        val data = defaultJson.decodeFromString<List<DoubleArray>>(text).toTypedArray()
        val thing = data.map { doubleArrayOf(ln(it[0]), it[1]) }.toTypedArray()
        val frame = DataFrame.of(thing, "log(x)", "y")
        val model = OLS.fit(Formula.lhs("y"), frame)
        val fitted = Histogram.of(model.residuals()).canvas()
//        Plot.show(Histogram.create("Runs Scored Residuals",runsScored.residuals()));
        val result = data.map { doubleArrayOf(it[0], model.predict(it)) }.toTypedArray()
//        val fitted = ScatterPlot.of(thing).canvas()
//        val real = LinePlot.of(data)
//        fitted.add(real)
        val file = createTempFile("image", ".png")
        ImageIO.write(fitted.toBufferedImage(1000, 1000), "png", file.toFile())
        Runtime.getRuntime().exec("open $file")
    }

    @Test
    fun exponentialRegression() {
        val data = this::class.java.getResource("/clustering-data.json")!!.readText()
        val results = defaultJson.decodeFromString<List<DoubleArray>>(data)
        val thing = results.map { it[1].toInt() }.toIntArray()

        val newData = results.map { doubleArrayOf(it[0], ln(it[1])) }
        val xMean = mean(newData.map { it[0] }.toDoubleArray())
        val yLogMean = mean(newData.map { it[1] }.toDoubleArray())
        val what = sum(newData.map { (it[0] - xMean).pow(2) }.toDoubleArray())
        val slope = sum(newData.map {
            (it[0] - xMean) * (it[1] - yLogMean)
        }.toDoubleArray()) / what
        val intercept = mean(newData.map { it[1] - it[0] * slope }.toDoubleArray())

        val final = results.map {
            doubleArrayOf(it[0], (exp(intercept) * exp(slope * it[0])))
        }.toTypedArray()
        val fitted = LinePlot.of(final).canvas()
//        val real = LinePlot.of(results.toTypedArray())
//        fitted.add(real)
        val file = createTempFile("image", ".png")
        ImageIO.write(fitted.toBufferedImage(1000, 1000), "png", file.toFile())
        Runtime.getRuntime().exec("open $file")
//        fitted.window()
//        Thread.sleep(20000)
    }

    @Test
    fun `test QR`() {
        val m = Array(6) { DoubleArray(5) { 0.0 } }
        m[0][0] = 5.0
        m[0][1] = 5.0
        m[0][2] = 0.0
        m[0][3] = 0.0
        m[0][4] = 1.0

        m[1][0] = 4.0
        m[1][1] = 5.0
        m[1][2] = 1.0
        m[1][3] = 1.0
        m[1][4] = 0.0

        m[2][0] = 5.0
        m[2][1] = 4.0
        m[2][2] = 1.0
        m[2][3] = 1.0
        m[2][4] = 0.0

        m[3][0] = 0.0
        m[3][1] = 0.0
        m[3][2] = 4.0
        m[3][3] = 4.0
        m[3][4] = 4.0

        m[4][0] = 0.0
        m[4][1] = 0.0
        m[4][2] = 5.0
        m[4][3] = 5.0
        m[4][4] = 5.0


        m[5][0] = 1.0
        m[5][1] = 1.0
        m[5][2] = 4.0
        m[5][3] = 4.0
        m[5][4] = 4.0

        val matrix = Matrix(m)
        val svd = matrix.svd()
        val rank = svd.rank()
        println(rank)
        println(matrix)
        val U = svd.U.submatrix(0, 0, 5, 1)
        val D = svd.diag().submatrix(0, 0, rank - 3, rank - 3)
        val V = svd.V.transpose().submatrix(0, 0, rank - 3, rank)
        println("U (range)")
        println(U)
        println("D")
        println(D)
        println("V transposed")
        println(V)
        println(U.mm(D).mm(V))
    }

    @Test
    fun buildMatrix() {
        val collectionProvider = TestCollectionProvider()
        val cardService = CardService(collectionProvider)
        val deckStorage = Deck::class.fileSystemStorage(Path.of("/Users/nsherred/projects/mtg-core/decks/parsed"))
        val deckService = DeckService(cardService, deckStorage)
        val decks = runBlocking {
            deckService.all().toList()
        }
        val uut = DeckAnalysisTool(decks)
        val results = runBlocking {
            val results = arrayListOf<DoubleArray>()
            val start = 2.0
            val end = 416.0
            val runs = end - start
            val concurrency = 8.0
            val chunkSize = (runs / concurrency).roundToInt()
            val chunks = (2..end.toInt()).chunked(chunkSize)
            runConcurrent(concurrency.toInt()) {
                chunks[it].forEach { clusterSize ->
                    log.info("running clustering for $clusterSize centroids")
                    val cluster = uut.cluster(clusterSize, 20)
                    results.add(doubleArrayOf(clusterSize.toDouble(), cluster.second.distortion))
                }
            }
            results
        }
        val dataOut = createTempFile("data", ".json")
        val outFile = dataOut.toFile()
        val sorted = results.sortedBy { it[0] }
        val resultList = sorted.toList()
        outFile.bufferedWriter().use {
            val data = defaultJson.encodeToString(resultList)
            it.write(data)
        }

        val image = LinePlot.of(sorted.toTypedArray()).canvas().toBufferedImage(1000, 1000)
        val file = createTempFile("image", ".png")
        ImageIO.write(image, "png", file.toFile())
        Runtime.getRuntime().exec("open $file")
        Runtime.getRuntime().exec("open $dataOut")
    }

    companion object {
        val log = createLogger<DeckAnalysisToolTest>()
    }

    private suspend fun <T> runConcurrent(times: Int, body: suspend (index: Int) -> T) {
        withContext(Dispatchers.IO) {
            val jobs = mutableListOf<Job>()
            for (i in 0 until times) {
                launch {
                    body(i)
                }
            }
            jobs.joinAll()
        }
    }
}