package controllers

import graphics.GraphicCore
import level.Level
import physics.PhysicCore

class PeopleController(level: Level, graphics: GraphicCore, physicCore: PhysicCore) : AbstractControler(level, graphics, physicCore) {

    init {

        physicCore.listeners.add({
            val iterator = level.personnages.listIterator()
            while (iterator.hasNext()) {
                val p = iterator.next()
                if(p.life <= 0){
                    iterator.remove()
                    level.objects.remove(p)
                }
            }
        })
    }

}