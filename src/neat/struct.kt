package neat

import java.lang.Double.min
import kotlin.math.exp

enum class NodeType {
    INPUT, HIDDEN, OUTPUT
}

data class Node(
        val id: Int,
        val bias: Double,
        val timeConstant: Double,
        val type: NodeType = NodeType.HIDDEN,
        val enabled: Boolean = true

) {
    val activation = fun(x: Double): Double { return 1 / (1 + exp(-x))}
    override fun toString(): String {
        return "Node(id=$id)"
    }


}

data class Connection (
        val id: Int,
        var weight: Double,
        val pointsFrom: Node
)

class Graph(val inputs: List<Node>, val hidden: List<Node>, val output: List<Node>, val connections: HashMap<Node, List<Connection>>): Cloneable {

    val nodes = inputs.plus(hidden).plus(output)

    fun eval(input_values: Array<Double>, final_time: Double, time_step: Double) : Array<Double> {
        /* Advance the simulation by the given amount of time, assuming that input_nodes are
        constant at the given values during the simulated time. */
        var time_seconds = 0.0
        if (input_values.size != inputs.size){
            throw RuntimeException ("Expected ${-this.nodes[0].id} input_nodes, got ${inputs.size}")
        }

        val values = nodes.associateBy({ it }, { .0 }).toMutableMap()
        for (n in inputs){
            values[n] = input_values[n.id + inputs.size]
        }
        println(values.toString())

        while(time_seconds < final_time) {
            val dt = min(time_step, final_time - time_seconds)

            // http://neat-python.readthedocs.io/en/latest/ctrnn.html
            for (it in values){
                if(it.key.id < 0){
                    continue
                }

                values[it.key] =
                    it.value + dt / it.key.timeConstant * (-it.value +
                        it.key.activation(it.key.bias + connections[it.key]!!.map({ it.weight * values[it.pointsFrom]!! }).sum())
                    )
            }

            time_seconds += dt
        }

        return values.filter { it.key.type ==  NodeType.OUTPUT }.map { it.value }.toTypedArray()
    }

}

fun Graph(n_inputs: Int, n_outputs: Int): Graph {

    var nid = -n_inputs
    val inputs = List(n_inputs, { Node(nid++, config.bias_init_mean, 1.0, NodeType.INPUT) })
    val outputs = List(n_outputs, { Node(nid++, config.bias_init_mean, 1.0, NodeType.INPUT) })

    val connections = hashMapOf<Node, List<Connection>>()
    if(config.initial_connection == "full") {
        var cid = 0
        for(j in 0 until n_outputs){
            var i = 0
            connections[outputs[j]] = List(n_inputs, { Connection(cid++, 1.0, inputs[i++]) })
        }

    }

    return Graph(inputs, emptyList(), outputs, connections)
}