package bot

import graphics.renderers.SpriteSheetRenderer
import level.Level
import models.Tube
import physics.behaviours.GhostBehaviour
import java.awt.Dimension
import java.io.File
import javax.imageio.ImageIO

class FlowerBot(val tube : Tube) :
        AbstractBot(
                tube.position.x + tube.dimension.width / 2 - 15, tube.position.y, Dimension(30, 44),
                GhostBehaviour(),
                SpriteSheetRenderer(ImageIO.read(File("assets/smb_enemies_sheet.png")), 420, 30, 15, 22)
        ){

    init {
        maxSpeed.y = 30.0
        maxSpeed.x = 0.0

        physicBehaviour.speed.y = maxSpeed.y
    }

    var waitingFor = 0.0

    override fun act(level: Level, delta_t: Double) {

        if(position.y < tube.position.y){
            physicBehaviour.speed.y = maxSpeed.y
        } else if(position.y > tube.position.y + tube.dimension.height){
            waitingFor += delta_t
            physicBehaviour.speed.y = 0.0

            if(waitingFor > 2.0) {
                physicBehaviour.speed.y = -maxSpeed.y
                waitingFor = .0
            }

        }

    }

}