package models

import graphics.renderers.AbstractRenderer
import physics.Vector
import physics.behaviours.AbstractPhysicBehaviour
import java.awt.Dimension

open class People(x: Double, y: Double, _dimension: Dimension, physicBehaviour: AbstractPhysicBehaviour, renderer: AbstractRenderer) : AbstractObject(physicBehaviour, renderer) {

    val maxLife = 2
    var life = maxLife
        get
    var isHurted = false

    var maxSpeed = Vector(5.0, 5.0)

    init {
        dimension.size = _dimension
        position = Vector(x, y)
    }

    /**
     * Retranche à la vie du personnage les dégats infligés
     *
     * @param degats Les degats infligés
     * @param recul La distance dont le personnage recul suite à l'impact
     */
    fun takeDamages(degats: Int, recul: Int = 0) {
        this.life -= degats

        if (recul != 0) {
            this.isHurted = true
        }
    }

}