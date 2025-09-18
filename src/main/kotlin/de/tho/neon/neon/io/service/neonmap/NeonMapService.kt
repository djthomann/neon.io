package de.tho.neon.neon.io.service.neonmap

import de.tho.neon.neon.io.model.NeonMap

interface NeonMapService {

    fun createMap(): NeonMap

    fun getMaps(): List<NeonMap>
    fun getMap(id: Long): NeonMap?

}