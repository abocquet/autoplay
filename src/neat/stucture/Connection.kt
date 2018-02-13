package neat.stucture


data class Connection (
        val id: Int,
        var weight: Double,
        val from: Int,
        val to: Int,
        val enabled: Boolean
) {
    companion object {
        var innovation = 1
            private set
            get () { return field++ }
    }
}
