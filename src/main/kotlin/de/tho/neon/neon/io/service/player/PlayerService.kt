package de.tho.neon.neon.io.service.player

import de.tho.neon.neon.io.model.Player

interface PlayerService {

    fun getPlayers(): List<Player>

    fun getPlayer(id: Long): Player?

    fun createPlayer(name: String): Player

}