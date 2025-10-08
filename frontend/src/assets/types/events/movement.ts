import type { MovementDirection } from "./movementDirections";

export interface Movement {
    playerId: number,
    direction: MovementDirection
  }