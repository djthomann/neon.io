package de.tho.neon.neon.io.controller

import de.tho.neon.neon.io.model.NeonMap
import de.tho.neon.neon.io.service.neonmap.NeonMapService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/maps")
class NeonMapController {

    val logger: Logger = LoggerFactory.getLogger(NeonMapController::class.java)

    @Autowired
    lateinit var neonMapService: NeonMapService

    @PostMapping("/create")
    fun postNewMap(): NeonMap {
        return neonMapService.createMap()
    }

    @PostMapping("/upload")
    fun uploadMap(@RequestParam("file") file: MultipartFile, @RequestParam("name") name: String): ResponseEntity<String> {
        return try {

            val content = file.inputStream.bufferedReader().use { it.readText() }

            logger.info("File name: ${file.originalFilename}")
            logger.info("content:\n$content")

            val map = neonMapService.createMapFromString(name, content)

            if(map == null) {
                return ResponseEntity.badRequest().body("Map could not be created")
            } else {
                ResponseEntity.ok("Map uploaded successfully")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).body("Error when reading file: ${e.message}")
        }
    }

    @GetMapping("")
    fun getMaps(): List<NeonMap> {
        return neonMapService.getMaps()
    }

    @GetMapping("{id}")
    fun getMap(@PathVariable("id") id: Long): NeonMap? {
        return neonMapService.getMap(id)
    }

}