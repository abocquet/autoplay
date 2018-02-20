package graphics.renderers

import graphics.DrawRequest
import models.AbstractObject
import java.awt.image.BufferedImage
import java.lang.Integer.min

open class BlocRenderer(spritesheet: BufferedImage, val x: Int, val y: Int, val side: Int) : AbstractRenderer(){

    val subimage = spritesheet.getSubimage(x, y, side, side)

    override fun draw(obj: AbstractObject, r: DrawRequest, offset: Int) {
        for(k in 0 until obj.dimension.width / side) {
            val w = min(side, obj.dimension.width - k * side)

            if(w <= 0){
                continue
            }

            r.image(
                subimage,
                obj.position.x.toInt() - offset + k * side, obj.position.y.toInt(),
                w, obj.dimension.height
            )
        }

        r.rect(obj.position.x.toInt() - offset, obj.position.y.toInt(), obj.dimension.width, obj.dimension.height)
    }


}