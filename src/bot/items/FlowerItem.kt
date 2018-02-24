package bot.items

import graphics.renderers.SpriteSheetRenderer
import level.Level
import physics.behaviours.GravityBehaviour
import java.awt.Dimension
import java.io.File
import javax.imageio.ImageIO

class FlowerItem :
        ItemBot(
                0.0, 0.0, Dimension(32, 32), GravityBehaviour(),
                SpriteSheetRenderer(ImageIO.read(File("assets/smb_items_sheet.png")), 34, 64, 15, 15)
        )
{
    override fun actOn(level: Level) {
        level.hero.life += 1
        this.life = 0
    }

    override fun act(level: Level, delta_t: Double) {}
}

