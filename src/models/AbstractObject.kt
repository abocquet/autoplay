package models

import graphics.DrawRequest
import graphics.Drawable
import graphics.renderers.AbstractRenderer
import physics.Physicable
import physics.Vector
import physics.behaviours.AbstractPhysicBehaviour
import java.awt.Dimension

open class AbstractObject(val physicBehaviour: AbstractPhysicBehaviour, val renderer: AbstractRenderer, val dimension: Dimension = Dimension(30,30)): Drawable, Physicable {

    final override var position = Vector()

    override fun render(r: DrawRequest, offset: Int, delta_t: Double){
        renderer.draw(this, r, offset, delta_t)
    }

    override fun update(delta_t: Double, objects: List<AbstractObject>){
        physicBehaviour.update(this, delta_t, objects)
    }

    fun touches(other: AbstractObject) : Boolean {
        val x = position.x
        val y = position.y
        val w = dimension.width
        val h = dimension.height

        val ox = other.position.x
        val oy = other.position.y
        val ow = other.dimension.width
        val oh = other.dimension.height
        return (x == ox + ow || ox == x + w) && (y+h >= oy && y <= oy+oh) ||
               (y == oy + oh || oy == y+h) && (x+w >= ox && x <= ox+ow)
    }

}


