package neat

import kotlin.math.exp

enum class NodeType {
    INPUT, HIDDEN, OUTPUT, BIAS
}

data class Node(
        val id: Int,
        val timeConstant: Double,
        val type: NodeType = NodeType.HIDDEN,
        val enabled: Boolean = true
) {
    val activation = fun(x: Double): Double { return 1 / (1 + exp(-x))}

    override fun toString(): String {
        return "Node(id=$id, type=$type)"
    }

    companion object {
        var innovation = 0
            private set
            get () { return field++ }
    }
}

data class Connection (
    val id: Int,
    var weight: Double,
    val from: Int,
    val to: Int,
    val enabled: Boolean
) {
    companion object {
        var innovation = 0
            private set
            get () { return field++ }
    }
}

class Genome(val inputs: List<Node>, val hidden: List<Node>, val output: List<Node>, val connections: List<Connection>): Cloneable {
    val nodes = inputs.plus(hidden).plus(output)
}

fun Genome(n_inputs: Int, n_outputs: Int): Genome {

    var nid = -n_inputs-1
    val inputs = listOf(Node(nid++, .0, NodeType.BIAS)).plus(List(n_inputs, { Node(nid++, config.bias_init_mean,  NodeType.INPUT) }))
    val outputs = List(n_outputs, { Node(nid++, 1.0, NodeType.OUTPUT) })

    val connections = mutableListOf<Connection>()
    if(config.initial_connection == "full") {
        var cid = 0
        for(i in 1..n_inputs){
            for(j in 0 until n_outputs) {
                connections.add(Connection(cid++, 1.0, -i, j, true))
            }
        }
    }

    return Genome(inputs, emptyList(), outputs, connections.toList())
}