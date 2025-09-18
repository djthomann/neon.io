package de.tho.neon.neon.io.controller

import de.tho.neon.neon.io.model.Lobby
import de.tho.neon.neon.io.service.lobby.LobbyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/lobbies")
class LobbyController {

    @Autowired
    lateinit var lobbyService: LobbyService

    @PostMapping("/create")
    fun postNewLobby(@RequestParam(required = false) ownerId: Long?): Lobby {
        return lobbyService.createLobby(ownerId)
    }

    @PostMapping("{id}/delete")
    fun deleteLobby(@PathVariable("id") id: Long): ResponseEntity<String> {
        val result = lobbyService.deleteLobby(id)
        return result.fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { ResponseEntity.badRequest().body(it.message) }
        )
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
        val result = lobbyService.joinLobby(playerId, lobbyId)
        return result.fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { ResponseEntity.badRequest().body(it.message) }
        )
    }

    @PostMapping("/{lobbyId}/leave")
    fun postLeaveLobby(@PathVariable lobbyId: Long, @RequestParam playerId: Long): ResponseEntity<String> {
        val result = lobbyService.leaveLobby(playerId, lobbyId)
        return result.fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { ResponseEntity.badRequest().body(it.message) }
        )

    }

    @PutMapping("/{lobbyId}/map/{mapId}")
    fun putMap(@PathVariable lobbyId: Long, @PathVariable mapId: Long,): ResponseEntity<String> {
        val result = lobbyService.setMap(lobbyId, mapId)
        return result.fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { ResponseEntity.badRequest().body(it.message) }
        )
    }

}