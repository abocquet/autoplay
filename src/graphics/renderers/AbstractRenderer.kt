package graphics.renderers

import graphics.DrawRequest
import models.AbstractObject

abstract class AbstractRenderer {
    abstract fun draw(obj: AbstractObject, r: DrawRequest, offset: Int)
}