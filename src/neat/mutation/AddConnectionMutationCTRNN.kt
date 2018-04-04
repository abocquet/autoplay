package neat.mutation

import neat.Config
import neat.stucture.Connection
import neat.stucture.Genome
import neat.stucture.Node
import java.util.*

class AddConnectionMutationCTRNN(val config: Config) : MutationInterface {
    override operator fun invoke(g: Genome) : Genome {
        if(g.hidden.isEmpty()){
            return g
        }

        val r = Random()

        val from = g.nodes[r.nextInt(g.nodes.size)]
        val to = if(r.nextDouble() < g.output.size / (g.output.size + g.hidden.size)){
            g.output[r.nextInt(g.output.size)]
        } else {
            g.hidden[r.nextInt(g.hidden.size)]
        }

        if (g.connections.any { it.from == from.id && it.to == to.id }){
            return Genome(g.inputs, g.hidden, g.output, g.connections.map { if(it.from == from.id && it.to == to.id) { it.copy(enabled = true) } else { it } })
        }

        var weight = r.nextGaussian() * config.weight_init_stdev + config.weight_init_mean
        weight = Math.max(weight, config.weight_min_value)
        weight = Math.min(weight, config.weight_max_value)

        val connections: List<Connection> = g.connections.plus(Connection(Connection.innovation, weight, from.id, to.id, true))

        return Genome(g.inputs, g.hidden, g.output, connections)
    }
}
