package neat

import neat.mutation.*
import neat.stucture.Genome
import java.io.Serializable
import java.lang.Double.max
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*
import kotlin.concurrent.thread

class Population(inputs: Int, outputs: Int, internal val eval: (Genome) -> Double): Serializable {

    // Initialisation
    val config = Config()
    private var r = Random()
    private var population = Array(config.pop_size, { Genome(inputs, outputs, config) })
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
            return "$best has a score of ${cache.fitness(best)} : \n ${best.connections}"
            return "$best has a score of ${cache.fitness(best)} : \n ${best.connections}"
        }

    fun fitness(genome: Genome): Double { return cache(genome) }

    // Méthodes utiles
    fun evolve(n: Int, async: Boolean = true){ (0 until n).forEach { evolve(async = async) } }

    fun evolve(show: Boolean = false, async: Boolean = true){
        generation++

        if (async) {

            // On calcule les fitness aurapavant
            cache = FitnessCache(eval)
            val cores = Runtime.getRuntime().availableProcessors()
            val ps = population.size

            val threads = mutableListOf<Thread>()
            val caches = mutableListOf<FitnessCache>()

            for (i in 0 until cores) {
                threads.add(thread {
                    val cache = FitnessCache(eval)
                    val range = if (i == cores - 1) {
                        ps * i / cores until ps
                    } else {
                        ps * i / cores until ps * (i + 1) / cores
                    }
                    population.sliceArray(range).forEach { cache.fitness(it) }
                    caches.add(cache)
                })
            }

            threads.forEach { it.join() }
            caches.forEach { it.share(cache) }
        }

        population.sortBy { - cache.fitness(it) }
        val newPop = Array<Genome?>(population.size, { null })

        for(i in 0 until config.elitism){
            newPop[i] = population[i]
        }

        val crossover = Crossover()
        speciate()

        if(show){
            println("best: ${cache(population[0])} (${population[0].hidden.size}) - worst : ${cache(population[population.size - 1])} (${population[population.size - 1].hidden.size})")
            print("members    : ") ; println(species.map { "${it.members.size} " })
            print("hidden     : ") ; println(population.map { "${it.hidden.size} " })
            print("stagnation : ") ; println(species.map{ "${it.stagnation}" })
            println()
        }

        species.removeAll { it.stagnation > config.max_stagnation || it.members.size == 0 }
        if(species.size == 0){
            return
        }

        val total = species.map(cache::fitness).sum()
        var offspring_count = 0
        val offspring = species.fold(listOf<Genome>()) { children, s ->
            val size = s.members.size
            val n = (cache.fitness(s) * population.size / total).toInt()
            val r = Random()

            s.members.sortBy(cache::fitness)
            val eliteSize = min(s.members.size, min(config.species_elitism, n))

            offspring_count += n

            children.plus(s.members.take(eliteSize)).plus(List(n - eliteSize, {
                val p1 = s.members[r.nextInt(size)]
                val p2 = s.members[r.nextInt(size)]
                if(cache(p1) > cache(p2)) crossover(p1, p2) else crossover(p2, p1)
            }))

        }.plus(List(max(0, config.pop_size - offspring_count), { // On complète avec des individus tirés au hasard
            val s = species[r.nextInt(species.size)]
            val size = s.members.size

            val p1 = s.members[r.nextInt(size)]
            val p2 = s.members[r.nextInt(size)]

            if(cache(p1) > cache(p2)) crossover(p1, p2) else crossover(p2, p1)
        }))
        .map(this::mutate)
        .sortedBy({ - cache(it) })


        val it = offspring.iterator()
        for(i in config.elitism until config.pop_size){
            population[i] = it.next()
        }

        species.forEach{ it.nextGen() }
    }

    private fun mutate(_p: Genome) : Genome {
        var p = _p

        val div1 = max(1.0, config.weight_replace_rate + config.weight_mutate_rate + config.bias_replace_rate + config.bias_mutate_rate)
        val nd1 = r.nextDouble()

        p = when {
            nd1 < (config.weight_replace_rate) / div1 -> ReplaceWeightMutation(config)(p)
            nd1 < (config.weight_replace_rate + config.weight_mutate_rate) / div1 -> EditWeightMutation(config)(p)
            nd1 < (config.weight_replace_rate + config.weight_mutate_rate + config.bias_replace_rate) / div1 -> ReplaceBiasMutation(config)(p)
            nd1 < (config.weight_replace_rate + config.weight_mutate_rate + config.bias_replace_rate + config.bias_mutate_rate) / div1 -> EditBiasMutation(config)(p)
            else -> p
        }

        val div2 = max(1.0, config.conn_add_prob + config.conn_delete_prob + config.node_add_prob + config.node_delete_prob)
        val nd2 = r.nextDouble()

        p =  when {
            nd2 < (config.conn_add_prob) / div2 -> AddConnectionMutationFF(config)(p)
            nd2 < (config.conn_add_prob + config.conn_delete_prob) / div2 -> RemoveConnectionMutation(config)(p)
            nd2 < (config.conn_add_prob + config.conn_delete_prob + config.node_add_prob ) / div2 -> AddNodeMutation(config)(p)
            nd2 < (config.conn_add_prob + config.conn_delete_prob + config.node_add_prob + config.node_delete_prob) / div2 -> DisableNodeMutation(config)(p)
            else -> p
        }

        return p
    }

    private fun speciate() {

        val distance = GenomeDistanceCache(config)
        species.forEach { it.members.clear() }
        population.forEach { g ->

            var flag = false
            for(s in species){
                if(distance(s.representative, g) < config.compatibility_threshold){
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

        species.removeAll { it.members.isEmpty() }
    }

    val score: Double
        get () {
            return population.map { FitnessCache(eval).fitness(it) }.max()!!
        }
}