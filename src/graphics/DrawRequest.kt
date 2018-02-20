package graphics

import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage


class DrawRequest(private val g: Graphics2D, private val width: Int, private val height: Int) {


    init {
        this.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    }

    var color : Color
        get() { return g.color }
        set(value) { g.color = value}

    fun rect(x: Int, y: Int, width: Int, height: Int, border: Float = 1.toFloat(), c: Color = Color.BLACK) {
        this.g.color = c
        this.g.stroke = BasicStroke(border)
        this.g.drawRect(x, this.height - y - height, width, height)
        this.g.color = Color.BLACK
    }

    fun rect(_x: Int, _y: Int, width: Int, height: Int, angle: Int) {

        // on ajoute un décalage sur x et y en fonction de l'angle de rotation
        // afin d'obtenir une rotation par raport au coins inférieur gauche
        val y = (this.height - _y - height * Math.cos(Math.toRadians(angle.toDouble()))).toInt()
        val x = (_x - height * Math.sin(Math.toRadians(angle.toDouble()))).toInt()

        this.g.translate(x, y)
        this.g.rotate(Math.toRadians((-angle).toDouble()))
        this.g.drawRect(0, 0, width, height)
        this.g.rotate(Math.toRadians(angle.toDouble()))
        this.g.translate(-x, -y)
    }

    fun image(img: BufferedImage, _x: Int, _y: Int, width: Int, height: Int, angle: Int = 0) {

        // on ajoute un décalage sur x et y en fonction de l'angle de rotation
        // afin d'obtenir une rotation par raport au coins inférieur gauche
        val y = this.height - _y - height * Math.cos(Math.toRadians(angle.toDouble()))
        val x = _x - height * Math.sin(Math.toRadians(angle.toDouble()))

        val trans = AffineTransform()

        trans.translate((x - if (width < 0) width else 0), y)
        trans.rotate(Math.toRadians((-angle).toDouble()))
        trans.scale(width.toDouble() / img.width, height.toDouble() / img.height)
        this.g.drawImage(img, trans, null)
    }

    fun image(img: BufferedImage, _x: Int, _y: Int, angle: Int) {

        // on ajoute un décalage sur x et y en fonction de l'angle de rotation
        // afin d'obtenir une rotation par raport au coins inférieur gauche
        val y = this.height - _y - img.height * Math.cos(Math.toRadians(angle.toDouble()))
        val x = _x - img.height * Math.sin(Math.toRadians(angle.toDouble()))

        val trans = AffineTransform()
        trans.translate(x.toDouble(), y.toDouble())
        trans.rotate(Math.toRadians((-angle).toDouble()))
        this.g.drawImage(img, trans, null)
    }

    fun image(img: BufferedImage, _x: Int, _y:Int){
        this.g.drawImage(img, null, _x, this.height - _y - img.height)
    }

    fun fillRect(x: Int, y: Int, width: Int, height: Int, c: Color) {
        this.g.color = c
        this.g.fillRect(x, this.height - y - height, width, height)
        this.g.color = Color.BLACK
    }

    fun drawString(s: String, _x: Int, y: Int, c: Color = Color.BLACK, fontSize: Int = 10, centered: Boolean = false) {

        var x = _x

        this.g.font = Font("Arial", Font.PLAIN, fontSize)

        if (centered) {
            val fm = this.g.fontMetrics
            val r = fm.getStringBounds(s, this.g)
            x = (this.width - r.width).toInt() / 2
        }

        this.g.color = c
        this.g.drawString(s, x, this.height - y)
        this.g.color = Color.BLACK
    }

    fun line(x1: Double, y1: Double, x2: Double, y2: Double) {
        this.g.drawLine(x1.toInt(), this.height - y1.toInt(), x2.toInt(), this.height - y2.toInt())
    }

}
