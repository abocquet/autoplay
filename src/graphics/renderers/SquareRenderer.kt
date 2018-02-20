package graphics.renderers

import graphics.DrawRequest
import models.AbstractObject


open class SquareRenderer : AbstractRenderer() {

    override fun draw(obj: AbstractObject, r: DrawRequest, offset: Int) {
        r.rect(obj.position.x.toInt() - offset, obj.position.y.toInt(), obj.dimension.width, obj.dimension.height)
    }

}
