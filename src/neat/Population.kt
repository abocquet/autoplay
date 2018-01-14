package neat

import java.util.*

class Population(inputs: Int, outputs: Int, size: Int, val eval: (g: Graph) -> Double) {

    val r = Random()
    var population = Array(size, { Graph(inputs, outputs) })

    fun evolve(){

    }

    fun crossover(pop: List<Graph>){
        pop.sortedBy { this.eval(it) }
        var i = 0
        val newPop = mutableListOf<Graph>()

        while(i < config.elitism){
            newPop[i] = pop[i]
            i++
        }

        while (i < pop.size){
            newPop[i] = crossover(
                pop[r.nextInt(pop.size)],
                pop[r.nextInt(pop.size)]
            )
        }


    }

    fun mutate(p: Graph) : Graph {
        if(r.nextDouble() > config.conn_add_prob) {
            return ConnectionMutation().mutate(p)
        } else if(r.nextDouble() > config.node_add_prob) {
            return NodeMutation().mutate(p)
        }

        return p
    }

}