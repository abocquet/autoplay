package GUI.Panels

import java.awt.Graphics
import javax.swing.JPanel

class StatPanel : JPanel() {

    override fun paint(g: Graphics) {
        g.drawRect(20, 20, 20, 20)
    }

}