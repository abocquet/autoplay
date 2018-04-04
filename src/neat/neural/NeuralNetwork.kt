package neat.neural

interface NeuralNetwork {
    fun eval(input_values: Array<Double>): Array<Double>
}