package graphics.renderers

import graphics.DrawRequest
import models.AbstractObject
import java.awt.image.BufferedImage

open class SpriteSheetRenderer(val spritesheet: BufferedImage, var seqLeft: Array<Int>, var seqRight : Array<Int>, var y: Int, var width: Int, var height: Int) : AbstractRenderer(){

    var x = seqLeft[0]
    var seq = seqLeft

    val animationStep = .1
    var lastChanged = 0.0
    var currentFrame = 0

    override fun draw(obj: AbstractObject, r: DrawRequest, offset: Int, delta_t: Double) {

        lastChanged += delta_t

        if (obj.physicBehaviour.speed.x == 0.0){
            currentFrame = 0
        } else if(lastChanged >= animationStep){
            currentFrame = if(seq.size == 1) 0 else ((currentFrame + 1) % (seqLeft.size - 1)) + 1
            lastChanged = 0.0
        }

        if(obj.renderer is SpriteSheetRenderer) {
            if (obj.physicBehaviour.speed.x > 0) {
                seq = seqRight
            } else if (obj.physicBehaviour.speed.x < 0) {
                seq = seqLeft
            }
        }

        x = seq[currentFrame]
        r.image(
            spritesheet.getSubimage(x, y, width, height),
            obj.position.x.toInt() - offset, obj.position.y.toInt(),
            obj.dimension.width, obj.dimension.height
        )

        r.rect(obj.position.x.toInt() - offset, obj.position.y.toInt(), obj.dimension.width, obj.dimension.height)
    }

    constructor(spritesheet: BufferedImage, x: Int, y: Int, width: Int, height: Int) : this(spritesheet, arrayOf(x), arrayOf(x), y, width, height)

}