package neat

import neat.stucture.Connection
import neat.stucture.Genome
import neat.stucture.Node
import kotlin.test.fail

class Crossover {

    private fun crossoverNodes(l1: List<Node>, l2: List<Node>) : List<Node>{
        if(l1.isEmpty()){
            return l2
        } else if (l2.isEmpty()){
            return l1
        } else {
            val h1 = l1.first() ; val h2 = l2.first()

            if(h1.id == h2.id){
                return listOf(h1).plus(crossoverNodes(l1.drop(1), l2.drop(1)))
            } else if(h1.id < h2.id) {
                return listOf(h1).plus(crossoverNodes(l1.drop(1), l2))
            } else if(h1.id > h2.id) {
                return listOf(h2).plus(crossoverNodes(l1, l2.drop(1)))
            }
        }

        fail("Can't happen")
    }

    private fun crossoverConnections(l1: List<Connection>, l2: List<Connection>) : List<Connection>{
        if(l1.isEmpty()){
            return l2
        } else if (l2.isEmpty()){
            return l1
        } else {
            val h1 = l1.first() ; val h2 = l2.first()

            if(h1.id == h2.id){
                return listOf(h1).plus(crossoverConnections(l1.drop(1), l2.drop(1)))
            } else if(h1.id < h2.id) {
                return listOf(h1).plus(crossoverConnections(l1.drop(1), l2))
            } else if(h1.id > h2.id) {
                return listOf(h2).plus(crossoverConnections(l1, l2.drop(1)))
            }
        }

        fail("Can't happen")
    }

    /**
     * g1 is supposed to have a better fitness than g2
     */
    operator fun invoke(g1: Genome, g2: Genome): Genome{

        // Check input data
        assert(g1.nodes.zipWithNext { n1, n2 -> n1.id < n2.id }.all { it }) // Check that every node is sorted by id
        assert(g1.connections.zipWithNext { c1, c2 -> c1.id < c2.id }.all { it }) // Check that every connection is sorted by id

        assert(g2.nodes.zipWithNext { n1, n2 -> n1.id < n2.id }.all { it })
        assert(g2.connections.zipWithNext { c1, c2 -> c1.id < c2.id }.all { it })

        assert(g1.inputs == g2.inputs && g1.output == g2.output)

        // We cross nodes
        val hidden = crossoverNodes(g1.hidden, g2.hidden)

        // Cross connections
        val connections = crossoverConnections(g1.connections, g2.connections)

        return Genome(g1.inputs, hidden.toList(), g1.output, connections)
    }

}