package de.tho.neon.neon.io.controller

import de.tho.neon.neon.io.business.GameAttack
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import de.tho.neon.neon.io.business.GameService
import de.tho.neon.neon.io.business.GameMovement

@Controller
class GameController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val gameService: GameService
) {

    @MessageMapping("/game/{gameId}/move")
    fun receiveMovement(
        @DestinationVariable gameId: Long,
        @Payload message: GameMovement
    ) {
        println("Received movement for game $gameId: $message")
        gameService.sendMovementToGame(gameId, message)
    }

    @MessageMapping("/game/{gameId}/attack")
    fun receiveAttack(
        @DestinationVariable gameId: Long,
        @Payload message: GameAttack
    ) {
        println("Received attack for game $gameId: $message")
        gameService.sendAttackToGame(gameId, message)
    }

}