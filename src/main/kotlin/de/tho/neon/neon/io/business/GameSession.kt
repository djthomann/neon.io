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
    val length: Long = 60,
    val map: NeonMap,
    val players: Map<Long, GamePlayer>,
    private val messagingTemplate: SimpMessagingTemplate
) {
    
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private var future: ScheduledFuture<*>? = null

    private val tickInterval: Long = 30
    private val gravity: Double = -5.0 / tickInterval
    private val jumpVelocity: Double = 25.0 / tickInterval
    private val groundLevel = 0.0

    fun movePlayer(movement: GameMovement) {
        val player = players[movement.playerId]

        if(player == null) {
            return
        }

        val vec = movement.vector

        val newPos = Vector3(player.x + vec.x * 0.2, player.y + vec.y * 0.2, player.z + vec.z * 0.2)

        if(!(newPos.y >= 0)) {
            println("POSITION UNDERGROUND: $newPos")
            return
        }

        if(!map.positionInBounds(newPos.x, newPos.z)) {
            println("POSITION NOT IN BOUNDs: $newPos")
            return   
        }

        
        if(map.positionInWalls(newPos.x, newPos.z)) {
            println("POSITION NOT IN WALLS: $newPos")
            return
        }

        println("NEW POSITION: $newPos")
        player.x = newPos.x
        player.z = newPos.z

        if(vec.y > 0 && player.y == 0.0) {
            player.vy = jumpVelocity
        }
    }

    fun startLoop() {
        future = executor.scheduleAtFixedRate(
            { tick() },
            0,
            tickInterval,
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
            applyGravity(player)
            sendPosition(player)
        }
    }

    private fun applyGravity(player: GamePlayer) {
        
        player.vy += gravity
        player.y += player.vy
    
        // Ground Collision
        if (player.y <= groundLevel) {
            player.y = groundLevel
            player.vy = 0.0
        }
    }

    fun sendPosition(player: GamePlayer) {
        messagingTemplate.convertAndSend(
            "/topic/game/$id",
            GamePosition(player.id, player.x, player.y, player.z)
        )
    }

}