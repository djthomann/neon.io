package de.tho.neon.neon.io.controller

import de.tho.neon.neon.io.model.Player
import de.tho.neon.neon.io.service.player.PlayerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/players")
class PlayerController(
    private val playerService: PlayerService
) {

    @PostMapping("/new/{name}")
    fun postNewPlayer(@PathVariable name: String): Player {
        return playerService.createPlayer(name)
    }

    @GetMapping()
    fun getPlayers(): List<Player> {
        return playerService.getPlayers()
    }

}