package com.flogiston.cg

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.flogiston.cg.domain.Coordinates
import com.flogiston.cg.domain.Pattern
import com.flogiston.cg.domain.Primitive

class Lab2 : ApplicationAdapter() {

    private lateinit var shapeRenderer : ShapeRenderer
    private lateinit var primitive : Primitive
    private lateinit var pattern : Pattern
    private lateinit var centerOfScreen : Coordinates

    override fun create() {
        super.create()
        shapeRenderer = ShapeRenderer()
        val step = (Math.PI / 16).toFloat()
        centerOfScreen = Coordinates(Gdx.graphics.width / 2.0f, Gdx.graphics.height / 2.0f)
        primitive = Primitive(SIZE, centerOfScreen, step, 0.0f)
        var coordinatesOfLastSpiral = Coordinates(0.0f, 0.0f)
        var rotationAngle = 0.0f
        val rotationOffset = (Math.PI / 4).toFloat()
        val primitives = mutableListOf<Primitive>()
        for(i in 0..NUMBER_OF_PRIMITIVES - 1) {
            val p = Primitive(SIZE, coordinatesOfLastSpiral, step, rotationAngle)
            primitives += p
            rotationAngle -= rotationOffset
            coordinatesOfLastSpiral = p.primitivePoints.last()
        }
        pattern = Pattern(primitives.toList())
    }

    override fun render() {
        super.render()
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        drawPattern(centerOfScreen)
    }

    override fun dispose() {
        super.dispose()
        shapeRenderer.dispose()
    }

    private fun drawPrimitive(position : Coordinates) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.BLUE
        for (i in 0..primitive.numPoints - 2) {
            shapeRenderer.line(
                    position.x + primitive.primitivePoints[i].x,
                    position.y + primitive.primitivePoints[i].y,
                    position.x + primitive.primitivePoints[i + 1].x,
                    position.y + primitive.primitivePoints[i + 1].y)
        }
        shapeRenderer.end()
    }

    private fun drawPattern(position : Coordinates) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.BLUE
        pattern.primitives.forEach {
            for (i in 0..it.numPoints - 2) {
                shapeRenderer.line(
                        position.x + it.primitivePoints[i].x,
                        position.y + it.primitivePoints[i].y,
                        position.x + it.primitivePoints[i + 1].x,
                        position.y + it.primitivePoints[i + 1].y
                )
            }
        }
        shapeRenderer.end()
    }

    companion object {
        const val NUMBER_OF_PRIMITIVES = 8
        const val SIZE = 32
    }
}
