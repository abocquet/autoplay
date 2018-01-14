package controllers

import graphics.GraphicCore
import level.Level
import physics.PhysicCore
import java.awt.event.KeyEvent
import java.awt.event.KeyListener



class HeroController(_level: Level, graphics: GraphicCore, physicCore: PhysicCore) : AbstractControler(_level, graphics, physicCore), KeyListener {

    val level = _level

    init {
        graphics.addKeyListener(this)
        level.hero.maxSpeed.x = 300.0
        level.hero.maxSpeed.y = 500.0
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
            pb.speed.y == 0.0 || pb.margins.bottom < 50 ||
            pb.margins.left == 0.0 || pb.margins.right == 0.0
        )){
            pb.speed.y = level.hero.maxSpeed.y

            if(pb.margins.right == 0.0){ pb.speed.x = -level.hero.maxSpeed.x / 2 }
            if(pb.margins.left  == 0.0){ pb.speed.x =  level.hero.maxSpeed.x / 2 }
        }
    }

    override fun keyReleased(e: KeyEvent?) {
        when (e?.keyChar) {
            'q' -> if(level.hero.physicBehaviour.speed.x == -level.hero.maxSpeed.x) level.hero.physicBehaviour.speed.x = 0.0
            'd' -> if(level.hero.physicBehaviour.speed.x == level.hero.maxSpeed.x) level.hero.physicBehaviour.speed.x = 0.0
        }
    }

}