package de.tho.neon.neon.io.business

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.ScheduledFuture
import org.springframework.messaging.simp.SimpMessagingTemplate
import de.tho.neon.neon.io.model.NeonMap
import de.tho.neon.neon.io.communication.game.GameEndEvent

class GameSession(
    val id: Long,
    val length: Long = 10,
    val map: NeonMap,
    val players: Map<Long, GamePlayer>,
    private val messagingTemplate: SimpMessagingTemplate
) {
    
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private var future: ScheduledFuture<*>? = null

    private var startTime: Long = 0

    private val tickInterval: Long = 30
    private val gravity: Double = -5.0 / tickInterval
    private val jumpVelocity: Double = 25.0 / tickInterval
    private val groundLevel = 0.0

    private var lasers = mutableListOf<GameLaser>()

    fun processPlayerAttack(attack: GameAttack) {
        val player = players[attack.playerId]

        if(player != null) {
            lasers.add(
                GameLaser(
                    shotAt = System.currentTimeMillis(),
                    origin = Vector3(player.x, player.y, player.z),
                    direction = attack.vector
                )
            )
            println("ADDED LASER")
        }

    }
    fun processPlayerMovement(movement: GameMovement) {
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

        startTime = System.currentTimeMillis()

        future = executor.scheduleAtFixedRate(
            { tick() },
            0,
            tickInterval,
            TimeUnit.MILLISECONDS
        )

        executor.scheduleAtFixedRate(
            { sendTime() }, 
            0, 
            1000, 
            TimeUnit.MILLISECONDS
        )

        executor.schedule({
            stopLoop()
        }, length, TimeUnit.SECONDS)
    }

    private fun stopLoop() {
        future?.cancel(false)
        println("Game $id: finished.")
        executor.shutdown()
        sendGameFinished()
    }

    private fun tick() {

        val tickTime = System.currentTimeMillis()

        // println("Game $id: TICK!")
        for((_, player) in players) {
            applyGravity(player)
            sendPosition(player)
        }

        lasers =  lasers.filter { laser ->  tickTime - laser.shotAt < 5000 }.toMutableList()

        sendLasers(lasers)
    }

    private fun sendLasers(lasers: List<GameLaser>) {

        println("Sending laser: $lasers")

        messagingTemplate.convertAndSend(
            "/topic/game/$id/lasers",
            lasers
        )
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
            "/topic/game/$id/position",
            GamePosition(player.id, player.x, player.y, player.z)
        )
    }

    fun sendTime() {

        val elapsedTimeSeconds = (System.currentTimeMillis() - startTime) / 1000
        val remainingTime = length - elapsedTimeSeconds
        
        println("Game $id: Time remaining: $remainingTime")

        messagingTemplate.convertAndSend(
            "/topic/game/$id/time",
            GameTime(remainingTime)
        )

    }

    fun sendGameFinished() {
        val gameEndEvent = GameEndEvent(id)
        messagingTemplate.convertAndSend("/topic/game/$id/end", gameEndEvent)
    }

}