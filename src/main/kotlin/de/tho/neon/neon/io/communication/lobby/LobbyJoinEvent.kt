package de.tho.neon.neon.io.communication.lobby

data class LobbyJoinEvent(
    val playerId: Long,
    val name: String
)