import type { Vector3 } from 'three'

export interface GameLaser {
  shotAt: number
  origin: Vector3
  direction: Vector3
}
