package neat

import neat.stucture.Genome

class FitnessCache(private val eval: (Genome) -> Double) {

    private val cache = hashMapOf<Genome, Double>()

    fun fitness(g: Genome): Double {
        if(!cache.containsKey(g)){
            cache[g] = eval(g)
        }

        return cache[g]!!
    }

    fun fitness(s: Species): Double{
        return s.members.map(this::fitness).sum()
    }

    fun add(l: List<Genome>){
        l.forEach { fitness(it) }
    }

}