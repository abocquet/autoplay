package GUI.Panels

import neat.stucture.Connection
import neat.stucture.Genome
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.lang.Integer.max
import javax.swing.JPanel

class GraphPanel(val g: Genome) : JPanel() {

    val margin = 40
    val nodeDiameter = 30
    var scale = 0.5

    var organization: Map<Int, Pair<Double, Double>>

    override fun getPreferredSize(): Dimension {
        if (isPreferredSizeSet) {
            return super.getPreferredSize()
        }

        return organization
                .map { it.value }
                .fold(Dimension(0, 0)) { acc, value ->
                    Dimension(
                            max(acc.width, (value.first * nodeDiameter * 10 * scale).toInt()),
                            max(acc.height, (value.second * nodeDiameter * 10 * scale).toInt())
                    )
                }
    }


    init {

        val distanceCache = mutableMapOf<Int, Int>()
        val exploredConnections = mutableListOf<Connection>()
        var connectionsToExplore = mutableSetOf<Connection>()

        connectionsToExplore.addAll(g.connections.filter { it.to in g.output.map { it.id } })
        g.output.forEach { distanceCache[it.id] = 0 }
        g.connections.filter { it.to in g.output.map { it.id } }.forEach { distanceCache[it.from] = 1 }

        while (connectionsToExplore.isNotEmpty()) {
            val newConnectionsToExplore = mutableSetOf<Connection>()
            connectionsToExplore.forEach { c ->
                exploredConnections.add(c)

                g.connections.filter { it.to == c.from }.forEach {
                    distanceCache[it.from] = max(distanceCache[c.from]!! + 1, distanceCache[it.from] ?: 0)
                    g.connections.filter { it2 -> it2.to == it.from }
                }
            }
            connectionsToExplore = newConnectionsToExplore
        }

        val maxDistance = distanceCache.maxBy { it.value }!!.value
        g.inputs.forEach { distanceCache[it.id] = maxDistance }

        val countInColumn = Array(maxDistance + 1, { number -> distanceCache.count { it.value == number } })
        val counter = Array(maxDistance + 1, { 1 })

        organization = distanceCache
                .map {
                    it.key to (
                            1 - (distanceCache[it.key]!!.toDouble() / maxDistance) to
                                    counter[distanceCache[it.key]!!]++.toDouble() / (countInColumn[distanceCache[it.key]!!] + 1)
                            )
                }.toMap()

    }

    public override fun paintComponent(_graphics: Graphics) {

        val graphics = _graphics as Graphics2D

        val transform = AffineTransform()
        transform.scale(scale, scale)
        graphics.transform(transform)

        g.connections
                .filter { organization.containsKey(it.from) && organization.containsKey(it.to) }
                .forEach { c ->
                    val p1 = positionOf(c.from)
                    val p2 = positionOf(c.to)

                    graphics.color = if (c.weight > 0) Color.GREEN else Color.RED
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

    private fun positionOf(nodeId: Int): Pair<Int, Int> {
        val x = (width - 2 * margin) * organization[nodeId]!!.first + margin / 2
        val y = height * organization[nodeId]!!.second

        return Pair(x.toInt(), y.toInt())
    }
}
