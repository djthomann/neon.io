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

        val vac = movementToVec(movement.direction)

        val newPos = Vec3(player.x + vac.x, player.y + vac.y, player.z +vac.z)

        if(!(newPos.y > 0)) {
            return
        }

        if(!map.positionInBounds(newPos.x, newPos.z)) {
            return   
        }


        player.x = newPos.x
            player.y = newPos.y
            player.z = newPos.z
    }

    fun movementToVec(direction: MovementType): Vec3 {
        return when(direction) {
            MovementType.FORWARD -> Vec3(x = 1)
            MovementType.BACKWARD -> Vec3(x = -1)
            MovementType.RIGHT -> Vec3(z = 1)
            MovementType.LEFT -> Vec3(z = -1)
            MovementType.UP -> Vec3(y = 1)
            MovementType.DOWN -> Vec3(y = -1)
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