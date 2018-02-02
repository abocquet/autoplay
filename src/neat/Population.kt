package neat

import java.util.*

class Population(inputs: Int, outputs: Int, val eval: (g: Genome) -> Double) {

    val r = Random()
    var population = Array(config.pop_size, { Genome(inputs, outputs) })
    var generation = 0
    var species = mutableMapOf<Genome, MutableList<Genome>>()

    fun evolve(){
        population.sortBy { this.eval(it) }
        var i = 0
        val newPop = Array<Genome?>(population.size, { null })

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

        generation++
        speciate()
    }

    fun crossover(g1: Genome, g2: Genome): Genome {
        return g1
    }

    fun mutate(p: Genome) : Genome {
        if(r.nextDouble() > config.conn_add_prob) {
            return AddConnectionMutation().mutate(p)
        } else if(r.nextDouble() > config.node_add_prob) {
            return AddNodeMutation().mutate(p)
        }

        return p
    }

    fun speciate() {
        val distance = GenomeDistanceCache()
        species.forEach { t, u -> u.clear() }
        population.forEach { g ->
            for((k, l) in species){
                if(distance(k, g) < config.compatibility_threshold){
                    l.add(g)
                    return
                }
            }

            species[g] = mutableListOf(g)
        }

        species.filterValues { it.isNotEmpty() }
    }

}