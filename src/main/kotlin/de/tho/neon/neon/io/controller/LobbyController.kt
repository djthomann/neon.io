package de.tho.neon.neon.io.controller

import de.tho.neon.neon.io.business.GameService
import de.tho.neon.neon.io.business.GameSession
import de.tho.neon.neon.io.communication.game.GameStartEvent
import de.tho.neon.neon.io.communication.lobby.LobbyDeletedEvent
import de.tho.neon.neon.io.communication.lobby.LobbyJoinEvent
import de.tho.neon.neon.io.communication.lobby.LobbyLeaveEvent
import de.tho.neon.neon.io.communication.lobby.LobbyMapChosenEvent
import de.tho.neon.neon.io.model.Lobby
import de.tho.neon.neon.io.service.lobby.LobbyService
import de.tho.neon.neon.io.service.neonmap.NeonMapService
import de.tho.neon.neon.io.service.player.PlayerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import LobbiesStateEvent

@RestController
@RequestMapping("/api/lobbies")
class LobbyController {

    @Autowired
    private lateinit var gameService: GameService

    @Autowired
    private lateinit var neonMapService: NeonMapService

    @Autowired
    private lateinit var playerService: PlayerService

    @Autowired
    lateinit var messagingTemplate: SimpMessagingTemplate
    @Autowired
    lateinit var lobbyService: LobbyService

    @PostMapping("/create")
    fun postNewLobby(@RequestParam(required = false) ownerId: Long?): Lobby {

        val newLobby = lobbyService.createLobby(ownerId)

        val lobbiesStateEvent = LobbiesStateEvent(lobbyService.getLobbies())
        messagingTemplate.convertAndSend("/topic/lobbies", lobbiesStateEvent)

        return newLobby
    }

    @PostMapping("{id}/delete")
    fun deleteLobby(@PathVariable("id") id: Long): ResponseEntity<String> {

        val lobby = lobbyService.getLobby(id)
        if(lobby == null) {
            return ResponseEntity.badRequest().body("Lobby not found")
        }

        val result = lobbyService.deleteLobby(id)

        if(result.isSuccess) {

            val deletedEvent = LobbyDeletedEvent(id)
            messagingTemplate.convertAndSend("/topic/lobby/${id}/delete", deletedEvent)
            sendLobbiesState()

            return ResponseEntity.ok(result.getOrNull().toString())
        } else {
            return ResponseEntity.badRequest().body(result.exceptionOrNull()?.message ?: "Unknown error")
        }

    }

    @GetMapping("")
    fun getLobbies(): List<Lobby> {
        return lobbyService.getLobbies()
    }

    @GetMapping("{id}")
    fun getLobby(@PathVariable("id") id: Long): Lobby? {
        return lobbyService.getLobby(id)
    }

    @PostMapping("/{lobbyId}/join")
    fun postJoinLobby(@PathVariable lobbyId: Long, @RequestParam playerId: Long): ResponseEntity<String> {

        val player = playerService.getPlayer(playerId)
        if(player == null) {
            return ResponseEntity.badRequest().body("Player not found")
        }
        val result = lobbyService.joinLobby(playerId, lobbyId)

        if(result.isSuccess) {

            val joinEvent = LobbyJoinEvent(playerId, player.name)
            messagingTemplate.convertAndSend("/topic/lobby/${lobbyId}/join", joinEvent)
            sendLobbiesState()
            
            return ResponseEntity.ok(result.getOrNull().toString())
        } else {
            return ResponseEntity.badRequest().body(result.exceptionOrNull()?.message ?: "Unknown error")
        }

    }

    @PostMapping("/{lobbyId}/leave")
    fun postLeaveLobby(@PathVariable lobbyId: Long, @RequestParam playerId: Long): ResponseEntity<String> {

        val player = playerService.getPlayer(playerId)
        if(player == null) {
            return ResponseEntity.badRequest().body("Player not found")
        }

        val result = lobbyService.leaveLobby(playerId, lobbyId)

        if(result.isSuccess) {

            val leaveEvent = LobbyLeaveEvent(playerId, player.name)
            messagingTemplate.convertAndSend("/topic/lobby/${lobbyId}/leave", leaveEvent)
            sendLobbiesState()

            return ResponseEntity.ok(result.getOrNull().toString())
        } else {
            return ResponseEntity.badRequest().body(result.exceptionOrNull()?.message ?: "Unknown error")
        }

    }

    @PutMapping("/{lobbyId}/map/{mapId}")
    fun putMap(@PathVariable lobbyId: Long, @PathVariable mapId: Long,): ResponseEntity<String> {

        val map = neonMapService.getMap(mapId)
        if(map == null) {
            return ResponseEntity.badRequest().body("Map not found")
        }

        val result = lobbyService.setMap(lobbyId, mapId)

        if(result.isSuccess) {

            val mapChosenEvent = LobbyMapChosenEvent(map)
            messagingTemplate.convertAndSend("/topic/lobby/${lobbyId}/map", mapChosenEvent)
            sendLobbiesState()

            return ResponseEntity.ok(result.getOrNull().toString())
        } else {
            return ResponseEntity.badRequest().body(result.exceptionOrNull()?.message ?: "Unknown error")
        }

    }

    @PostMapping("/{lobbyId}/start")
    fun startGame(@PathVariable lobbyId: Long): ResponseEntity<String> {

        val lobby = lobbyService.getLobby(lobbyId)
        if(lobby == null) {
            return ResponseEntity.badRequest().body("Lobby not found")
        }

        val game: GameSession? = gameService.newGame(lobby)
        if(game == null) {
            return ResponseEntity.badRequest().body("Game couldn't be started")
        }
        game.startLoop()

        val gameStartEvent = GameStartEvent(game.id)
        messagingTemplate.convertAndSend("/topic/lobby/${lobbyId}/start", gameStartEvent)

        return ResponseEntity.ok("Game Started!")
    }

    fun sendLobbiesState() {
        val lobbiesStateEvent = LobbiesStateEvent(lobbyService.getLobbies())
        messagingTemplate.convertAndSend("/topic/lobbies", lobbiesStateEvent)
    }

}