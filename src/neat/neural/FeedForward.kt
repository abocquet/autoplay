package neat.neural

import neat.stucture.Genome

class FeedForward(private val genome: Genome) : NeuralNetwork {

    private val mem = mutableMapOf<Int, Double>()

    override fun eval(input_values: Array<Double>) : Array<Double> {
        return genome.output.map { valueOf(it.id, input_values) }.toTypedArray()
    }

    fun valueOf(nid: Int, input_values: Array<Double>) : Double {

        if(nid < 0){
            return input_values[nid + input_values.size]
        }

        if(mem.containsKey(nid) && mem[nid] != null){
            return mem[nid]!!
        }

        val node = genome.nodes.first { it.id == nid }

        return node.activation(
            genome.connections
                .filter { it.to == nid }
                .map { c -> valueOf(c.from, input_values) * c.weight }
                .sum()
            + node.bias
        )
    }

}