package io.nicronomicon.mtg.recommendation

import io.nicronomicon.utils.createLogger
import smile.math.MathEx
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToLong

class PolyRegression(data: Array<DoubleArray>) {
    private val x = data.map { it[0] }
    private val y = data.map { it[1] }
    val xm = x.average()
    val ym = y.average()
    val x2m = x.map { it * it }.average()
    val x3m = x.map { it * it * it }.average()
    val x4m = x.map { it * it * it * it }.average()
    val xym = x.zip(y).map { it.first * it.second }.average()
    val x2ym = x.zip(y).map { it.first * it.first * it.second }.average()

    val sxx = x2m - xm * xm
    val sxy = xym - xm * ym
    val sxx2 = x3m - xm * x2m
    val sx2x2 = x4m - x2m * x2m
    val sx2y = x2ym - x2m * ym

    val b = (sxy * sx2x2 - sx2y * sxx2) / (sxx * sx2x2 - sxx2 * sxx2)
    val c = (sx2y * sxx - sxy * sxx2) / (sxx * sx2x2 - sxx2 * sxx2)
    val a = ym - b * xm - c * x2m

    fun predict(xx: Double) = a + b * xx + c * xx * xx



    private companion object {
        val log = createLogger<PolyRegression>()
    }
}