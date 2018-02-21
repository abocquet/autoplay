package models

import graphics.DrawRequest
import graphics.Drawable
import graphics.renderers.AbstractRenderer
import physics.Physicable
import physics.Vector
import physics.behaviours.AbstractPhysicBehaviour
import java.awt.Dimension

open class AbstractObject(val physicBehaviour: AbstractPhysicBehaviour, val renderer: AbstractRenderer): Drawable, Physicable {

    final override var position = Vector()
    val dimension = Dimension(30, 30)

    override fun render(dr: DrawRequest, offset: Int, delta_t: Double){
        renderer.draw(this, dr, offset, delta_t)
    }

    override fun update(delta_t: Double, objects: List<AbstractObject>){
        physicBehaviour.update(this, delta_t, objects)
    }

}


