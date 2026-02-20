import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { Player } from '@/assets/types/player'
import type { GameLaser } from '@/assets/types/events/gameLaser'

export const useGameStore = defineStore('game', () => {
  const x = ref<number>(0)
  const y = ref<number>(0)
  const z = ref<number>(0)

  const time = ref<number>(100)

  const players = ref<Player[] | null>(null)

  const lasers = ref<GameLaser[]>([])

  return { time, x, y, z, players, lasers }
})
