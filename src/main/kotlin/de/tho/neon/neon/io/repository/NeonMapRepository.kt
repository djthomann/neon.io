package de.tho.neon.neon.io.repository

import de.tho.neon.neon.io.model.NeonMap
import org.springframework.stereotype.Repository
import kotlin.random.Random

@Repository
class NeonMapRepository {

    var nextId: Long = 1
    val maps = mutableMapOf<Long, NeonMap>()

    fun scambleMap(map: NeonMap) {
        val scrambledTiles = Array(map.height) { y ->
            Array(map.width) { x ->
                Random.nextBoolean()
            }
        }
        map.tiles = scrambledTiles
    }

    fun addMap(): NeonMap {
        val id = nextId
        val map = NeonMap(nextId, 5, 5)
        scambleMap(map)
        maps[id] = map
        nextId++
        return map
    }

    fun getMaps(): List<NeonMap> {
        return maps.values.toList()
    }

    fun getMap(id: Long): NeonMap? {
        return maps[id]
    }

}