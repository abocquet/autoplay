package models

import graphics.renderers.BlocRenderer
import physics.behaviours.NoneBehaviour

class Bloc(x: Double, y: Double, width: Int, height: Int): AbstractObject(NoneBehaviour(), BlocRenderer()){

    init {
        position.x = x
        position.y = y

        dimension.width = width
        dimension.height = height
    }

}

