package de.tho.neon.neon.io.service.neonmap

import de.tho.neon.neon.io.model.NeonMap
import de.tho.neon.neon.io.repository.NeonMapRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NeonMapServiceImpl: NeonMapService {

    @Autowired
    lateinit var neonMapRepository: NeonMapRepository

    override fun createMap(): NeonMap {
        return neonMapRepository.addMap()
    }

    override fun getMaps(): List<NeonMap> {
        return neonMapRepository.getMaps()
    }

    override fun getMap(id: Long): NeonMap? {
        return neonMapRepository.getMap(id)
    }
}