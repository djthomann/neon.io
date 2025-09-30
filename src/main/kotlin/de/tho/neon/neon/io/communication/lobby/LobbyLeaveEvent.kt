package de.tho.neon.neon.io.communication.lobby

data class LobbyLeaveEvent(
    val playerId: Long,
    val name: String
)