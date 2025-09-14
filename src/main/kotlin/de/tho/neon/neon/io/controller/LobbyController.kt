package de.tho.neon.neon.io.controller

import de.tho.neon.neon.io.model.Lobby
import de.tho.neon.neon.io.service.lobby.LobbyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/lobbies")
class LobbyController {

    @Autowired
    lateinit var lobbyService: LobbyService

    @PostMapping("/create")
    fun postNewLobby(): Lobby {
        return lobbyService.createLobby()
    }

    @GetMapping("")
    fun getLobbies(): List<Lobby> {
        return lobbyService.getLobbies()
    }

    @GetMapping("{id}")
    fun getLobby(@PathVariable("id") id: Long): Lobby? {
        return lobbyService.getLobby(id)
    }

}