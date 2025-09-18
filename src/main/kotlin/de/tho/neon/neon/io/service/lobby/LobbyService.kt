package de.tho.neon.neon.io.service.lobby

import de.tho.neon.neon.io.model.Lobby

interface LobbyService {

    fun createLobby(ownerId: Long?): Lobby

    fun deleteLobby(id: Long): Result<String>

    fun getLobbies(): List<Lobby>

    fun getLobby(id: Long): Lobby?

    fun joinLobby(playerId: Long, lobbyId: Long): Result<String>

    fun leaveLobby(playerId: Long, lobbyId: Long): Result<String>

    fun setMap(lobbyId: Long, mapId: Long): Result<String>

}