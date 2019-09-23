package com.flogiston.cg.domain

import kotlin.math.cos
import kotlin.math.sin

class Primitive (
        n : Int, //pi * n
        val center : Coordinates,
        val step : Float,
        var angle : Float
) {
    val tMin = 0.0f
    val tMax = (Math.PI * n).toFloat()
    val numPoints = ((tMax - tMin) / step).toInt() + 1
    val primitivePoints : Array<Coordinates> by lazy {
        Array(numPoints) { i ->
            val t = i * step
            Coordinates(center.x + t * sin((t + angle)), center.y + t * cos(t + angle))
        }
    }
}