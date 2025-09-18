package de.tho.neon.neon.io.service.lobby

import de.tho.neon.neon.io.model.Lobby
import de.tho.neon.neon.io.model.NeonMap
import de.tho.neon.neon.io.repository.LobbyRepository
import de.tho.neon.neon.io.repository.PlayerRepository
import de.tho.neon.neon.io.service.neonmap.NeonMapService
import de.tho.neon.neon.io.service.player.PlayerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class LobbyServiceImpl: LobbyService {
    @Autowired
    private lateinit var neonMapService: NeonMapService

    @Autowired
    lateinit var lobbyRepository: LobbyRepository

    @Autowired
    lateinit var playerService: PlayerService

    override fun createLobby(ownerId: Long?): Lobby {
        val owner = ownerId?.let { playerService.getPlayer(it) }
        return lobbyRepository.addLobby(owner)
    }

    override fun deleteLobby(id: Long): Result<String> {
        if(lobbyRepository.removeLobby(id)) {
            return Result.success("The Lobby was removed successfully")
        } else {
            return Result.failure(Exception("Lobby with id $id doesn't exist"))
        }
    }

    override fun getLobbies(): List<Lobby> {
        return lobbyRepository.getLobbies()
    }

    override fun getLobby(id: Long): Lobby? {
        return lobbyRepository.getLobby(id)
    }

    override fun joinLobby(playerId: Long, lobbyId: Long): Result<String> {

        val lobby = lobbyRepository.getLobby(lobbyId)
            ?: return Result.failure(Exception("Lobby not found"))

        val player = playerService.getPlayer(playerId)
            ?: return Result.failure(Exception("Player not found"))

        if(lobby.players.contains(player)) {
            return Result.failure(Exception("Player was already in the lobby"))
        } else {
            lobby.players.add(player)
            return Result.success("Player added")
        }

    }

    override fun leaveLobby(playerId: Long, lobbyId: Long): Result<String> {

        val lobby = lobbyRepository.getLobby(lobbyId)
            ?: return Result.failure(Exception("Lobby not found"))

        val player = playerService.getPlayer(playerId)
            ?: return Result.failure(Exception("Player not found"))

        return if (lobby.players.remove(player)) {
            Result.success("Player removed")
        } else {
            Result.failure(Exception("Player was not in the lobby"))
        }
    }

    override fun setMap(lobbyId: Long, mapId: Long): Result<String> {
        val lobby = getLobby(lobbyId)
        if (lobby == null) {
            return Result.failure(Exception("Lobby with ID $lobbyId not found"))
        }

        val map: NeonMap? = neonMapService.getMap(mapId)
        if (map == null) {
            return Result.failure(Exception("Map with ID $mapId not found"))
        }

        lobby.map = map
        return Result.success("Map set")
    }
}