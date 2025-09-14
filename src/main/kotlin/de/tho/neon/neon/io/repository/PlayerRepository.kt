package de.tho.neon.neon.io.repository

import de.tho.neon.neon.io.model.Player
import org.springframework.stereotype.Repository

@Repository
class PlayerRepository {

    var nextId: Long = 1

    val players = mutableMapOf<Long, Player>()

    fun addPlayer(name: String): Player {
        val id: Long = nextId
        val player = Player(id, name)
        players.put(id, player)
        nextId++
        return player
    }

    fun getPlayer(id: Long): Player? {
        return players[id]
    }

    fun getPlayers(): List<Player> {
        return players.values.toList()
    }

}