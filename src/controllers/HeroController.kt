package controllers

import bot.ennemies.FlowerBot
import bot.items.ItemBot
import graphics.GraphicCore
import level.Level
import models.ItemBloc
import models.Mario
import models.People
import physics.PhysicCore
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import kotlin.math.abs

class HeroController(val level: Level, graphics: GraphicCore, physicCore: PhysicCore) : AbstractControler(level, graphics, physicCore), KeyListener {

    private var targeting: People? = null

    init {
        val mario = level.hero as Mario
        graphics.addKeyListener(this)
        mario.maxSpeed.x = 300.0
        mario.maxSpeed.y = 600.0

        physicCore.listeners.add({ delta_t: Double ->

            val hx = mario.position.x
            val hy = mario.position.y
            val hw = mario.dimension.width
            val hh = mario.dimension.height

            mario.hurtedClock += delta_t

            // Gestion des collisions
            level.personnages.forEach {

                val px = it.position.x
                val py = it.position.y
                val pw = it.dimension.width
                val ph = it.dimension.height

                if (it != mario
                    && hy in py-1..py+ph
                    && (px <= hx + hw && hx <= px+pw)
                    && it.life > 0
                ) {

                    if(it is ItemBot){
                        it.actOn(level)
                    }
                    else {
                        if (
                            hy - py - abs(hx - px - pw / 2)>= 0 || hy - py - abs(hx + hw - px - pw / 2)>= 0
                            && it !is FlowerBot
                        ) {
                            it.life = 0
                            mario.physicBehaviour.speed.y = mario.maxSpeed.y
                        } else if(!mario.isHurted){
                            mario.life -= 1
                        }
                    }

                }
            }

            // On ajoute les items

            val elementsToAdd = mutableListOf<ItemBot?>()

            level.objects.forEach {

                val bx = it.position.x
                val by = it.position.y
                val bw = it.dimension.width

                if(it is ItemBloc){
                    if(hy + hh == by && hx <= bx + bw && hx + hw >= bx) {
                        elementsToAdd.add(it.use(level))
                    }
                }
            }

            elementsToAdd.filterNotNull().forEach { item ->
                level.objects.add(item)
                level.personnages.add(item)
            }

        })
    }

    override fun keyTyped(e: KeyEvent?) {}

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