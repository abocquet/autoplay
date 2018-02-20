package bot

import graphics.renderers.TurtleRenderer
import level.Level
import physics.behaviours.InertiaBehaviour
import java.awt.Dimension

class TurtleBot(val x: Double, val y: Double, private val space: Double) : AbstractBot(x, y, Dimension(15, 25) * 2, InertiaBehaviour(), TurtleRenderer()){

    init {
        maxSpeed.x = 50.0
    }

    override fun act(level: Level, delta_t: Double) {
        if(position.x <= this.x){
            this.physicBehaviour.speed.x = this.maxSpeed.x
        }

        if(position.x >= this.x + this.space){
            this.physicBehaviour.speed.x = -this.maxSpeed.x
        }
    }

}

private operator fun Dimension.times(i: Int): Dimension { return Dimension(this.width * i, this.height * i)}
