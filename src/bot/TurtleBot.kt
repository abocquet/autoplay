package bot

import level.Level
import graphics.renderers.TurtleRenderer
import physics.behaviours.InertiaBehaviour
import java.awt.Dimension

class TurtleBot(val x: Double, val y: Double, val space: Double) : AbstractBot(x, y, Dimension(30, 30), InertiaBehaviour(), TurtleRenderer()){

    init {
        maxSpeed.x = 50.0
    }

    override fun act(level: Level) {
        if(position.x <= this.x){
            this.physicBehaviour.speed.x = this.maxSpeed.x
        }

        if(position.x >= this.x + this.space){
            this.physicBehaviour.speed.x = -this.maxSpeed.x
        }
    }

}
