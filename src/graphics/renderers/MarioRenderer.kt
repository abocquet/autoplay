package graphics.renderers

import graphics.DrawRequest
import models.AbstractObject
import models.People
import java.io.File
import javax.imageio.ImageIO

class MarioRenderer : SpriteSheetRenderer(ImageIO.read(File("assets/smb_mario_sheet.png")), arrayOf(180, 150, 120, 90), arrayOf(210, 240, 270, 300), 0, 15, 15){

    override fun draw(obj: AbstractObject, r: DrawRequest, offset: Int, delta_t: Double) {

        if(obj is People){
            this.y =  when(obj.life) {
                2 -> 52
                else -> 0
            }

            this.height = when(obj.life){
                2 -> 30
                else -> 15
            }
        }

        super.draw(obj, r, offset, delta_t)
    }

}