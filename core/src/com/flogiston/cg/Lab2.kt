package com.flogiston.cg

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.flogiston.cg.domain.Coordinates
import com.flogiston.cg.domain.Primitive

class Lab2 : ApplicationAdapter() {

    private lateinit var shapeRenderer : ShapeRenderer
    private lateinit var primitive : Primitive
    private lateinit var centerOfScreen : Coordinates

    override fun create() {
        super.create()
        shapeRenderer = ShapeRenderer()
        primitive = Primitive(30, (Math.PI / 16).toFloat())
        centerOfScreen = Coordinates(Gdx.graphics.width / 2.0f, Gdx.graphics.height / 2.0f)
    }

    override fun render() {
        super.render()
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        drawPrimitive(centerOfScreen)
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
}
