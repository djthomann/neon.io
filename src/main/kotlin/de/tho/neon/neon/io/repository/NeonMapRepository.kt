package de.tho.neon.neon.io.repository

import java.io.File
import de.tho.neon.neon.io.model.NeonMap
import org.springframework.stereotype.Repository
import kotlin.random.Random

@Repository
class NeonMapRepository {

    var nextId: Long = 1
    val maps = mutableMapOf<Long, NeonMap>()

    init {
        loadMaps()
    }

    private fun loadMaps() {
        val resourceUrl = javaClass.classLoader.getResource("maps")
            ?: throw IllegalStateException("Folder 'maps' not found.")

        val mapDir = File(resourceUrl.toURI())

        // Only load maps with .nmap ending
        val nmapFiles = mapDir.listFiles { file ->
            file.isFile && file.extension == "nmap"
        } ?: emptyArray()

        for (file in nmapFiles) {
            addMapFromFile(file)
        }

        println("Loaded Maps: ${maps.size}")
    }

    fun addMapFromFile(file: File) {

        val lines = file.readLines()

        val name = file.name
        val content = lines.joinToString("\n")

        addMapFromString(name, content)
    }

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
        val scrambledTiles = Array(map.height) { _ ->
            Array(map.width) { _ ->
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