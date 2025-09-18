package de.tho.neon.neon.io.model

data class Lobby(
    val id: Long,
    val owner: Player?,
    val capacity: Int,
    val players: MutableList<Player>,
    var map: NeonMap?
)