package models

import bot.Items.ItemBot
import graphics.renderers.SpriteSheetRenderer
import level.Level
import java.io.File
import javax.imageio.ImageIO

class ItemBloc(x: Double, y: Double, width: Int, height: Int, val item: ItemBot) :
        Bloc(
                x, y, width, height,
                SpriteSheetRenderer(ImageIO.read(File("assets/blocks_sheet.png")), 384, 0, 15, 15)
        )
{

    var used = false

    fun use(level: Level) : ItemBot? {

        if(!used) {
            used = true
            (this.renderer as SpriteSheetRenderer).x = 432
            item.position = this.position.add(0.0, this.dimension.height + 2.0)
            return item
        }

        return null
    }

}