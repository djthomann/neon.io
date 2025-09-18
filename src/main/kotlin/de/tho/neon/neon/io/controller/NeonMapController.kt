package de.tho.neon.neon.io.controller

import de.tho.neon.neon.io.model.NeonMap
import de.tho.neon.neon.io.service.neonmap.NeonMapService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/maps")
class NeonMapController {

    @Autowired
    lateinit var neonMapService: NeonMapService

    @PostMapping("/create")
    fun postNewMap(): NeonMap {
        return neonMapService.createMap()
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