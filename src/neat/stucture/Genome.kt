package neat.stucture

import neat.Config

data class Genome(val inputs: List<Node>, val hidden: List<Node>, val output: List<Node>, val connections: List<Connection>): Cloneable {
    val nodes = inputs.plus(output).plus(hidden)

    init {
        assert(nodes.zipWithNext { n1, n2 -> n1.id < n2.id }.all { it })
        assert(connections.zipWithNext { c1, c2 -> c1.id < c2.id }.all { it })
    }

    override fun toString(): String {
        return "Genome(\n\tinputs=$inputs\n\thidden=$hidden\n\toutput=$output\n\tconnections=$connections\n)"
    }
}

fun Genome(n_inputs: Int, n_outputs: Int): Genome {

    var nid = -n_inputs
    val inputs = List(n_inputs, { Node(nid++, Config.bias_init_mean, NodeType.INPUT) })
    val outputs = List(n_outputs, { Node(nid++, 1.0, NodeType.OUTPUT) })

    val connections = mutableListOf<Connection>()
    if(Config.initial_connection == "full") {
        var cid = - n_outputs
        for(i in 1..n_inputs){
            for(j in 0 until n_outputs) {
                connections.add(Connection(cid++, 1.0, -i, j, true))
            }
        }
    }

    return Genome(inputs, emptyList(), outputs, connections.toList())
}