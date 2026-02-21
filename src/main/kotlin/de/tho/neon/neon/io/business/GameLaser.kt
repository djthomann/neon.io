package de.tho.neon.neon.io.business

data class GameLaser(
    val shotAt: Long,
    val origin: Vector3,
    val direction: Vector3
)

// TODO() Implement real intersection with 3D cylinder as player
fun GameLaser.hitDistance2D(player: GamePlayer): Double? {
    // Ray origin (XZ)
    val ox = origin.x
    val oz = origin.z

    // Ray direction (XZ)
    val dx = direction.x
    val dz = direction.z

    // Player center (XZ)
    val cx = player.x
    val cz = player.z
    val radius = player.radius

    // Vector from circle center to ray origin
    val ocx = ox - cx
    val ocz = oz - cz

    // Quadratic coefficients
    val a = dx * dx + dz * dz
    val b = 2.0 * (ocx * dx + ocz * dz)
    val c = ocx * ocx + ocz * ocz - radius * radius

    val discriminant = b * b - 4.0 * a * c

    if (discriminant < 0.0) {
        return null // no hit
    }

    val sqrtDisc = kotlin.math.sqrt(discriminant)

    val t1 = (-b - sqrtDisc) / (2.0 * a)
    val t2 = (-b + sqrtDisc) / (2.0 * a)

    // behind or in front of ray
    val t = when {
        t1 >= 0.0 -> t1
        t2 >= 0.0 -> t2
        else -> return null
    }

    return t
}