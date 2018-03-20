package graphics

import level.Level
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.concurrent.thread

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
        drawers.toList().forEach { d -> d.render(dr, scrolling, delta_t) }

        //On libère les ressources système manuellement
        //http://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html#dispose()
        g.dispose()
    }

}


class GraphicCore(val level: Level, width: Int, height: Int) : JFrame() {

    var FRAMERATE = 24

    val drawables = mutableListOf<Drawable>()
    private val pan = Panel(drawables, level, (1.0 / FRAMERATE))
    private var t: Thread? = null
    val listeners = mutableListOf<() -> Unit>()
    var shouldRun = false

    init {
        title = "AutoPlay"
        setSize(width, height)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setLocationRelativeTo(null)
        contentPane = pan
        isResizable = false
        requestFocus()
        focusTraversalKeysEnabled = false

        toFront()

        isVisible = true

    }

    fun start() {
        if (shouldRun) {
            return
        }

        shouldRun = true
        thread {
            while (shouldRun) {

                val x = level.hero.position.x
                val scrolling = pan.scrolling

                if (x - scrolling < 200) {
                    pan.scrolling = Integer.max((x - 200).toInt(), 0)
                }

                if (x > scrolling + 300) {
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

    fun stop() {
        shouldRun = false
    }
}

