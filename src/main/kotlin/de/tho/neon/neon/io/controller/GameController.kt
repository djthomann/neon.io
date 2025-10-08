package de.tho.neon.neon.io.controller

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import de.tho.neon.neon.io.business.GameService
import de.tho.neon.neon.io.business.GameMovement
import de.tho.neon.neon.io.business.GamePlayer

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
        gameService.processMovement(gameId, message)
    }

}