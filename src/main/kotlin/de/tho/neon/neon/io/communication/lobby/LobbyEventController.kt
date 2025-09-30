package de.tho.neon.neon.io.communication.lobby

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class LobbyEventController(
    private val messagingTemplate: SimpMessagingTemplate
) {

    @MessageMapping("/lobby/{lobbyId}/join")
    fun sendLobbyMessage(@DestinationVariable lobbyId: Long, event: LobbyJoinEvent) {
        messagingTemplate.convertAndSend("/topic/lobby/$lobbyId/join", event)
    }

}