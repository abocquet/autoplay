package models

import graphics.DrawRequest
import graphics.Drawable
import graphics.renderers.AbstractRenderer
import physics.Physicable
import physics.Vector
import physics.behaviours.AbstractPhysicBehaviour
import java.awt.Dimension

open class AbstractObject(_physicBehaviour: AbstractPhysicBehaviour, _renderer: AbstractRenderer): Drawable, Physicable {

    override var position = Vector()
    val dimension = Dimension(30, 30)

    val physicBehaviour = _physicBehaviour
    private val renderer = _renderer

    override fun render(dr: DrawRequest, offset: Int){
        renderer.draw(this, dr, offset)
    }

    override fun update(delta_t: Double, objects: MutableList<AbstractObject>){
        physicBehaviour.update(this, delta_t, objects)
    }

}


