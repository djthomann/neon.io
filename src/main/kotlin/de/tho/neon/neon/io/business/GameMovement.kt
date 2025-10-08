package de.tho.neon.neon.io.business

data class GameMovement(
    val playerId: Long,
    val direction: MovementType
)