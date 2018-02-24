package models

import graphics.renderers.AbstractRenderer
import physics.behaviours.NoneBehaviour

open class Bloc(x: Double, y: Double, width: Int, height: Int, renderer : AbstractRenderer, var isDeadly : Boolean = false, var isEnd : Boolean = false): AbstractObject(NoneBehaviour(), renderer){

    init {
        position.x = x
        position.y = y

        dimension.width = width
        dimension.height = height
    }

}

