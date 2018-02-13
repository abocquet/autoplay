package neat

import neat.mutation.*
import neat.stucture.Genome
import java.util.*

class Population(inputs: Int, outputs: Int, private val eval: (Genome) -> Double) {

    private val r = Random()
    internal var population = Array(Config.pop_size, { Genome(inputs, outputs) })
    private var generation = 0
    private var species = mutableListOf<Species>()

    val results : String
        get() {
            val cache = FitnessCache(eval)
            population.sortBy(cache::fitness)
            population.reverse()
            val best = population[0]
            return "$best has a score of ${cache.fitness(best)}"
        }

    fun evolve(n: Int){ (0 until n).forEach { evolve() } }

    private fun evolve(){
        generation++
        val cache = FitnessCache(eval)

        population.sortBy { cache.fitness(it) }
        val newPop = Array<Genome?>(population.size, { null })

        for(i in 0 until Config.elitism){
            newPop[i] = population[i]
        }

        val crossover = Crossover()
        speciate()

        println("number of species : ${species.size}")
        println("popsize: ${population.size}")
        species.forEach { print("${it.members.size} ") }
        println()

        val total = species.map(cache::fitness).sum()
        val offspring = species.fold(listOf<Genome>()) { children, s ->
            val n = ((cache.fitness(s) / total) * population.size).toInt()
            val r = Random()
            val size = s.members.size

            val p1 = s.members[r.nextInt(size)]
            val p2 = s.members[r.nextInt(size)]

            children.plus(List(n, {
                if(cache.fitness(p1) > cache.fitness(p2)) crossover(p1, p2) else crossover(p2, p1)
            }))
        }
        .map(this::mutate)
        .sortedBy(cache::fitness)

        val it = offspring.iterator()
        for(i in Config.elitism until population.size){
            population[i] = it.next()
        }

        population.forEach { print("${cache.fitness(it)} ") }
        println()
    }

    private fun mutate(p: Genome) : Genome {
        return when {
            r.nextDouble() > Config.conn_add_prob -> AddConnectionMutation()(p)
            r.nextDouble() > Config.bias_replace_rate -> ChangeBiasMutation()(p)
            r.nextDouble() > Config.node_delete_prob -> DisableNodeMutation()(p)
            r.nextDouble() > Config.conn_delete_prob -> RemoveConnectionMutation()(p)
            r.nextDouble() > Config.node_add_prob -> AddNodeMutation()(p)
            else -> p
        }
    }

    private fun speciate() {

        val distance = GenomeDistanceCache()
        species.forEach { it.members.clear() }
        population
        population.forEach { g ->

            var flag = false
            for(s in species){
                if(distance(s.representative, g) < Config.compatibility_threshold){
                    s.members.add(g)
                    flag = true
                    break
                }
            }

            if(!flag) {
                species.add(Species(g, generation))
            }
        }

        species.removeAll { it.members.size == 0 }
        species.forEach { assert(it.members.size > 0) }
    }

}