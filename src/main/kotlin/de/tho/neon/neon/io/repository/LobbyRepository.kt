package de.tho.neon.neon.io.repository

import de.tho.neon.neon.io.model.Lobby
import de.tho.neon.neon.io.model.Player
import org.springframework.stereotype.Repository

@Repository
class LobbyRepository {

    var nextId: Long = 1

    val lobbies = mutableMapOf<Long, Lobby>()

    fun addLobby(owner: Player?): Lobby {
        val id = nextId
        val lobby = Lobby(id, owner,8, mutableListOf())
        lobbies.put(id, lobby)
        nextId++
        return lobby
    }

    fun removeLobby(id: Long): Boolean {
        return lobbies.remove(id) != null
    }

    fun getLobby(id: Long): Lobby? {
        return lobbies[id]
    }

    fun getLobbies(): List<Lobby> {
        return lobbies.values.toList()
    }

}