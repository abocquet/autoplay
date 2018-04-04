package controllers

import bot.AbstractBot
import graphics.GraphicCore
import level.Level
import physics.PhysicCore

class BotController(level: Level, graphics: GraphicCore, physicCore: PhysicCore) : AbstractController(level, graphics, physicCore) {

    init {
        physicCore.listeners.add({ delta_t ->
            val objects = level.objects.toList()

            for (p in level.objects) {
                if (p is AbstractBot) {
                    p.update(delta_t, objects, level)
                } else {
                    p.update(delta_t, objects)
                }
            }
        })
    }

}