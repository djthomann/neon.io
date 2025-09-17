package de.tho.neon.neon.io.service.player

import de.tho.neon.neon.io.model.Player
import de.tho.neon.neon.io.repository.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PlayerServiceImpl: PlayerService {

    @Autowired
    lateinit var playerRepository: PlayerRepository

    override fun getPlayers(): List<Player> {
        return playerRepository.getPlayers()
    }

    override fun getPlayer(id: Long): Player? {
        return playerRepository.getPlayer(id)
    }

    override fun createPlayer(name: String): Player {
        return playerRepository.addPlayer(name)
    }
}