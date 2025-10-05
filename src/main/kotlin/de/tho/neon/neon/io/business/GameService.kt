package de.tho.neon.neon.io.business

import org.springframework.stereotype.Service
import de.tho.neon.neon.io.business.GameSession

@Service
class GameService {

    val games: MutableMap<Long, GameSession> = mutableMapOf()

    var nextId: Long = 0

    fun newGame(): GameSession {
        val id = nextId++
        val game = GameSession(id, 5)

        games.put(id, game)

        return game;

    }

}