package io.nicronomicon.mtg.recommendation

import io.nicronomicon.utils.createLogger
import smile.math.MathEx
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToLong

interface Transform {
    fun transform(x: Double, y:Double): DoubleArray
}

class LogRegression(data: Array<DoubleArray>) : Transform {
    private var sumOfLogX = 0.0 // 0
    private var sumOfYxLogX = 0.0 // 1
    private var sumOfY = 0.0 // 2
    private var sumOfLogXexp2 = 0.0 // 3
    private val len = data.size
    private val coeffB: Long
    private val coeffA: Long
    private val size = data.size

    init {
        for (n in 0 until len) {
            val x = data[n][0]
            val y = data[n][1]
            sumOfLogX += ln(x)
            sumOfYxLogX += y * ln(x)
            sumOfY += y
            sumOfLogXexp2 += (ln(x).pow(2.0))
        }

        coeffB = ((sumOfYxLogX - (sumOfY * sumOfLogX)) /
                (sumOfLogXexp2 - sumOfLogX.pow(2))).roundToLong()
        coeffA = (sumOfY - (coeffB * sumOfLogX)).roundToLong()
        log.info("y = $coeffA - $coeffB * ln(x)")
    }

    private companion object {
        val log = createLogger<LogRegression>()
    }

    override fun transform(x: Double, y: Double): DoubleArray {
        val transformed = coeffA - (coeffB * ln(x))
        return doubleArrayOf(x, transformed)
    }
}