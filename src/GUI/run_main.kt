package GUI

import neat.Population
import kotlin.math.abs


fun main(args: Array<String>) {

    val population = Population(5, 3, { abs(3 - it.hidden.size).toDouble() })
    val frame = MainFrame(population)

}
