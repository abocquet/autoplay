package neat.mutation

import neat.stucture.Connection
import neat.stucture.Genome
import neat.stucture.Node
import neat.stucture.NodeType
import java.util.*

class AddNodeMutation : MutationInterface {

    override operator fun invoke(g: Genome) : Genome {
        val r = Random()

        val from = g.nodes[r.nextInt(g.nodes.size)]
        val to: Node?
        to = if(r.nextDouble() < g.output.size / (g.output.size + g.hidden.size)){
            g.output[r.nextInt(g.output.size)]
        } else {
            g.hidden[r.nextInt(g.hidden.size)]
        }

        val new = Node(Node.innovation, 1.0, NodeType.HIDDEN)
        val hidden = g.hidden.plus(new)
        val connections: List<Connection> = g.connections
                .map { if(it.to == to.id && it.from == from.id) { Connection(it.id, it.weight, new.id, it.to, false) } else { it } }
                .plus(Connection(Connection.innovation, 1.0, from.id, new.id, true))

        return Genome(g.inputs, hidden, g.output, connections)
    }
}
