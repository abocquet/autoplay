package physics.behaviours

import models.AbstractObject

open class GhostBehaviour : AbstractPhysicBehaviour() {

    override fun update(obj: AbstractObject, delta_t: Double, obstacles: MutableList<AbstractObject>) {

        val deplacement = speed * delta_t
        obj.position += deplacement

    }

}
