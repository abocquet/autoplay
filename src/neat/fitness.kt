package neat

import neat.stucture.Genome
import java.io.Serializable

class FitnessCache(private val eval: (Genome) -> Double) : Serializable{

    private val cache = hashMapOf<Genome, Double>()
    operator fun invoke(g: Genome): Double { return fitness(g) }

    fun fitness(g: Genome): Double {
        if(!cache.containsKey(g) || cache[g] == null){
            cache[g] = eval(g)
        }

        return cache[g]!!
    }

    fun fitness(s: Species): Double{
        return s.members.map(this::fitness).average()
    }

    fun add(l: List<Genome>){
        l.forEach { fitness(it) }
    }

    fun share(other: FitnessCache){
        other.cache.putAll(this.cache)
    }

}