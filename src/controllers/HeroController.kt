package controllers

import bot.Ennemies.FlowerBot
import bot.Items.ItemBot
import graphics.GraphicCore
import level.Level
import models.ItemBloc
import models.People
import physics.PhysicCore
import physics.Vector
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
                    && py <= hy && hy <= py + ph
                    && (
                        (px <= hx && hx <= px + pw) ||
                        (px <= hx + hw && hx + hw <= px + pw) ||
                        (hx <= px && px + pw <= hx + hw)
                    )
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
                    && hy >= py + ph / 2
                    && (
                        inTriangle(
                            Vector(level.hero.position),
                            Vector(px, py + ph),
                            Vector(px + pw, py + ph),
                            Vector(px + pw / 2, py - ph / 2)
                        ) ||
                        inTriangle(
                            Vector(level.hero.position.add(hw.toDouble(), 0.0)),
                            Vector(px, py + ph),
                            Vector(px + pw, py + ph),
                            Vector(px + pw / 2, py - ph / 2)
                        )
                    ) && (targeting?.position?.y ?: py-1) < py
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

    private fun mixtProduct (p1: Vector, p2: Vector, p3: Vector) : Double
    {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y)
    }

    private fun inTriangle(p: Vector, t1: Vector, t2: Vector, t3: Vector) : Boolean {

        val b1 = mixtProduct(p, t1, t2) < 0.0f
        val b2 = mixtProduct(p, t2, t3) < 0.0f
        val b3 = mixtProduct(p, t3, t1) < 0.0f

        return ((b1 == b2) && (b2 == b3))

    }

}