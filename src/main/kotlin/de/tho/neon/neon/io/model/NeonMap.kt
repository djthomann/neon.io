package de.tho.neon.neon.io.model

data class NeonMap(

    val id: Long,

    val name: String,

    val width: Int = 10, // x
    val height: Int = 10, // z

    var tiles: Array<Array<Boolean>> = Array(height) {
        Array(width) { false }
    }
) {

    fun getTileAt(x: Int, z: Int): Boolean {
        return tiles[z][x]
    }

    fun positionInBounds(x: Double, z: Double): Boolean {
        return x >= 0.0 && x <= width && z >= 0.0 && z <= height
    }

    // TODO() Rename this function
    fun positionInWalls(x: Double, z: Double): Boolean {
        return getTileAt(x.toInt(), z.toInt())
    }

    // TODO() Implement this function
    fun bodyInWalls(x: Double, y: Double, z: Double): Boolean {
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NeonMap

        if (!tiles.contentDeepEquals(other.tiles)) return false

        return true
    }

    override fun hashCode(): Int {
        return tiles.contentDeepHashCode()
    }
}