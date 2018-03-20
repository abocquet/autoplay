package physics

import bot.AbstractBot
import level.Level
import models.AbstractObject
import kotlin.concurrent.thread

interface Physicable {
    fun update(delta_t: Double, objects: List<AbstractObject>)
    var position: Vector
}

class PhysicCore(val level: Level) {

    val framerate = 50
    val delta_t = 1000 / framerate // Le temps écoulé entre deux cycles (en ms)
    val listeners = mutableListOf<(Double) -> Unit>()


    private var shouldRun = false

    fun start() {
        if (shouldRun) {
            return
        }

        shouldRun = true
        thread {
            while (shouldRun) {

                val objects = level.objects.toList()

                for (p in level.objects) {
                    if (p is AbstractBot) {
                        p.update(1.0 / framerate.toDouble(), objects, level)
                    } else {
                        p.update(1.0 / framerate.toDouble(), objects)
                    }
                }

                listeners.forEach { it(1.0 / framerate.toDouble()) }

                try {
                    Thread.sleep(delta_t.toLong())
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    break
                }

            }
        }
    }

    fun stop() {
        shouldRun = false
    }

}