package graphics.renderers

import graphics.DrawRequest
import models.AbstractObject
import models.ItemBloc
import java.io.File
import javax.imageio.ImageIO

open class ItemBlocRenderer : AbstractRenderer(){

    val spritesheet = ImageIO.read(File("assets/blocks_sheet.png"))
    val seq = arrayOf(384, 432)
    val y = 0
    val width = 16
    val height = 16

    override fun draw(obj: AbstractObject, r: DrawRequest, offset: Int, delta_t: Double) {

        if(obj is ItemBloc) {
            val x = if(!obj.used) seq[0] else seq[1]

            r.image(
                    spritesheet.getSubimage(x, y, width, height),
                    obj.position.x.toInt() - offset, obj.position.y.toInt(),
                    obj.dimension.width, obj.dimension.height
            )

        }
    }

}