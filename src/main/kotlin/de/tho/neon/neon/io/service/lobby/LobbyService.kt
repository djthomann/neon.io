package de.tho.neon.neon.io.service.lobby

import de.tho.neon.neon.io.model.Lobby

interface LobbyService {

    fun createLobby(): Lobby

    fun getLobbies(): List<Lobby>

    fun getLobby(id: Long): Lobby?

}