package graphics.renderers

import graphics.DrawRequest
import models.AbstractObject
import java.awt.image.BufferedImage

open class SpriteSheetRenderer(val spritesheet: BufferedImage, var seqLeft: Array<Int>, var seqRight : Array<Int>, var y: Int, var width: Int, var height: Int) : AbstractRenderer(){

    var x = seqLeft[0]

    override fun draw(obj: AbstractObject, r: DrawRequest, offset: Int) {
        r.image(
            spritesheet.getSubimage(x, y, width, height),
            obj.position.x.toInt() - offset, obj.position.y.toInt(),
            obj.dimension.width, obj.dimension.height
        )

        r.rect(obj.position.x.toInt() - offset, obj.position.y.toInt(), obj.dimension.width, obj.dimension.height)
    }

    constructor(spritesheet: BufferedImage, x: Int, y: Int, width: Int, height: Int) : this(spritesheet, arrayOf(x), arrayOf(x), y, width, height)

}