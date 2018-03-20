package GUI.Panels

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.util.*
import javax.swing.JPanel
import kotlin.math.roundToInt

class PlotPanel : JPanel() {

    init {
        preferredSize = Dimension(500, 300)
    }

    val points = mutableListOf<Plot>()

    val marginTop    = 20
    val marginLeft   = 50
    val marginBottom = 50
    val marginRight  = 50


    fun plot(X: List<Double>, Y: List<Double>, label: String = "", color : Color? = null, dotSize : Int = 1, linked : Boolean = false){
        if(X.count() != Y.count()){
            throw Exception("X and Y must be the same size (${X.count()}, ${Y.count()} given)")
        }

        val r = Random()
        points.add(Plot(X.toMutableList(), Y.toMutableList(), label, color ?: Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)), dotSize, linked))
    }

    operator fun get(label: String) : Plot? {
        return points.firstOrNull { it.label == label }
    }

    @Synchronized
    override fun paint(g: Graphics) {

        try {
            // On dessine les axes

            g.drawLine(marginLeft, marginTop, marginLeft, height - marginBottom) // Ligne de droite
            g.drawLine(marginLeft, height - marginBottom, width - marginRight, height - marginBottom) // Ligne du bas

            // On met les valeurs min et max

            val plots = points.filter { it.X.count() > 0 }

            if (plots.count() == 0) {
                return
            }

            val minX = plots.map({ it.X.min()!! }).min()!!
            val maxX = plots.map({ it.X.max()!! }).max()!!

            val minY = plots.map({ it.Y.min()!! }).min()!!
            val maxY = plots.map({ it.Y.max()!! }).max()!!

            g.drawString(minX.toString(), marginLeft, (height - marginBottom / 2.0).toInt())
            g.drawString(maxX.toString(), width - marginRight, (height - marginBottom / 2.0).toInt())

            g.drawString(((minY * 100).roundToInt() / 100.0).toString(), 5, height - marginBottom)
            g.drawString(((maxY * 100).roundToInt() / 100.0).toString(), 5, marginTop)

            // On affiche la légende

            plots.forEachIndexed { index, plot ->
                g.color = plot.color
                g.drawLine(
                        marginLeft * 2 * (1 + index), (height - marginBottom / 2.0 - 5).toInt(),
                        marginLeft * 2 * (1 + index) + 10, (height - marginBottom / 2.0 - 5).toInt()
                )
                g.color = Color.BLACK
                g.drawString(plot.label, marginLeft * 2 * (1 + index) + 15, (height - marginBottom / 2.0).toInt())
            }

            // Puis les plots

            plots.filter { it.X.count() > 0 }.forEach { plot ->
                val coords = plot.X.zip(plot.Y)

                coords.forEach {
                    val x = it.first
                    val y = it.second

                    g.color = plot.color

                    g.fillOval(
                            ((x - minX) / (maxX - minX) * (width - marginLeft - marginRight) + marginLeft).toInt(),
                            height - ((y - minY) / (maxY - minY) * (height - marginLeft - marginRight) + marginLeft).toInt(),
                            plot.dotSize, plot.dotSize
                    )
                }

                if (!plot.linked) {
                    return
                }

                coords.zipWithNext().forEach {
                    val x0 = it.first.first
                    val y0 = it.first.second

                    val x1 = it.second.first
                    val y1 = it.second.second

                    g.color = plot.color

                    g.drawLine(
                            ((x0 - minX) / (maxX - minX) * (width - marginLeft - marginRight) + marginLeft).toInt(),
                            height - ((y0 - minY) / (maxY - minY) * (height - marginLeft - marginRight) + marginLeft).toInt(),
                            ((x1 - minX) / (maxX - minX) * (width - marginLeft - marginRight) + marginLeft).toInt(),
                            height - ((y1 - minY) / (maxY - minY) * (height - marginLeft - marginRight) + marginLeft).toInt()
                    )
                }
            }
        }
        catch (e: ConcurrentModificationException){}
        catch (e: NullPointerException) {}
    }

    fun clear(){
        points.forEach { it.clear() }
        repaint()
    }

}

data class Plot(val X: MutableList<Double>, val Y: MutableList<Double>, val label: String, val color: Color, val dotSize: Int, val linked: Boolean, val limit : Int = 1000){

    @Synchronized
    fun add(x: Double, y: Double){
        X.add(x)
        Y.add(y)

        while(X.max()!! - X.min()!! > limit){
            X.removeAt(0)
            Y.removeAt(0)
        }
    }

    fun clear() {
        X.clear()
        Y.clear()
    }

}