
package de.tho.neon.neon.io.business

import de.tho.neon.neon.io.model.Player

data class GamePlayer(
    val id: Long,
    val user: Player,
    var x: Double,
    var y: Double,
    var z: Double,
    var radius: Double = 0.5,
    val height: Double = 1.0,
    var hitTime: Long? = null,
    var lastShotTime: Long? = null,
    var vy: Double = 0.0
)

fun GamePlayer.positionInBounds(pos: Vector3): Boolean {
    val dx = pos.x - x
    val dz = pos.z - z

    val distSq = dx * dx + dz * dz

    return distSq <= radius * radius
}