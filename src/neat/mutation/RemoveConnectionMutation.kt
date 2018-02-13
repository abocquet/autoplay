package neat.mutation

import neat.stucture.Genome
import java.util.*

class RemoveConnectionMutation : MutationInterface {
    override operator fun invoke(g: Genome) : Genome {
        val r = Random()
        val connection = g.connections[r.nextInt(g.connections.size)]
        return g.copy(connections = g.connections.map {
            if(it.id == connection.id){ it.copy(enabled = false) } else { it }
        })
    }
}