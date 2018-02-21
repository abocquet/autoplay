package bot

import graphics.renderers.AbstractRenderer
import level.Level
import models.AbstractObject
import models.People
import physics.behaviours.AbstractPhysicBehaviour
import java.awt.Dimension

abstract class AbstractBot(x: Double, y: Double, dimension: Dimension, physicBehaviour: AbstractPhysicBehaviour, renderer: AbstractRenderer) : People(x, y, dimension, physicBehaviour, renderer) {
    abstract fun act(level: Level, delta_t: Double)

    open fun update(delta_t: Double, objects: List<AbstractObject>, level: Level){
        physicBehaviour.update(this, delta_t, objects)
        act(level, delta_t)
    }

}