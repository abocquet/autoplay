package neat

import kotlin.math.abs


fun main(args: Array<String>){

    val exam = arrayOf(
            Pair(arrayOf(0.0, 0.0), 0.0),
            Pair(arrayOf(1.0, 0.0), 1.0),
            Pair(arrayOf(0.0, 1.0), 1.0),
            Pair(arrayOf(1.0, 1.0), 0.0)
    )

    val population = Population(2, 1, { g ->
        val nn = CTRNN(g)
        exam.map { 1 - (abs(nn.eval(it.first, 10.0, .1)[0] - it.second)) }.sum() - 0.01 * g.hidden.size
    })

    while (population.score < population.config.fitness_threshold) {
        population.evolve(100)
        population.evolve(true)
    }

    println(population.results)

    /*val g = Genome(2, 1)

    println(g.connections)
    println(g.nodes)

    val neural = CTRNN(g)
    println(neural.eval(arrayOf(0.0, 10.0), 10.0, 0.1).contentDeepToString())*/

    /*val inputs = listOf(
        Node(-4, 1.0, NodeType.BIAS),
        Node(-3, 1.0, NodeType.INPUT),
        Node(-2, 1.0, NodeType.INPUT),
        Node(-1, 1.0, NodeType.INPUT)
    )

    val p1h = listOf(
        Node(5, 1.0)
    )

    val p2h = listOf(
        Node(5, 1.0),
        Node(6, 1.0)
    )

    val outputs = listOf(
        Node(4, 1.0, NodeType.OUTPUT)
    )

    val c1 = listOf(
        Connection(1, 1.0, 1, 4, true),
        Connection(2, 1.0, 2, 4, false),
        Connection(3, 1.0, 3, 4, true),
        Connection(4, 1.0, 2, 5, true),
        Connection(5, 1.0, 5, 4, true),
        Connection(8, 1.0, 1, 5, true)
    )

    val c2 = listOf(
        Connection(1, 1.0, 1, 4, true),
        Connection(2, 1.0, 2, 4, false),
        Connection(3, 1.0, 3, 4, true),
        Connection(4, 1.0, 2, 5, true),
        Connection(5, 1.0, 5, 4, false),
        Connection(6, 1.0, 5, 6, true),
        Connection(7, 1.0, 6, 4, true),
        Connection(9, 1.0, 3, 5, true),
        Connection(10, 1.0, 1, 6, true)
    )

    val p1 = Genome(inputs, p1h, outputs, c1)
    val p2 = Genome(inputs, p2h, outputs, c2)*/

    //val child = Crossover()(p1, p2)
    //print(child)

}