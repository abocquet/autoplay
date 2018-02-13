package neat

import neat.stucture.Genome
import neat.stucture.NodeType
import java.lang.Integer.max
import kotlin.math.abs

class Species(val representative: Genome, generation: Int) {
    val created = generation
    val last_improved = generation
    var members = mutableListOf<Genome>()
    val fitness_history = mutableListOf<Int>()
}

class GenomeDistanceCache {

    private val distances = mutableMapOf<Pair<Genome, Genome>, Double>()

    operator fun invoke(g1: Genome, g2: Genome) : Double {
        var distance = distances[Pair(g1, g2)]
        if(distance == null){
            //Compute node gene distance
            var nodeDistance = 0.0

            var disjointNodes = 0
            g1.nodes.forEach { if(it !in g2.nodes){ disjointNodes++ } }

            for(n2 in g2.nodes){
                val search = g1.nodes.filter { it.id == n2.id } // TODO: could be more efficient throught dichotomic search since nodes are sorted by id
                if(search.isEmpty()){
                    disjointNodes++
                } else {
                    val n1 = search[0]

                    val c1 = g1.connections.filter { it.to == n1.id && g1.nodes.firstOrNull { n -> n.type == NodeType.BIAS }?.id == it.from }
                    val v1 : Double = if(c1.isEmpty()){ 0.0 } else { c1[0].weight }

                    val c2 = g2.connections.filter { it.to == n2.id && g2.nodes.firstOrNull { n -> n.type == NodeType.BIAS }?.id == it.from }
                    val v2 : Double = if(c2.isEmpty()) { 0.0 } else { c2[0].weight }

                    nodeDistance += abs(v1 - v2)
                    if(n1.activation != n2.activation){
                        nodeDistance += 1
                    }
                }
            }

            val maxNodes = max(g1.nodes.size, g2.nodes.size)
            nodeDistance = (nodeDistance * Config.compatibility_weight_coefficient + (Config.compatibility_disjoint_coefficient * disjointNodes)) / maxNodes

            //Compute connection gene difference
            var connectionDistance = 0.0
            var disjointConnections = 0

            g2.connections.forEach { c2 ->
                val c1 = g1.connections.find { c1 -> c1.from == c2.from && c1.to == c2.to }
                if(c1 == null){
                    disjointConnections += 1
                } else {
                    connectionDistance += abs(c1.weight - c2.weight)
                }
            }

            g1.connections.forEach { c1 ->
                val c2 = g2.connections.find { c2 -> c2.from == c1.from && c1.to == c2.to }
                if(c2 == null){
                    disjointConnections += 1
                }
            }

            val maxConn = max(g1.connections.size, g2.connections.size)
            connectionDistance = (connectionDistance + (Config.compatibility_disjoint_coefficient * disjointConnections)) / maxConn

            distance = nodeDistance + connectionDistance
        }

        return distance
    }
}
