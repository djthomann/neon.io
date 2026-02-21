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

        val attackTime = System.currentTimeMillis()
        val playerShooting = players[attack.playerId] ?: return

        if(playerShooting.hitTime != null) {
            return
        }

        if(playerShooting.lastShotTime != null) {
            return
        }

        // player exists and isn't hit and isn't on cooldown
        playerShooting.lastShotTime = attackTime

        val laser = GameLaser(
            shotAt = System.currentTimeMillis(),
            origin = Vector3(playerShooting.x, playerShooting.y, playerShooting.z),
            direction = attack.vector.normalize()
        )

        lasers.add(laser)
        // println("ADDED LASER")

        for ((_, player) in players) {
            if(playerShooting != player) {
                val dist = laser.hitDistance2D(player)

                if(dist != null) {
                    println("HIT")
                    if (player.hitTime == null) {
                        player.hitTime = attackTime
                    }
                } else {
                    println("NOT HIT")
                }
            }
        }

    }
    fun processPlayerMovement(movement: GameMovement) {
        val player = players[movement.playerId]

        if(player == null) {
            return
        }

        val vec = movement.vector

        val newPos = Vector3(player.x + vec.x * 0.1, player.y + vec.y * 0.1, player.z + vec.z * 0.1)

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

        for((_, p) in players) {
            if(p != player && p.positionInBounds(newPos)) {
                return
            }
        }

        if(map.bodyInWalls(newPos.x, newPos.z, player.radius)) {
            println("BODY IN WALLS: $newPos")
            return
        }

        // println("NEW POSITION: $newPos")
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
            updateHitState(tickTime, player)
            updateShootCooldown(tickTime, player)
            applyGravity(player)
            sendPlayerInfo(player)
        }

        lasers =  lasers.filter { laser ->  tickTime - laser.shotAt < 5000 }.toMutableList()

        sendLasers(lasers)
    }

    private fun updateHitState(tickTime: Long, player: GamePlayer) {

        val playerHitTime = player.hitTime ?: return

        // Check if player is okay again
        if(tickTime - playerHitTime > 5000) {
            player.hitTime = null
        } else {
            println("STILL HIT")
        }
    }

    private fun updateShootCooldown(tickTime: Long, player: GamePlayer) {

        val playerLastShot = player.lastShotTime ?: return

        // Check if player is okay again
        if(tickTime - playerLastShot > 200) {
            player.lastShotTime = null
        } else {
            println("STILL ON COOLDOWN")
        }

    }

    private fun sendLasers(lasers: List<GameLaser>) {

        // println("Sending laser: $lasers")

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

    fun sendPlayerInfo(player: GamePlayer) {

        println("PLAYER INFO: $player")

        messagingTemplate.convertAndSend(
            "/topic/game/$id/player-info",
            PlayerInfo(player.id, player.x, player.y, player.z, player.hitTime != null)
        )
    }

    fun sendTime() {

        val elapsedTimeSeconds = (System.currentTimeMillis() - startTime) / 1000
        val remainingTime = length - elapsedTimeSeconds
        
        // println("Game $id: Time remaining: $remainingTime")

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