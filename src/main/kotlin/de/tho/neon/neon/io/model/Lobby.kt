package de.tho.neon.neon.io.model

data class Lobby(
    val id: Long,
    val capacity: Int,
    val players: List<Player>
)