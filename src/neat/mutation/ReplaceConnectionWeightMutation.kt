package neat.mutation

import neat.Config
import neat.stucture.Genome
import java.util.*

class ReplaceConnectionWeightMutation(val config: Config) : MutationInterface {
    override operator fun invoke(g: Genome): Genome {

        val r = Random()
        val connection = g.connections[r.nextInt(g.connections.size)]

        var weight = r.nextGaussian() * config.bias_init_stdev + config.bias_init_mean
        weight = Math.max(weight, config.bias_min_value)
        weight = Math.min(weight, config.bias_max_value)

        return g.copy(connections = g.connections.map {
            if(it.id == connection.id){ it.copy(weight = weight) } else { it }
        })

    }
}