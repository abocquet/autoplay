package neat


fun main(args: Array<String>){

    val g = Genome(2, 1)

    println(g.connections)
    println(g.nodes)

    val neural = CTRNN(g)
    println(neural.eval(arrayOf(0.0, 10.0), 10.0, 0.1).contentDeepToString())

}