package de.tho.neon.neon.io.business

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.ScheduledFuture
import org.springframework.messaging.simp.SimpMessagingTemplate
import de.tho.neon.neon.io.business.GamePlayer
import de.tho.neon.neon.io.business.GamePosition
import de.tho.neon.neon.io.business.MovementType
import de.tho.neon.neon.io.model.NeonMap

class GameSession(
    val id: Long,
    val length: Long,
    val map: NeonMap,
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

        val vec = movement.vector

        val newPos = Vector3(player.x + vec.x * 0.2, player.y + vec.y * 0.2, player.z + vec.z * 0.2)

        if(!(newPos.y > 0)) {
            return
        }

        if(!map.positionInBounds(newPos.x, newPos.z)) {
            return   
        }

        
        if(map.positionInWalls(newPos.x, newPos.z)) {
            return
        }

        player.x = newPos.x
        player.y = newPos.y
        player.z = newPos.z
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