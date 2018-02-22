package physics

import bot.AbstractBot
import level.Level
import models.AbstractObject

interface Physicable {
    fun update(delta_t: Double, objects: List<AbstractObject>)
    var position: Vector
}

class PhysicCore(val level: Level) {

    val framerate = 50
    val delta_t = 1000 / framerate // Le temps écoulé entre deux cycles (en ms)
    val listeners = mutableListOf<(Double) -> Unit>()

    private var t: Thread? = null

    fun start() {
        if (t == null) {
            t = Thread(Play())
            t!!.start()
        }
    }

    fun stop() {
        if (t != null) {
            t!!.interrupt()
            t = null
        }
    }

    internal inner class Play : Runnable {
        override fun run() {
            while (true) {

                val objects = level.objects.toList()

                for (p in level.objects){
                    if(p is AbstractBot){
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

}