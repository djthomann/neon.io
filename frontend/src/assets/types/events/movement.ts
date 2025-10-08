import type { Vector3 } from "three";
import type { MovementDirection } from "./movementDirections";

export interface Movement {
    playerId: number,
    vector: Vector3
}