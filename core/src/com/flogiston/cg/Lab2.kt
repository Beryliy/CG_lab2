package com.flogiston.cg

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flogiston.cg.domain.Coordinates
import com.flogiston.cg.domain.Pattern
import com.flogiston.cg.domain.Primitive
import com.flogiston.cg.domain.SpecialEffect

class Lab2 : ApplicationAdapter() {

    private lateinit var shapeRenderer : ShapeRenderer
    private lateinit var centerOfScreen : Coordinates
    private lateinit var primitive : Primitive
    private lateinit var pattern : Pattern
    private lateinit var specialEffect : SpecialEffect
    private var initialTimeMoment = -1L
    private lateinit var stage : Stage
    var drawModele : (position : Coordinates) -> Unit = this::drawPrimitive

    override fun create() {
        super.create()
        initialTimeMoment = TimeUtils.nanoTime()
        shapeRenderer = ShapeRenderer()
        centerOfScreen = Coordinates(Gdx.graphics.width / 2.0f + Gdx.graphics.width / 8.0f, Gdx.graphics.height / 2.0f)

        //setup models
        val step = (Math.PI / 16).toFloat()
        primitive = Primitive(SIZE, Coordinates(0.0f, 0.0f), step, 0.0f)
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
        specialEffect = SpecialEffect(primitives, 0.0f)

        //setup buttons
        stage = Stage(ScreenViewport())
        Gdx.input.inputProcessor = stage
        val buttonSkin = Skin(Gdx.files.internal("neon/skin/neon-ui.json"))
        val buttonHeight = Gdx.graphics.height / 16.0f
        val buttonWidth = Gdx.graphics.width / 3.0f
        val primitiveButton = TextButton("primitive", buttonSkin)
        setupButton(primitiveButton, this::drawPrimitive, Coordinates(0.0f, 0.0f), buttonWidth, buttonHeight)
        val patternButton = TextButton("pattern", buttonSkin)
        setupButton(patternButton, this::drawPattern, Coordinates(buttonWidth, 0.0f), buttonWidth, buttonHeight)
        val specialEffectButton = TextButton("specialEffect", buttonSkin)
        setupButton(specialEffectButton, this::drawSpecialEffect, Coordinates(buttonWidth + buttonWidth, 0.0f), buttonWidth, buttonHeight)
        stage.addActor(primitiveButton)
        stage.addActor(patternButton)
        stage.addActor(specialEffectButton)
    }

    override fun render() {
        super.render()
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
        drawModele(centerOfScreen)
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

    private fun drawSpecialEffect(position : Coordinates) {
        val fractialPeriodPart = ((TimeUtils.nanoTime() - initialTimeMoment) % PERIOD) / PERIOD
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.BLUE
        specialEffect.rotationAngle = (Math.PI * 2 * fractialPeriodPart).toFloat()
        specialEffect.primitives.forEach {
            val dislodgedPoints = it.getDislodgetPoints(specialEffect.rotationAngle)
            for (i in 0..it.numPoints - 2) {
                shapeRenderer.line(
                        position.x + dislodgedPoints[i].x,
                        position.y + dislodgedPoints[i].y,
                        position.x + dislodgedPoints[i + 1].x,
                        position.y + dislodgedPoints[i + 1].y
                )
            }
        }
        shapeRenderer.end()
    }

    private fun setupButton(
            button : TextButton,
            drawMethod : (position : Coordinates) -> Unit,
            pos : Coordinates,
            width : Float,
            height : Float) {
        button.setPosition(pos.x, pos.y)
        button.setSize(width, height)
        button.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                drawModele = drawMethod
                return true
            }
        })
    }

    companion object {
        const val NUMBER_OF_PRIMITIVES = 8
        const val PERIOD = 4_000_000_000.0f
        const val SIZE = 32
    }
}
