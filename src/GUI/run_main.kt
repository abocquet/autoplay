package GUI

import neat.CTRNN
import neat.Population
import kotlin.math.abs


fun main(args: Array<String>) {

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

    val frame = MainFrame(population)

}
