package neat

import java.util.*

interface MutationInterface {
    fun mutate(g: Genome) : Genome
}

class AddNodeMutation : MutationInterface {

    override fun mutate(graph: Genome) : Genome {
        val r = Random()

        var from = graph.nodes[r.nextInt(graph.nodes.size)]
        val to: Node?
        if(r.nextDouble() < graph.output.size / (graph.output.size + graph.hidden.size)){
            to = graph.output[r.nextInt(graph.output.size)]
        } else {
            to = graph.hidden[r.nextInt(graph.hidden.size)]
        }

        val new = Node(Node.innovation, config.bias_init_mean, NodeType.HIDDEN)
        val hidden = graph.hidden.plus(new)
        val connections: List<Connection> = graph.connections
            .map { if(it.to == to.id && it.from == from.id) { Connection(it.id, it.weight, it.from, it.to, false) } else { it } }
            .plus(Connection(Connection.innovation, 1.0, from.id, to.id, true))

        return Genome(graph.inputs, hidden, graph.output, connections)
    }
}

class AddConnectionMutation : MutationInterface {
    override fun mutate(genome: Genome) : Genome {
        val r = Random()

        var from = genome.nodes[r.nextInt(genome.nodes.size)]
        val to: Node?
        if(r.nextDouble() < genome.output.size / (genome.output.size + genome.hidden.size)){
            to = genome.output[r.nextInt(genome.output.size)]
        } else {
            to = genome.hidden[r.nextInt(genome.hidden.size)]
        }

        val connections: List<Connection> = genome.connections
            .plus(Connection(Connection.innovation, 1.0, from.id, to.id, true))

        return Genome(genome.inputs, genome.hidden, genome.output, connections)
    }
}