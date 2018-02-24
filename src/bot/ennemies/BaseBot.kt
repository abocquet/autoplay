package bot.ennemies

import bot.AbstractBot
import graphics.renderers.AbstractRenderer
import level.Level
import physics.behaviours.InertiaBehaviour
import java.awt.Dimension

class BaseBot(val x: Double, val y: Double, dimension: Dimension, private val space: Double, renderer: AbstractRenderer) : AbstractBot(x, y, dimension, InertiaBehaviour(), renderer)
{
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

operator fun Dimension.times(i: Int): Dimension { return Dimension(this.width * i, this.height * i)}
