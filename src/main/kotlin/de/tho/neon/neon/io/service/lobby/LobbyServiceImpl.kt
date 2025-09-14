package de.tho.neon.neon.io.service.lobby

import de.tho.neon.neon.io.model.Lobby
import de.tho.neon.neon.io.repository.LobbyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LobbyServiceImpl: LobbyService {

    @Autowired
    lateinit var lobbyRepository: LobbyRepository

    override fun createLobby(): Lobby {
        return lobbyRepository.addLobby()
    }

    override fun getLobbies(): List<Lobby> {
        return lobbyRepository.getLobbies()
    }

    override fun getLobby(id: Long): Lobby? {
        return lobbyRepository.getLobby(id)
    }
}