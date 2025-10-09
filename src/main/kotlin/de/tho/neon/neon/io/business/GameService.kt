package de.tho.neon.neon.io.business

import org.springframework.stereotype.Service
import org.springframework.messaging.simp.SimpMessagingTemplate
import de.tho.neon.neon.io.business.GameSession
import de.tho.neon.neon.io.model.Lobby
import de.tho.neon.neon.io.business.GamePlayer

@Service
class GameService(
    private val messagingTemplate: SimpMessagingTemplate
) {

    val games: MutableMap<Long, GameSession> = mutableMapOf()

    var nextId: Long = 0

    fun newGame(lobby: Lobby): GameSession? {
        val id = nextId++
        val players: Map<Long, GamePlayer> = lobby.players.associate { 
            it.id to GamePlayer(it.id, it, 5.0, 5.0, 5.0) 
        }
        
        val map =  lobby.map
        if(map == null) {
            return null
        }

        val game = GameSession(id, 60, map, players, messagingTemplate)

        games.put(id, game)

        return game;

    }

    fun processMovement(gameId: Long, movement: GameMovement) {
        println("Sending movement to game" + games)
        games[gameId]?.movePlayer(movement)
    }
}