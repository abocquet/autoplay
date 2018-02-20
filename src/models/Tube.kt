package models

import graphics.renderers.SpriteSheetRenderer
import java.io.File
import javax.imageio.ImageIO

class Tube(x: Double, y: Double) :
    Bloc(
        x, y, 64, 64,
        SpriteSheetRenderer(
            ImageIO.read(File("assets/blocks_sheet.png")),
            0, 160, 32, 32
        )
    )