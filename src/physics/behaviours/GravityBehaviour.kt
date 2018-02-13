package physics.behaviours

import models.AbstractObject
import java.lang.Double.max

class GravityBehaviour: InertiaBehaviour() {

    private val gravity = 981.0

    override fun update(obj: AbstractObject, delta_t: Double, obstacles: MutableList<AbstractObject>) {

        if (this.margins.bottom > 0) {
            acceleration.y = -gravity
            speed += acceleration * delta_t
        } else {
            speed.y = max(speed.y, 0.0)
        }

        if(this.margins.top == 0.0 && speed.y > 0) {
            speed.y = 0.0
        }

        super.update(obj, delta_t, obstacles)
    }

}