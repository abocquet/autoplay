package graphics

import level.Level
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.lang.Integer.max
import javax.swing.JFrame
import javax.swing.JPanel

interface Drawable {
    fun render(r: DrawRequest, offset: Int, delta_t: Double)
}

class Panel(val drawers: MutableList<Drawable>, val level: Level, var delta_t: Double) : JPanel() {

    var scrolling = 0

    public override fun paintComponent(_g: Graphics) {
        val g = _g as Graphics2D

        // On dessine l'arrière plan
        g.color = Color(132, 186, 255)
        g.fillRect(0, 0, this.width, this.height)
        g.color = Color.BLACK

        val dr = DrawRequest(g, this.width, this.height)
        level.objects.toList().forEach { d -> d.render(dr, scrolling, delta_t) }
        drawers.toList().forEach { d -> d.render(dr, scrolling, delta_t)}

        //On libère les ressources système manuellement
        //http://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html#dispose()
        g.dispose()
    }

}


class GraphicCore(val level: Level, width: Int, height: Int): JFrame() {

    var FRAMERATE = 24

    val drawables = mutableListOf<Drawable>()
    private val pan = Panel(drawables, level, (1.0 / FRAMERATE))
    private var t: Thread? = null
    val listeners = mutableListOf<() -> Unit>()

    init {
        title = "AutoPlay"
        setSize(width, height)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setLocationRelativeTo(null)
        contentPane = pan
        isVisible = true
        isResizable = false
        requestFocus()
        focusTraversalKeysEnabled = false
    }

    fun start() {
        if (t == null) {
            t = Thread(Play())
            t?.start()
        }
    }

    fun stop() {
        if (t != null) {
            t?.interrupt()
            t = null
        }
    }

    internal inner class Play : Runnable {
        override fun run() {
            while (true) {

                val x = level.hero.position.x
                val scrolling = pan.scrolling

                if(x - scrolling <  200){
                    pan.scrolling = max((x - 200).toInt(), 0)
                }

                if(x > scrolling + 300){
                    pan.scrolling = (x - 300).toInt()
                }

                listeners.forEach { it() }

                pan.repaint()
                try {
                    Thread.sleep((1000 * pan.delta_t).toLong())
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    break
                }

            }
        }
    }

}

