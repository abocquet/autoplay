package neat.mutation

import neat.stucture.Genome
import java.util.*

class DisableNodeMutation : MutationInterface {

    override operator fun invoke(g: Genome) : Genome {
        if(g.hidden.isEmpty()){
            return g
        }

        val r = Random()
        val node = g.hidden[r.nextInt(g.hidden.size)]

        val new = node.copy(enabled = false)
        return g.copy(hidden = g.hidden.map { if(it.id == new.id) new else it })
    }
}