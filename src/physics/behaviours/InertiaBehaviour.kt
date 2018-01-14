package physics.behaviours

import models.AbstractObject
import java.lang.Double.max
import java.lang.Double.min

open class InertiaBehaviour : AbstractPhysicBehaviour() {

    override fun update(obj: AbstractObject, delta_t: Double, obstacles: MutableList<AbstractObject>) {

        val deplacement = speed * delta_t
        this.calcMargin(obj, obstacles as MutableList<AbstractObject>, deplacement)

        deplacement.x = if (deplacement.x < 0) max(deplacement.x, -this.margins.left) else min(deplacement.x, this.margins.right)
        deplacement.y = if (deplacement.y < 0) max(deplacement.y, -this.margins.bottom) else min(deplacement.y, this.margins.top)

        obj.position += deplacement
    }

}
