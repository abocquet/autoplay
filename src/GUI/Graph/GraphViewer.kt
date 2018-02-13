package GUI.Graph

import neat.stucture.Genome
import java.awt.Color
import java.awt.Graphics
import java.lang.Integer.max
import javax.swing.JFrame
import javax.swing.JPanel

class GraphViewer(g: Genome) : JFrame() {

    init {
        isVisible = true
        val panel = GraphPanel(g)
        setSize(panel.preferedSize.first, panel.preferedSize.second)
        contentPane =  panel

    }

}

class GraphPanel(val g : Genome): JPanel() {

    val margin = 40
    val nodeDiameter = 30

    var organization: Map<Int, Pair<Double, Double>>

    val preferedSize : Pair<Int, Int>
        get() {
            return organization
                .map { it.value }
                .fold(Pair(0, 0)) { acc, value ->
                    Pair(
                        max(acc.first, (value.first * nodeDiameter * 10).toInt()),
                        max(acc.second, (value.second * nodeDiameter * 10).toInt())
                    )
                }
        }

    init {

        val nodesDistances : MutableMap<Int, Int> = mutableMapOf()
        var nodesToExplore = mutableSetOf<Int>() // Contient les id des nodes a explorer

        nodesToExplore.addAll(g.output.map { it.id })
        var distance = 0

        while(nodesToExplore.isNotEmpty()){
            println(nodesToExplore)
            val newNodesToExplore = mutableSetOf<Int>()

            nodesToExplore.forEach { nid ->
                nodesDistances[nid] = distance
                newNodesToExplore.addAll(g.connections
                    .filter { it.to == nid }
                    .map { c -> c.from }
                    .filterNot { nodesToExplore.contains(it) || newNodesToExplore.contains(it) }
                )
            }

            nodesToExplore = newNodesToExplore
            distance++
        }

        val countInColumn = Array(distance, { number -> nodesDistances.count { it.value == number } })
        val counter = Array(distance, {1})

        organization = nodesDistances
        .map { it.key to (
            (distance-1).toDouble() - (nodesDistances[it.key]!!.toDouble() / (distance - 1)) to
            counter[nodesDistances[it.key]!!]++.toDouble() / (countInColumn[nodesDistances[it.key]!!] + 1)
        ) }.toMap()

        println(organization)
    }

    public override fun paintComponent(graphics: Graphics){
        g.connections
        .filter { organization.containsKey(it.from) && organization.containsKey(it.to) }
        .forEach { c ->
            val p1 = positionOf(c.from)
            val p2 = positionOf(c.to)

            graphics.color = if(c.weight > 0) Color.GREEN else Color.RED
            graphics.drawLine(p1.first + nodeDiameter / 2, p1.second + nodeDiameter / 2, p2.first + nodeDiameter / 2, p2.second + nodeDiameter / 2)
        }

        organization.forEach {
            val pos = positionOf(it.key)
            graphics.color = Color.WHITE
            graphics.fillOval(pos.first, pos.second, nodeDiameter, nodeDiameter)
            graphics.color = Color.BLACK
            graphics.drawString(it.key.toString(), pos.first + nodeDiameter / 3, pos.second + nodeDiameter / 2 + 5)

        }

    }

    private fun positionOf(nodeId: Int) : Pair<Int, Int>{
        val x = (width - 2 * margin) * organization[nodeId]!!.first + margin / 2
        val y = height * organization[nodeId]!!.second

        return Pair(x.toInt(), y.toInt())
    }
}
