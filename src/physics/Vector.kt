package physics

class Vector(_x: Double = 0.0, _y: Double = 0.0) {

    var x = _x
    var y = _y

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

    fun normeSquare(): Double {
        return x * x + y * y
    }
}