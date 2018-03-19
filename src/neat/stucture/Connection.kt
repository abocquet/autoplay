package neat.stucture

import java.io.Serializable


data class Connection (
        val id: Int,
        var weight: Double,
        val from: Int,
        val to: Int,
        val enabled: Boolean
) : Serializable {
    companion object {
        var innovation = 1
            private set
            get () { return field++ }
    }
}
