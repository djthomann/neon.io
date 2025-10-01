import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { Player } from '@/assets/types/player'

export const usePlayersStore = defineStore('players', () => {
  const players = ref<Player[]>([])

  return { players }
})
