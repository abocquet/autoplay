package neat.mutation

import neat.Config
import neat.stucture.Genome
import java.util.*

class ReplaceConnectionWeightMutation : MutationInterface {
    override operator fun invoke(g: Genome): Genome {

        val r = Random()
        val connection = g.connections[r.nextInt(g.connections.size)]

        var weight = r.nextGaussian() * Config.bias_init_stdev + Config.bias_init_mean
        weight = Math.max(weight, Config.bias_min_value)
        weight = Math.min(weight, Config.bias_max_value)

        return g.copy(connections = g.connections.map {
            if(it.id == connection.id){ it.copy(weight = weight) } else { it }
        })

    }
}