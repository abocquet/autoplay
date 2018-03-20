package GUI

import GUI.Panels.GenomePanel
import GUI.Panels.PlotPanel
import neat.Config
import neat.Population
import neat.stucture.Connection
import neat.stucture.Genome
import neat.stucture.Node
import neat.stucture.NodeType
import java.awt.Color
import java.lang.Double.max
import java.util.*
import javax.swing.JFrame
import javax.swing.JPanel

fun main(args: Array<String>) {

    genomeTest()

}

class SimpleFrame(val panel: JPanel) : JFrame() {

    init {
        isVisible = true
        setLocationRelativeTo(null)
        size = panel.preferredSize
        contentPane = panel
    }

}

fun genomeTest() {

    val population = Population(3, 1, { max(1.0, 3 + Random().nextGaussian()) })
    population.evolve(10)
    val genome = population.members[3]

    val panel = GenomePanel(sampleGenome2())
    val app = SimpleFrame(panel)
}

fun sampleGenome(): Genome {
    val genome = Genome(3, 2, Config())

    val inputs = listOf(
            Node(-3, 1.0, NodeType.INPUT),
            Node(-2, 1.0, NodeType.INPUT),
            Node(-1, 1.0, NodeType.INPUT)
    )

    val p1h = listOf(
            Node(1, 1.0)
    )

    val p2h = listOf(
            Node(1, 1.0),
            Node(2, 1.0)
    )

    val outputs = listOf(
            Node(0, 1.0, NodeType.OUTPUT)
    )

    val c1 = listOf(
            Connection(1, 1.0, -1, 0, true),
            Connection(2, 1.0, -2, 0, false),
            Connection(3, 1.0, -3, 0, true),
            Connection(4, 1.0, -2, 1, true),
            Connection(5, 1.0, 1, 0, true),
            Connection(8, 1.0, -1, 1, true)
    )

    val c2 = listOf(
            Connection(1, 1.0, -1, 0, true),
            Connection(2, 1.0, -2, 0, false),
            Connection(3, 1.0, -3, 0, true),
            Connection(4, 1.0, -2, 1, true),
            Connection(5, 1.0, 1, 0, false),
            Connection(6, 1.0, -1, 2, true),
            Connection(7, 1.0, -2, 0, true),
            Connection(9, 1.0, -3, 1, true),
            Connection(10, 1.0, -1, 2, true)
    )

    val p1 = Genome(inputs, p1h, outputs, c1)
    val p2 = Genome(inputs, p2h, outputs, c2)

    return p1
}

fun sampleGenome2(): Genome {

    val connections = listOf(
            Connection(id = -1, weight = 1.0, from = -1, to = 0, enabled = true),
            Connection(id = 0, weight = -1.0, from = -2, to = 0, enabled = true),
            Connection(id = 1, weight = 1.0, from = -3, to = 0, enabled = true),
            Connection(id = 1, weight = 1.0, from = 1, to = 0, enabled = true),
            Connection(id = 2, weight = 1.0, from = -2, to = 1, enabled = true),
            Connection(id = 7, weight = 1.0, from = 4, to = 0, enabled = true),
            Connection(id = 8, weight = 1.0, from = -3, to = 4, enabled = true),
            Connection(id = 15, weight = -1.0, from = 8, to = 0, enabled = true),
            Connection(id = 16, weight = 1.0, from = -3, to = 8, enabled = true),
            Connection(id = 21, weight = -1.0, from = -3, to = 1, enabled = true),
            Connection(id = 22, weight = 1.0, from = 11, to = 0, enabled = true),
            Connection(id = 23, weight = 1.0, from = 0, to = 11, enabled = true),
            Connection(id = 30, weight = 1.0, from = 15, to = 11, enabled = true),
            Connection(id = 31, weight = 2.0, from = 0, to = 15, enabled = true),
            Connection(id = 54, weight = 1.0, from = 23, to = 1, enabled = true),
            Connection(id = 55, weight = 1.0, from = 0, to = 23, enabled = true),
            Connection(id = 58, weight = -1.0, from = 15, to = 4, enabled = true),
            Connection(id = 84, weight = 1.0, from = 11, to = 4, enabled = true)
    )

    val nodes = listOf(
            Node(id = -3, type = NodeType.INPUT, timeConstant = 1.0),
            Node(id = -2, type = NodeType.INPUT, timeConstant = 1.0),
            Node(id = -1, type = NodeType.INPUT, timeConstant = 1.0),
            Node(id = 0, type = NodeType.OUTPUT, timeConstant = 1.0),
            Node(id = 1, type = NodeType.HIDDEN, timeConstant = 1.0),
            Node(id = 4, type = NodeType.HIDDEN, timeConstant = 1.0),
            Node(id = 8, type = NodeType.HIDDEN, timeConstant = 1.0),
            Node(id = 11, type = NodeType.HIDDEN, timeConstant = 1.0),
            Node(id = 15, type = NodeType.HIDDEN, timeConstant = 1.0)
    )

    return Genome(nodes.filter { it.type == NodeType.INPUT }, nodes.filter { it.type == NodeType.HIDDEN }, nodes.filter { it.type == NodeType.OUTPUT }, connections)
}

fun plotTest() {
    val panel = PlotPanel()
    val app = SimpleFrame(panel)

    val X = (0 until 100).toDouble()
    val Y1 = X.map { Math.cos(it / 100.0 * 2 * 3.141592) }
    val Y2 = X.map { Math.sin(it / 100.0 * 9 * 3.141592) }

    panel.plot(X, Y1, linked = true, dotSize = 0, color = Color.BLUE)
    panel.plot(X, Y2, linked = true, dotSize = 0, label = "Sin rapide")
    app.repaint()
}

private fun IntRange.toDouble(): List<Double> {
    return this.map { it.toDouble() }
}
