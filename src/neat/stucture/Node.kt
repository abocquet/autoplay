package neat.stucture

import java.io.Serializable
import kotlin.math.exp


enum class NodeType {
    INPUT, HIDDEN, OUTPUT
}

data class Node(
        val id: Int,
        val timeConstant: Double,
        val type: NodeType = NodeType.HIDDEN,
        val enabled: Boolean = true,
        val bias: Double = 0.0
) : Serializable {
    val activation = fun(x: Double): Double { return 1 / (1 + exp(-x))}

    override fun toString(): String {
        return "Node(id=$id, type=$type)"
    }

    companion object {
        var innovation = 1
            private set
            get () { return field++ }
    }
}