import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { Player } from '@/assets/types/player'

export const useGameStore = defineStore('game', () => {
  const x = ref<number>(0)
  const y = ref<number>(0)
  const z = ref<number>(0)

  const players = ref<Player[] | null>(null)

  return { x, y, z, players }
})