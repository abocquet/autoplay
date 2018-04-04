package neat.neural

import neat.stucture.Genome
import neat.stucture.Node
import neat.stucture.NodeType

class GraphNode(
        val node: Node,
        val connections: MutableList<Pair<Double, GraphNode>>,
        var value: Double = 0.0
)

class CTRNN(private val genome: Genome, var final_time: Double, var time_step: Double) : NeuralNetwork {

    private val nodes = genome.nodes.map { GraphNode(it, mutableListOf()) }

    init {

        nodes.forEach { it.connections.addAll(
            genome.connections
                .filter { c -> c.to == it.node.id && c.enabled }
                .map {
                    c -> Pair(c.weight, nodes.filter { it.node.id == c.from }[0])
                }
        ) }

    }

    override fun eval(input_values: Array<Double>) : Array<Double> {
        /* Advance the simulation by the given amount of time, assuming that input_nodes are
        constant at the given values during the simulated time. */
        var time_seconds = 0.0
        if (input_values.size != genome.inputs.size) {
            throw RuntimeException("Expected ${-genome.nodes[0].id} input_nodes, got ${genome.inputs.size}")
        }

        nodes
            .filter { it.node.type == NodeType.INPUT }
            .forEach { it.value = input_values[it.node.id + genome.inputs.size] }

        while (time_seconds < final_time) {
            val dt = java.lang.Double.min(time_step, final_time - time_seconds)

            // http://neat-python.readthedocs.io/en/latest/ctrnn.html
            for (node in nodes) {
                if (node.node.id < 0) {
                    continue
                }

                node.value =
                        node.value +
                        dt / node.node.timeConstant * (
                        -node.value + node.node.activation(node.node.bias + node.connections.filter { it.second.node.enabled }.map { it.first * it.second.value }.sum())
                        )

                time_seconds += dt
            }
        }

        return nodes.filter { it.node.type == NodeType.OUTPUT }.map { it.value }.toTypedArray()
    }
}