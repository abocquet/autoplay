package controllers

import bot.FlowerBot
import graphics.GraphicCore
import level.Level
import models.People
import physics.PhysicCore
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class HeroController(val level: Level, graphics: GraphicCore, physicCore: PhysicCore) : AbstractControler(level, graphics, physicCore), KeyListener {

    var targeting: People? = null

    init {
        graphics.addKeyListener(this)
        level.hero.maxSpeed.x = 300.0
        level.hero.maxSpeed.y = 500.0

        physicCore.listeners.add({

            val hx = level.hero.position.x
            val hy = level.hero.position.y
            val hw = level.hero.dimension.width

            level.personnages.forEach {

                val px = it.position.x
                val py = it.position.y
                val pw = it.dimension.width
                val ph = it.dimension.height

                if (it != level.hero
                    && py <= hy && hy <= py + ph
                    && (
                        (px <= hx && hx <= px + pw) ||
                        (px <= hx + hw && hx + hw <= px + pw) ||
                        (hx <= px && px + pw <= hx + hw)
                    )
                ) {

                    if(it == targeting){
                        it.life = 0
                        level.hero.physicBehaviour.speed.y = level.hero.maxSpeed.y
                    } else {
                        level.hero.life -= 1
                        println(level.hero.life)
                    }

                }
            }

            level.personnages.forEach {

                val px = it.position.x
                val py = it.position.y
                val pw = it.dimension.width
                val ph = it.dimension.height

                if (it != level.hero
                    && it !is FlowerBot
                    && hy >= py + ph / 2
                    && (
                        (px <= hx && hx <= px + pw) ||
                        (px <= hx + hw && hx + hw <= px + pw) ||
                        (hx <= px && px + pw <= hx + hw)
                    )
                    && (targeting?.position?.y ?: py-1) < py
                ){
                    targeting = it
                } else if(targeting == it){
                    targeting = null
                }
            }

        })
    }

    override fun keyTyped(e: KeyEvent?) {
    }

    override fun keyPressed(e: KeyEvent?) {

        val pb = level.hero.physicBehaviour

        when (e?.keyChar) {
            'q' -> pb.speed.x = -level.hero.maxSpeed.x
            'd' -> pb.speed.x =  level.hero.maxSpeed.x
        }

        if(e?.keyCode == 32 && (
            pb.speed.y == 0.0 || pb.margins.bottom < 50
        )){
            pb.speed.y = level.hero.maxSpeed.y
        }
    }

    override fun keyReleased(e: KeyEvent?) {
        when (e?.keyChar) {
            'q' -> if(level.hero.physicBehaviour.speed.x == -level.hero.maxSpeed.x) level.hero.physicBehaviour.speed.x = 0.0
            'd' -> if(level.hero.physicBehaviour.speed.x == level.hero.maxSpeed.x) level.hero.physicBehaviour.speed.x = 0.0
        }
    }

}