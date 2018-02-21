package bot.Items

import graphics.renderers.SpriteSheetRenderer
import level.Level
import physics.behaviours.GravityBehaviour
import java.awt.Dimension
import java.io.File
import javax.imageio.ImageIO

class MushroomItem :
        ItemBot(
            0.0, 0.0, Dimension(32, 32), GravityBehaviour(),
            SpriteSheetRenderer(ImageIO.read(File("assets/smb_items_sheet.png")), 184, 34, 15, 15)
        )
{
    override fun actOn(level: Level) {
        level.hero.life += 1
    }
}