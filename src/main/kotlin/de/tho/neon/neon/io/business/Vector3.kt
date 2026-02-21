
package de.tho.neon.neon.io.business

import kotlin.math.sqrt

data class Vector3(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0
)

fun Vector3.normalize(): Vector3 {
    val len = sqrt(x * x + y * y + z * z)

    if (len == 0.0) {
        return Vector3(0.0, 0.0, 0.0)
    }

    return Vector3(
        x / len,
        y / len,
        z / len
    )
}