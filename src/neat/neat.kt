package neat

import neat.stucture.Connection
import neat.stucture.Genome
import neat.stucture.Node
import neat.stucture.NodeType
import kotlin.math.abs


fun main(args: Array<String>) {

    val g = Genome(
        inputs= listOf(Node(id=-2, type= NodeType.INPUT, timeConstant = 1.0), Node(id=-1, type= NodeType.INPUT, timeConstant = 1.0)),
        hidden= listOf(Node(id=78, type= NodeType.HIDDEN, timeConstant = 1.0)),
        output= listOf(Node(id=0, type= NodeType.OUTPUT, timeConstant = 1.0)),
        connections= listOf(
                Connection(id=-1, weight=17.289520452941787, from=-1, to=0, enabled=true),
                Connection(id=0, weight=27.004651430072762, from=-2, to=0, enabled=true),
                Connection(id=180, weight=-18.721117259795925, from=78, to=0, enabled=true),
                Connection(id=181, weight=8.795612279000192, from=-2, to=78, enabled=true),
                Connection(id=198, weight=9.766505898431122, from=-1, to=78, enabled=true)
                //Connection(id=208, weight=-4.168425678380614, from=78, to=78, enabled=true),
                //Connection(id=561, weight=-8.227183506822566, from=0, to=78, enabled=true)
        )
    )

    val network = CTRNN(g)
    println(network.eval(arrayOf(0.0, 0.0), 10.0, 0.1)[0])
    println(network.eval(arrayOf(0.0, 1.0), 10.0, 0.1)[0])
    println(network.eval(arrayOf(1.0, 0.0), 10.0, 0.1)[0])
    println(network.eval(arrayOf(1.0, 1.0), 10.0, 0.1)[0])

}

fun trainingTest() {
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