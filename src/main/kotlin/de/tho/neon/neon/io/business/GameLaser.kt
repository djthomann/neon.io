package de.tho.neon.neon.io.business

data class GameLaser(
    val shotAt: Long,
    val origin: Vector3,
    val direction: Vector3
)