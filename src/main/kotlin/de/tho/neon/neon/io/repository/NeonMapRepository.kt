package de.tho.neon.neon.io.repository

import de.tho.neon.neon.io.model.NeonMap
import org.springframework.stereotype.Repository
import kotlin.random.Random

@Repository
class NeonMapRepository {

    var nextId: Long = 1
    val maps = mutableMapOf<Long, NeonMap>()

    // Creates a NeonMap from a String with 0s and 1s
    fun addMapFromString(name: String, content: String): NeonMap? {
        val rows = content.lines()

        val width = rows[0].length
        val height = rows.size

        val map = addMap(name, width, height)

        val tiles = Array(height) { y ->
            Array(width) { x ->
                when (rows[y][x]) {
                    '1' -> true
                    '0' -> false
                    else -> return null
                }
            }
        }

        map.tiles = tiles
        return map

    }

    fun scambleMap(map: NeonMap) {
        val scrambledTiles = Array(map.height) { y ->
            Array(map.width) { x ->
                Random.nextBoolean()
            }
        }
        map.tiles = scrambledTiles
    }

    fun addMap(name: String, width: Int, height: Int): NeonMap {
        val id = nextId
        val map = NeonMap(nextId, name,width,height)
        scambleMap(map)
        maps[id] = map
        nextId++
        return map
    }
    fun addMap(): NeonMap {
        return addMap("New Map", 25, 25)
    }

    fun getMaps(): List<NeonMap> {
        return maps.values.toList()
    }

    fun getMap(id: Long): NeonMap? {
        return maps[id]
    }

}