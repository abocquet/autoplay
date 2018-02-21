package bot.items

import graphics.renderers.SpriteSheetRenderer
import level.Level
import physics.behaviours.GravityBehaviour
import java.awt.Dimension
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class MushroomItem :
        ItemBot(
            0.0, 0.0, Dimension(32, 32), GravityBehaviour(),
            SpriteSheetRenderer(ImageIO.read(File("assets/smb_items_sheet.png")), 184, 34, 15, 15)
        )
{
    init {
        this.physicBehaviour.speed.x = 80.0 * if(Random().nextInt(2) > 0) 1 else -1
    }

    override fun actOn(level: Level) {
        level.hero.life += 1
        this.life = 0
    }

    var lastChanged = .0

    override fun act(level: Level, delta_t: Double) {
        if((physicBehaviour.margins.left == 0.0 || physicBehaviour.margins.right == 0.0) && lastChanged >= 1){
            physicBehaviour.speed.x *= -1
            lastChanged = .0
        }

        lastChanged += delta_t
    }
}

