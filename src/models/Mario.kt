package models

import graphics.renderers.MarioRenderer
import physics.behaviours.GravityBehaviour
import java.awt.Dimension

class Mario(x: Double, y: Double) : People(
        x, y,
        Dimension(40, 40),
        GravityBehaviour(),
        MarioRenderer())
{
    var hurtedClock = -10.0
    val isHurted : Boolean
        get () {
            if(hurtedClock in 0.0..3.0) {
                return true
            } else {
                hurtedClock = -1.0
                return false
            }
        }

    override fun update(delta_t: Double, objects: List<AbstractObject>){
        physicBehaviour.update(this, delta_t, objects)

        if(hurtedClock >= 0.0) {
            hurtedClock += delta_t
        }
    }

    override var life = 1
        set(value) {
            if(value < field){
                hurtedClock = 0.0
            }

            field = value

            if(life == 1){
                this.dimension.height = 40
            } else {
                this.dimension.height = 80
            }
        }
}