import de.tho.neon.neon.io.model.Lobby

data class LobbiesStateEvent(
    val lobbies: List<Lobby>
)