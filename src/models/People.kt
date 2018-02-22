package models

import graphics.renderers.AbstractRenderer
import physics.Vector
import physics.behaviours.AbstractPhysicBehaviour
import java.awt.Dimension

open class People(x: Double, y: Double, dimension: Dimension, physicBehaviour: AbstractPhysicBehaviour, renderer: AbstractRenderer) : AbstractObject(physicBehaviour, renderer, dimension) {
    open var life = 1
    var maxSpeed = Vector(5.0, 5.0)
    init { position = Vector(x, y) }
}