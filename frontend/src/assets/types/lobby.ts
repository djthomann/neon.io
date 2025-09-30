import type { NeonMap } from './map'
import type { Player } from './player'

export interface Lobby {
  id: number
  players: Player[]
  capacity: number
  map: NeonMap | null
}
