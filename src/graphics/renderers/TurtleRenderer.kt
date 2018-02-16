package graphics.renderers

import java.io.File
import javax.imageio.ImageIO

class TurtleRenderer : SpriteSheetRenderer(
        ImageIO.read(File("assets/smb_enemies_sheet.png")),
        arrayOf(150), arrayOf(210),
        0, 15, 25
)
