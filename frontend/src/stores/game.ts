import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useGameStore = defineStore('game', () => {
  const x = ref<number>(0)
  const y = ref<number>(0)
  const z = ref<number>(0)

  return { x, y, z }
})