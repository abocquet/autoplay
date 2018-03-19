package neat

import neat.mutation.*
import neat.stucture.Genome
import java.io.Serializable
import java.lang.Double.max
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*

class Population(inputs: Int, outputs: Int, private val eval: (Genome) -> Double): Serializable {

    // Initialisation
    private var r = Random()
    private var population = Array(Config.pop_size, { Genome(inputs, outputs) })
    val members: List<Genome>
        get() = population.asList()

    private var species = mutableListOf<Species>()
    private var cache = FitnessCache(eval)

    var generation = 0
        private set

    // Interfaces avec l'extrieur
    val scores : List<Double>
        get() = population.map { cache(it) }

    val results : String
        get() {
            population.sortBy(cache::fitness)
            population.reverse()
            val best = population[0]
            return "$best has a score of ${cache.fitness(best)}"
        }

    fun fitness(genome: Genome): Double { return cache(genome) }

    // Méthodes utiles
    fun evolve(n: Int){ (0 until n).forEach { evolve() } }

    fun evolve(show: Boolean = false){
        generation++
        cache = FitnessCache(eval)

        population.sortBy { - cache.fitness(it) }
        val newPop = Array<Genome?>(population.size, { null })

        for(i in 0 until Config.elitism){
            newPop[i] = population[i]
        }

        val crossover = Crossover()
        speciate()

        if(show){
            println("best: ${cache(population[0])} (${population[0].hidden.size}) - worst : ${cache(population[population.size - 1])} (${population[population.size - 1].hidden.size})")
            println(species.map { "${it.members.size} " })
            println(population.map { "${it.hidden.size} " })
            println(species.map{ "${it.stagnation}" })
            println()
        }

        species.removeAll { it.stagnation > Config.max_stagnation || it.members.size == 0 }
        val total = species.map(cache::fitness).sum()
        var offspring_count = 0
        val offspring = species.fold(listOf<Genome>()) { children, s ->
            val size = s.members.size
            val n = (cache.fitness(s) * population.size / total).toInt()
            val r = Random()

            s.members.sortBy(cache::fitness)
            val eliteSize = min(s.members.size, min(Config.species_elitism, n))

            offspring_count += n

            children.plus(s.members.take(eliteSize)).plus(List(n - eliteSize, {
                val p1 = s.members[r.nextInt(size)]
                val p2 = s.members[r.nextInt(size)]
                if(cache(p1) > cache(p2)) crossover(p1, p2) else crossover(p2, p1)
            }))

        }.plus(List(max(0, Config.pop_size - offspring_count), { // On complète avec des individus tirés au hasard
            val s = species[r.nextInt(species.size)]
            val size = s.members.size

            val p1 = s.members[r.nextInt(size)]
            val p2 = s.members[r.nextInt(size)]

            if(cache(p1) > cache(p2)) crossover(p1, p2) else crossover(p2, p1)
        }))
        .map(this::mutate)
        .sortedBy({ - cache(it) })


        val it = offspring.iterator()
        for(i in Config.elitism until Config.pop_size){
            population[i] = it.next()
        }

        species.forEach{ it.nextGen() }
    }

    private fun mutate(_p: Genome) : Genome {
        var p = _p

        val div1 = max(1.0, Config.weight_replace_rate + Config.weight_mutate_rate + Config.bias_replace_rate + Config.bias_mutate_rate)
        val nd1 = r.nextDouble()

        p = when {
            nd1 < (Config.weight_replace_rate) / div1 -> ReplaceConnectionWeightMutation()(p)
            nd1 < (Config.weight_replace_rate + Config.weight_mutate_rate) / div1 -> MutateConnectionWeightMutation()(p)
            nd1 < (Config.weight_replace_rate + Config.weight_mutate_rate + Config.bias_replace_rate) / div1 -> ReplaceBiasMutation()(p)
            nd1 < (Config.weight_replace_rate + Config.weight_mutate_rate + Config.bias_replace_rate + Config.bias_mutate_rate) / div1 -> MutateBiasMutation()(p)
            else -> p
        }

        val div2 = max(1.0, Config.conn_add_prob + Config.conn_delete_prob + Config.node_add_prob + Config.node_delete_prob)
        val nd2 = r.nextDouble()

        p =  when {
            nd2 < (Config.conn_add_prob) / div2 -> AddConnectionMutation()(p)
            nd2 < (Config.conn_add_prob + Config.conn_delete_prob) / div2 -> RemoveConnectionMutation()(p)
            nd2 < (Config.conn_add_prob + Config.conn_delete_prob + Config.node_add_prob ) / div2 -> AddNodeMutation()(p)
            nd2 < (Config.conn_add_prob + Config.conn_delete_prob + Config.node_add_prob + Config.node_delete_prob) / div2 -> DisableNodeMutation()(p)
            else -> p
        }

        return p
    }

    private fun speciate() {

        val distance = GenomeDistanceCache()
        species.forEach { it.members.clear() }
        population.forEach { g ->

            var flag = false
            for(s in species){
                if(distance(s.representative, g) < Config.compatibility_threshold){
                    s.members.add(g)
                    s.addFitness(cache(g))
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

    val score: Double
        get () {
            return population.map { FitnessCache(eval).fitness(it) }.max()!!
        }
}