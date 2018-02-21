package bot.ennemies

import bot.AbstractBot
import graphics.renderers.SpriteSheetRenderer
import graphics.renderers.TurtleRenderer
import level.Level
import physics.behaviours.GravityBehaviour
import physics.behaviours.InertiaBehaviour
import java.awt.Dimension
import java.io.File
import javax.imageio.ImageIO

class TurtleBot(val x: Double, val y: Double, private val space: Double) : AbstractBot(x, y, Dimension(15, 25) * 2, InertiaBehaviour(), TurtleRenderer()){

    init {
        maxSpeed.x = 50.0
    }

    override fun act(level: Level, delta_t: Double) {
        if(position.x <= this.x){
            this.physicBehaviour.speed.x = this.maxSpeed.x
        }

        if(position.x >= this.x + this.space){
            this.physicBehaviour.speed.x = -this.maxSpeed.x
        }
    }

}

private operator fun Dimension.times(i: Int): Dimension { return Dimension(this.width * i, this.height * i)}


class MushroomBot(val x: Double, val y: Double) :
        AbstractBot(
                x, y, Dimension(32, 32), GravityBehaviour(),
                SpriteSheetRenderer(ImageIO.read(File("assets/smb_items_sheet.png")), 184, 34, 15, 15)
        )
{
    init {
        maxSpeed.x = 50.0
    }

    override fun act(level: Level, delta_t: Double) {
        physicBehaviour.speed.x = 0.0
    }
}