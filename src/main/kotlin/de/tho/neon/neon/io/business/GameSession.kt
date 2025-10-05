package de.tho.neon.neon.io.business

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.ScheduledFuture

class GameSession(
    val id: Long,
    val length: Long
) {
    
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private var future: ScheduledFuture<*>? = null

    fun startLoop() {
        future = executor.scheduleAtFixedRate(
            { tick() },
            0,
            1000,
            TimeUnit.MILLISECONDS
        )

        executor.schedule({
            stopLoop()
        }, length, TimeUnit.SECONDS)
    }

    private fun stopLoop() {
        future?.cancel(false)
        println("Game $id: finished.")
        executor.shutdown()
    }

    private fun tick() {
        println("Game $id: TICK!")
    }

}