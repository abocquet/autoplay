package neat.mutation

import neat.Config
import neat.stucture.Genome
import java.util.*

class ReplaceBiasMutation(val config: Config) : MutationInterface {
    override fun invoke(g: Genome): Genome {

        val r = Random()

        val to = if(r.nextDouble() < g.output.size / (g.output.size + g.hidden.size)){
            g.output[r.nextInt(g.output.size)]
        } else {
            g.hidden[r.nextInt(g.hidden.size)]
        }

        var bias = r.nextGaussian() * config.bias_init_stdev + config.bias_init_mean
        bias = Math.max(bias, config.bias_min_value)
        bias = Math.min(bias, config.bias_max_value)

        return Genome(
                g.inputs,
                g.hidden.map { if(it == to) to.copy(bias = bias) else it },
                g.output.map { if(it == to) to.copy(bias = bias) else it },
                g.connections
        )
    }

}
