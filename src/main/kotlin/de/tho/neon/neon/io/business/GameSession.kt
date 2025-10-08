package de.tho.neon.neon.io.business

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.ScheduledFuture
import org.springframework.messaging.simp.SimpMessagingTemplate
import de.tho.neon.neon.io.business.GamePlayer
import de.tho.neon.neon.io.business.GamePosition
import de.tho.neon.neon.io.business.MovementType

class GameSession(
    val id: Long,
    val length: Long,
    val players: Map<Long, GamePlayer>,
    private val messagingTemplate: SimpMessagingTemplate
) {
    
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private var future: ScheduledFuture<*>? = null

    fun movePlayer(movement: GameMovement) {
        val player = players[movement.playerId]

        if(player == null) {
            return
        }

        when(movement.direction) {
            MovementType.FORWARD -> player.x += 1
            MovementType.BACKWARD -> player.x -= 1
            MovementType.RIGHT -> {
                println("MOVING RIGHT")
                player.z += 1
            }
            MovementType.LEFT -> player.z -= 1
            MovementType.UP -> player.y += 1
            MovementType.DOWN -> player.y -= 1
        }
    }

    fun startLoop() {
        future = executor.scheduleAtFixedRate(
            { tick() },
            0,
            30,
            TimeUnit.MILLISECONDS
        )

        println(players)

        executor.schedule({
            stopLoop()
        }, length, TimeUnit.SECONDS)
    }

    private fun stopLoop() {
        future?.cancel(false)
        println("Game $id: finished.")
        executor.shutdown()
    }

    private fun tick() {
        println("Game $id: TICK!")
        for((_, player) in players) {
            sendPosition(player)
        }
    }

    fun sendPosition(player: GamePlayer) {
        messagingTemplate.convertAndSend(
            "/topic/game/$id",
            GamePosition(player.id, player.x, player.y, player.z)
        )
    }

}