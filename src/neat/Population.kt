package neat

import java.util.*

class Population(inputs: Int, outputs: Int, size: Int, val eval: (g: Graph) -> Double) {

    val r = Random()
    var population = Array(size, { Graph(inputs, outputs) })

    fun evolve(){
        population.sortBy { this.eval(it) }
        var i = 0
        val newPop = Array<Graph?>(population.size, { null })

        while(i < config.elitism){
            newPop[i] = population[i]
            i++
        }

        while (i < population.size){
            if(r.nextDouble() > 0.5) {
                newPop[i] = crossover(
                        population[r.nextInt(population.size)],
                        population[r.nextInt(population.size)]
                )
            } else {
                newPop[i] = population[r.nextInt(population.size)]
            }

            newPop[i] = mutate(newPop[i]!!)
            i++
        }
    }

    fun crossover(g1: Graph, g2: Graph): Graph {
        return g1
    }

    fun mutate(p: Graph) : Graph {
        if(r.nextDouble() > config.conn_add_prob) {
            return AddConnectionMutation().mutate(p)
        } else if(r.nextDouble() > config.node_add_prob) {
            return AddNodeMutation().mutate(p)
        }

        return p
    }

}