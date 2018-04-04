package neat.mutation

import neat.Config
import neat.stucture.Connection
import neat.stucture.Genome
import neat.stucture.Node
import neat.stucture.NodeType
import java.util.*

class AddNodeMutation(val config: Config) : MutationInterface {

    override operator fun invoke(g: Genome) : Genome {
        val r = Random()

        val from = g.nodes[r.nextInt(g.nodes.size)]
        val to = if(r.nextDouble() < g.output.size / (g.output.size + g.hidden.size)){
            g.output[r.nextInt(g.output.size)]
        } else {
            g.hidden[r.nextInt(g.hidden.size)]
        }

        val oldWeight : Double? = g.connections.firstOrNull { it.to == to.id && it.from == from.id }?.weight

        var newWeight = r.nextGaussian() * config.weight_init_stdev + config.weight_init_mean
        newWeight = Math.max(newWeight, config.weight_min_value)
        newWeight = Math.min(newWeight, config.weight_max_value)

        val new = Node(Node.innovation, 1.0, NodeType.HIDDEN)
        val hidden = g.hidden.plus(new)
        val connections: List<Connection> = g.connections
                .filterNot { it.to == to.id && it.from == from.id }
                .plus(Connection(Connection.innovation, oldWeight ?: newWeight, new.id, to.id, true))
                .plus(Connection(Connection.innovation, 1.0, from.id, new.id, true))

        return Genome(g.inputs, hidden, g.output, connections)
    }
}
