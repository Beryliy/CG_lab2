package com.flogiston.cg.domain

import kotlin.math.cos
import kotlin.math.sin

class Primitive (
        n : Int, //pi * n
        val step : Float
) {
    val tMin = 0.0f
    val tMax = (Math.PI * n).toFloat()

    fun pointsOfPrimitive() : Array<Coordinates> {
        val t = 0.0f
        val numPoints = ((tMax - tMin) / step).toInt() + 1
        val coordinates = Array(numPoints) { i ->
            Coordinates(t * sin(t), t * cos(t))
        }
        return coordinates
    }
}