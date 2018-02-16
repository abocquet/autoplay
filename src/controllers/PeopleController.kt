package controllers

import graphics.GraphicCore
import graphics.renderers.SpriteSheetRenderer
import level.Level
import physics.PhysicCore

class PeopleController(level: Level, graphics: GraphicCore, physicCore: PhysicCore) : AbstractControler(level, graphics, physicCore) {

    init {
        graphics.listeners.add({
            level.personnages.forEach {
                if(it.renderer is SpriteSheetRenderer) {
                    if (it.physicBehaviour.speed.x > 0) {
                        it.renderer.x = it.renderer.seqRight[0]
                    } else if (it.physicBehaviour.speed.x < 0) {
                        it.renderer.x = it.renderer.seqLeft[0]
                    }
                }
            }
        })

        physicCore.listeners.add({

            val iterator = level.personnages.listIterator()
            while (iterator.hasNext()) {
                val p = iterator.next()
                if(p.life == 0){
                    iterator.remove()
                    level.objects.remove(p)
                }
            }

        })
    }

}