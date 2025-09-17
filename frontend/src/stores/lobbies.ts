import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import type { Lobby } from '@/assets/types/lobby'

export const useLobbyStore = defineStore('lobbies', () => {
  const selectedLobby = ref<Lobby | null>(null)

  const lobbyList = ref<Lobby[]>([])

  return { lobbyList, selectedLobby }
})
