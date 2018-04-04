package neat

import neat.mutation.AddConnectionMutationFF
import neat.neural.CTRNN
import neat.neural.FeedForward
import neat.stucture.Connection
import neat.stucture.Genome
import neat.stucture.Node
import neat.stucture.NodeType
import kotlin.math.abs


fun main(args: Array<String>) {
    trainingTest()
}

fun trainingTest() {
    val exam = arrayOf(
            Pair(arrayOf(0.0, 0.0), 0.0),
            Pair(arrayOf(1.0, 0.0), 1.0),
            Pair(arrayOf(0.0, 1.0), 1.0),
            Pair(arrayOf(1.0, 1.0), 0.0)
    )

    val population = Population(2, 1, { g ->
        val nn = CTRNN(g, 3.0, .1)
        exam.map { 1 - (abs(nn.eval(it.first)[0] - it.second)) }.sum() - 0.01 * g.hidden.size
    })

    while (population.score < population.config.fitness_threshold) {
        population.evolve(100, async = false)
        population.evolve(true)
    }

    println(population.results)
}

fun cyclicTest(){
    val inputs = listOf(
            Node(-2, 1.0, NodeType.INPUT),
            Node(-1, 1.0, NodeType.INPUT)
    )

    val outputs = listOf(
            Node(4, 1.0, NodeType.OUTPUT)
    )

    val c1 = listOf(
        //Connection(1, 1.0, -2, -1, true),
        Connection(2, 1.0, -1, 0, true),
        //Connection(3, 1.0, -2, -1, true),
        //Connection(4, 1.0, -3, -2, true),
        //Connection(5, 1.0, -4, -3, true),
        //Connection(6, 1.0, 0, -4, true)

        Connection(3, 1.0, -2, 0, true)
    )

    val genome = Genome(inputs, listOf(), outputs, c1)
    val ff = FeedForward(genome)
    val mut = AddConnectionMutationFF(Config())
    println(mut.isAcyclic(genome, 0))
    println(ff.eval(arrayOf(3.0, 7.0)).contentDeepToString())
}