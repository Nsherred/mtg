package io.nicronomicon.mtg.recommendation

import io.nicronomicon.utils.createLogger
import smile.math.MathEx
import smile.math.MathEx.log
import smile.math.matrix.Matrix
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToLong

class WhatRegression(data: Array<DoubleArray>) {
    val ttt = data.map { doubleArrayOf(ln(it[0]), it[1]) }
    val A = Matrix(ttt.map { doubleArrayOf(it[0], 1.0) }.toTypedArray())
    val B = Matrix(ttt.map { doubleArrayOf(it[1]) }.toTypedArray())
    val AT = A.transpose()
    val first = AT.mm(A)
    val second = first.inverse()
    val third = second.mm(AT)
    val fit = third.mm(B)
    private val f = fit.toArray()
//    private val cA = f[0][0]
//    private val cB = f[0][1]

    init {
//        log.info("$cA ")
    }

    fun predict(x: Double): Double {
        return f[1][0] + (f[0][0] * log(x))
    }

    private companion object {
        val log = createLogger<WhatRegression>()
    }
}