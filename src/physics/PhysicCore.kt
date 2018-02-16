package physics

import bot.AbstractBot
import level.Level
import models.AbstractObject

interface Physicable {
    fun update(delta_t: Double, objects: MutableList<AbstractObject>)
    var position: Vector
}

class PhysicCore(val level: Level) {

    val framerate = 50
    val delta_t = 1000 / framerate // Le temps écoulé entre deux cycles (en ms)
    val listeners = mutableListOf<() -> Unit>()

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

                for (p in level.objects){
                    if(p is AbstractBot){
                        p.update(1.0 / framerate.toDouble(), level.objects, level)
                    } else {
                        p.update(1.0 / framerate.toDouble(), level.objects)
                    }
                }

                listeners.forEach { it() }

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