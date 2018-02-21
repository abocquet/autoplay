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
    override var life = 1
        get
        set(value) {
            if(value <= 2){
                field = value
            }

            if(life == 1){
                this.dimension.height = 40
            } else if(life == 2){
                this.dimension.height = 80
            }
        }
}