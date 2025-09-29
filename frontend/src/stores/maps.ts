import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { NeonMap } from '@/assets/types/map'

export const useMapStore = defineStore('maps', () => {
  const mapList = ref<NeonMap[]>([])

  return { mapList }
})
