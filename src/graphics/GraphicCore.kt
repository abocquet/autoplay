package graphics

import level.Level
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JFrame
import javax.swing.JPanel

interface Drawable {
    fun render(r: DrawRequest, offset: Int)
}

class Panel(_drawers: MutableList<Drawable>) : JPanel() {

    private val drawers = _drawers
    var scrolling = 0

    public override fun paintComponent(_g: Graphics) {
        val g = _g as Graphics2D

        // On dessine l'arrière plan
        g.color = Color.white
        g.fillRect(0, 0, this.width, this.height)
        g.color = Color.BLACK

        val dr = DrawRequest(g, this.width, this.height)
        this.drawers.forEach { d -> d.render(dr, scrolling)}

        //On libère les ressources système manuellement
        //http://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html#dispose()
        g.dispose()
    }

}


class GraphicCore(val level: Level): JFrame() {

    var FRAMERATE = 60

    val drawables = mutableListOf<Drawable>()
    private val pan = Panel(drawables)
    private var t: Thread? = null

    init {
        title = "AutoPlay"
        setSize(1080, 720)
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

                if(x - scrolling < 200){
                    pan.scrolling = (x - 200).toInt()
                }

                if(x > scrolling + 500){
                    pan.scrolling = (x - 500).toInt()
                }

                pan.repaint()
                try {
                    Thread.sleep((1000 / FRAMERATE).toLong())
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    break
                }

            }
        }
    }

}