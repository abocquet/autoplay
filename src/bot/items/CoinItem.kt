package bot.items

import graphics.renderers.SpriteSheetRenderer
import level.Level
import physics.behaviours.GravityBehaviour
import java.awt.Dimension
import java.io.File
import javax.imageio.ImageIO

class CoinItem :
        ItemBot(
                0.0, 0.0, Dimension(32, 32), GravityBehaviour(),
                SpriteSheetRenderer(ImageIO.read(File("assets/blocks_sheet.png")), 385, 16, 15, 15)
        )
{
    override fun actOn(level: Level) {}

    override fun act(level: Level, delta_t: Double) {
        if(physicBehaviour.margins.bottom == 0.0 && physicBehaviour.speed.y == 0.0){
            this.life = 0
        }
    }
}

