package de.tho.neon.neon.io.model

data class NeonMap(

    val id: Long,

    val name: String,

    val width: Int = 10,
    val height: Int = 10,

    var tiles: Array<Array<Boolean>> = Array(height) {
        Array(width) { false }
    }
) {

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