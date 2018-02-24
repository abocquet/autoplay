package graphics.renderers

import graphics.DrawRequest
import models.AbstractObject
import models.Mario
import java.io.File
import javax.imageio.ImageIO

class MarioRenderer : SpriteSheetRenderer(ImageIO.read(File("assets/smb_mario_sheet.png")), arrayOf(180, 150, 120, 90), arrayOf(210, 240, 270, 300), 0, 15, 15){

    var animationTicker = 0.0

    override fun draw(p: AbstractObject, r: DrawRequest, offset: Int, delta_t: Double) {

        if(p is Mario){
            this.y =  when(p.life) {
                2 -> 52
                3 -> 122
                else -> 0
            }

            this.height = when(p.life){
                in 2..3 -> 30
                else -> 15
            }

            if(p.isHurted){
                animationTicker += delta_t

                if(animationTicker % 0.2 >= .1){
                     // We stop so we have a blinking effect
                    return
                }
            } else {
                animationTicker = 0.0
            }
        }

        super.draw(p, r, offset, delta_t)
    }
}