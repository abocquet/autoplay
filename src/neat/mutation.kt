package neat

import java.util.*

interface MutationInterface {
    fun mutate(g: Graph) : Graph
}

class AddNodeMutation : MutationInterface {

    override fun mutate(graph: Graph) : Graph {
        val r = Random()

        var from = graph.nodes[r.nextInt(graph.nodes.size)]
        val to: Node?
        if(r.nextDouble() < graph.output.size / (graph.output.size + graph.hidden.size)){
            to = graph.output[r.nextInt(graph.output.size)]
        } else {
            to = graph.hidden[r.nextInt(graph.hidden.size)]
        }

        val new = Node(Node.innovation, config.bias_init_mean, 1.0)
        val hidden = graph.hidden.plus(new)
        val connections: Map<Node, List<Connection>> = graph.connections
            .mapValues {  if(it.key == to) { it.value.plus(Connection(Connection.innovation, 1.0, new)) } else { it.value } }
            .plus(Pair(new, listOf(Connection(Connection.innovation, 1.0, from))))

        return Graph(graph.inputs, hidden, graph.output, connections)
    }
}

class AddConnectionMutation : MutationInterface {
    override fun mutate(graph: Graph) : Graph {
        val r = Random()

        var from = graph.nodes[r.nextInt(graph.nodes.size)]
        val to: Node?
        if(r.nextDouble() < graph.output.size / (graph.output.size + graph.hidden.size)){
            to = graph.output[r.nextInt(graph.output.size)]
        } else {
            to = graph.hidden[r.nextInt(graph.hidden.size)]
        }

        val connections: Map<Node, List<Connection>> = graph.connections
                .plus(Pair(to, listOf(Connection(Connection.innovation, 1.0, from))))

        return Graph(graph.inputs, graph.hidden, graph.output, connections)
    }
}