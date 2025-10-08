
package de.tho.neon.neon.io.business

import de.tho.neon.neon.io.model.Player

data class GamePlayer(
    val id: Long,
    val user: Player,
    var x: Double,
    var y: Double,
    var z: Double
)