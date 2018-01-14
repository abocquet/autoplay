package physics.behaviours

import models.AbstractObject

class NoneBehaviour : AbstractPhysicBehaviour() {
    override fun update(obj: AbstractObject, delta_t: Double, obstacles: MutableList<AbstractObject>) {}
}