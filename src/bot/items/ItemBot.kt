package bot.items

import bot.AbstractBot
import graphics.renderers.AbstractRenderer
import level.Level
import physics.behaviours.AbstractPhysicBehaviour
import java.awt.Dimension

abstract class ItemBot(x: Double, y: Double, dimension: Dimension, physicBehaviour: AbstractPhysicBehaviour, renderer: AbstractRenderer) : AbstractBot(x, y, dimension, physicBehaviour, renderer){
    override fun act(level: Level, delta_t: Double) {
        if(this.physicBehaviour.margins.left == 0.0 || this.physicBehaviour.margins.right == 0.0) {
            physicBehaviour.speed.x *= -1
        }
    }

    abstract fun actOn(level: Level)
}