package de.tho.neon.neon.io.repository

import de.tho.neon.neon.io.model.Lobby
import org.springframework.stereotype.Repository

@Repository
class LobbyRepository {

    var nextId: Long = 1

    val lobbies = mutableMapOf<Long, Lobby>()

    fun addLobby(): Lobby {
        val id = nextId
        val lobby = Lobby(id, 8, emptyList())
        lobbies.put(id, lobby)
        nextId++
        return lobby
    }

    fun getLobby(id: Long): Lobby? {
        return lobbies[id]
    }

    fun getLobbies(): List<Lobby> {
        return lobbies.values.toList()
    }

}