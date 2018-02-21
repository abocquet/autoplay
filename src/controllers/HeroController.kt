package controllers

import bot.ennemies.FlowerBot
import bot.items.ItemBot
import graphics.GraphicCore
import level.Level
import models.ItemBloc
import models.People
import physics.PhysicCore
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class HeroController(val level: Level, graphics: GraphicCore, physicCore: PhysicCore) : AbstractControler(level, graphics, physicCore), KeyListener {

    private var targeting: People? = null

    init {
        graphics.addKeyListener(this)
        level.hero.maxSpeed.x = 300.0
        level.hero.maxSpeed.y = 500.0

        physicCore.listeners.add({

            val hx = level.hero.position.x
            val hy = level.hero.position.y
            val hw = level.hero.dimension.width
            val hh = level.hero.dimension.height

            // Gestion des collisions
            level.personnages.forEach {

                val px = it.position.x
                val py = it.position.y
                val pw = it.dimension.width
                val ph = it.dimension.height

                if (it != level.hero
                    && py <= hy + hh && hy <= py + ph
                    && (px <= hx + hw && hx <= px+pw)
                ) {

                    if(it is ItemBot){
                        it.actOn(level)
                    }
                    else {
                        if (it == targeting) {
                            it.life = 0
                            level.hero.physicBehaviour.speed.y = level.hero.maxSpeed.y
                        } else {
                            level.hero.life -= 1
                            println(level.hero.life)
                        }
                    }

                }
            }

            // Anticipation du personnage sur qui on va sauter
            level.personnages.forEach {

                val px = it.position.x
                val py = it.position.y
                val pw = it.dimension.width
                val ph = it.dimension.height

                if (it != level.hero
                    && it !is FlowerBot
                    && hy >= py + ph - 3
                    && (px <= hx + hw && hx <= px + pw)
                    && (targeting?.position?.y ?: py-1) < py
                ){
                    targeting = it
                } else if(targeting == it){
                    targeting = null
                }
            }

            val elementsToAdd = mutableListOf<ItemBot?>()
            // On ajoute les items
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