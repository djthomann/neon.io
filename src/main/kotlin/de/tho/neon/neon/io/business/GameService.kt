package de.tho.neon.neon.io.business

import org.springframework.stereotype.Service
import org.springframework.messaging.simp.SimpMessagingTemplate
import de.tho.neon.neon.io.model.Lobby
import kotlin.random.Random

@Service
class GameService(
    private val messagingTemplate: SimpMessagingTemplate
) {

    val games: MutableMap<Long, GameSession> = mutableMapOf()

    var nextId: Long = 0

    fun newGame(lobby: Lobby): GameSession? {
        val id = nextId++
        val map = lobby.map ?: return null

        val players: Map<Long, GamePlayer> = lobby.players.associate {
            var playerX = Random.nextInt(0, map.width)
            var playerZ = Random.nextInt(0, map.height)

            while(!map.getTileAt(playerX, playerZ)) {
                playerX = Random.nextInt(0, map.width)
                playerZ = Random.nextInt(0, map.height)
            }

            it.id to GamePlayer(it.id, it, playerX.toDouble(), 5.0, playerZ.toDouble())
        }



        val game = GameSession(id, 100, map, players, messagingTemplate)

        games.put(id, game)

        return game;

    }

    fun sendMovementToGame(gameId: Long, movement: GameMovement) {
        // println("Sending movement to game" + games)
        games[gameId]?.processPlayerMovement(movement)
    }

    fun sendAttackToGame(gameId: Long, attack: GameAttack) {
        // println("Sending attack to game" + games)
        games[gameId]?.processPlayerAttack(attack)
    }
}