package neat.mutation

import neat.stucture.Genome

interface MutationInterface {
    operator fun invoke(g: Genome) : Genome
}

