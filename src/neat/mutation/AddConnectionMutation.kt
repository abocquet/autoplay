package neat.mutation

import neat.Config
import neat.stucture.Connection
import neat.stucture.Genome
import neat.stucture.Node
import java.util.*

class AddConnectionMutation : MutationInterface {
    override operator fun invoke(g: Genome) : Genome {
        if(g.hidden.isEmpty()){
            return g
        }

        val r = Random()

        val from = g.nodes[r.nextInt(g.nodes.size)]
        val to: Node?
        to = if(r.nextDouble() < g.output.size / (g.output.size + g.hidden.size)){
            g.output[r.nextInt(g.output.size)]
        } else {
            g.hidden[r.nextInt(g.hidden.size)]
        }

        if (g.connections.any { it.from == from.id && it.to == to.id }){
            return g
        }

        var weight = r.nextGaussian() * Config.weight_init_stdev + Config.weight_init_mean
        weight = Math.max(weight, Config.weight_min_value)
        weight = Math.min(weight, Config.weight_max_value)

        val connections: List<Connection> = g.connections.plus(Connection(Connection.innovation, weight, from.id, to.id, true))

        return Genome(g.inputs, g.hidden, g.output, connections)
    }
}
