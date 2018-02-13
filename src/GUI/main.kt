package GUI

import GUI.Graph.GraphViewer
import neat.stucture.Connection
import neat.stucture.Genome
import neat.stucture.Node
import neat.stucture.NodeType

fun main(args: Array<String>) {

    val genome = Genome(3, 2)

    val inputs = listOf(
            Node(-4, 1.0, NodeType.BIAS),
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
            Connection(10, 1.0,-1, 2, true)
    )

    val p1 = Genome(inputs, p1h, outputs, c1)
    val p2 = Genome(inputs, p2h, outputs, c2)

    val app = GraphViewer(p2)

}
