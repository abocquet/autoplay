package neat.mutation

import neat.Config
import neat.stucture.Connection
import neat.stucture.Genome
import neat.stucture.NodeType
import java.util.*

class ChangeBiasMutation : MutationInterface {
    override fun invoke(g: Genome): Genome {

        val r = Random()

        val to = if(r.nextDouble() < g.output.size / (g.output.size + g.hidden.size)){
            g.output[r.nextInt(g.output.size)]
        } else {
            g.hidden[r.nextInt(g.hidden.size)]
        }

        var weight = r.nextGaussian() * Config.bias_init_stdev * (Config.bias_max_value + Config.bias_min_value) + Config.bias_min_value + Config.bias_init_mean - 1
        weight = Math.max(weight, Config.bias_min_value)
        weight = Math.min(weight, Config.bias_max_value)

        assert(g.nodes[0].type == NodeType.BIAS)
        val biasNode = g.nodes[0]

        var flag = false
        val connections = g.connections.map {
            if(it.from == g.nodes[0].id && it.to == to.id) {
                flag = true
                Connection(it.id, weight, it.from, it.to, it.enabled)
            } else { it }
        }

        return if (!flag){
            Genome(g.inputs, g.hidden, g.output, connections.plus(Connection(Connection.innovation, weight, biasNode.id, to.id, true)))
        } else {
            Genome(g.inputs, g.hidden, g.output, connections)
        }



    }

}