package physics

data class Vector(var x: Double = 0.0, var y: Double = 0.0) {
    constructor(v: Vector) : this() {
        this.x = v.x
        this.y = v.y
    }

    override fun toString(): String {
        return "Vector(x=$x, y=$y)"
    }

    operator fun times(lambda: Double): Vector {
        return Vector(x * lambda, y * lambda)
    }

    operator fun plusAssign(vector: Vector) {
        this.x += vector.x
        this.y += vector.y
    }

    fun add(x: Double, y: Double): Vector {
        return Vector(this.x + x, this.y + y)
    }
}

val Vector.normeSquare: Double
    get() {
        return x * x + y * y
    }
