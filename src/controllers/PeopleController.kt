package controllers

import graphics.GraphicCore
import level.Level
import models.Bloc
import physics.PhysicCore

class PeopleController(level: Level, graphics: GraphicCore, physicCore: PhysicCore) : AbstractController(level, graphics, physicCore) {

    init {
        physicCore.listeners.add({
            val iterator = level.personnages.listIterator()
            while (iterator.hasNext()) {
                val p = iterator.next()

                if(level.objects.count { it is Bloc && it.isDeadly && p.touches(it) } > 0){
                    p.life = 0
                }

                if(p.life <= 0){
                    iterator.remove()
                    level.objects.remove(p)
                }
            }
        })
    }

}