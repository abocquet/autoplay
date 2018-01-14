package bot

import level.Level
import graphics.renderers.AbstractRenderer
import physics.behaviours.AbstractPhysicBehaviour
import models.AbstractObject
import models.People
import java.awt.Dimension

abstract class AbstractBot(x: Double, y: Double, _dimension: Dimension, physicBehaviour: AbstractPhysicBehaviour, renderer: AbstractRenderer) : People(x, y, _dimension, physicBehaviour, renderer) {
    abstract fun act(level: Level)

    fun update(delta_t: Double, objects: MutableList<AbstractObject>, level: Level){
        physicBehaviour.update(this, delta_t, objects)
        act(level)
    }

}