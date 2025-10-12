<template>
  <transition name="slide-width">
    <div
      v-if="props.playersVisible"
      class="box"
    >
      <div class="header">Players</div>
      <ul class="player-list">
        <li v-for="player in playersStore.players" :key="player.id">
          {{ player.name }} (#{{ player.id }})
        </li>
      </ul>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { defineProps, onMounted } from 'vue'
import { usePlayersStore } from '@/stores/players'
import type { Player } from '@/assets/types/player'

const playersStore = usePlayersStore()

async function getPlayers(): Promise<Player[]> {
  const res = await fetch('/api/players', { method: 'GET' })
  if (!res.ok) {
    // error.value = "Error when getting players"
  }
  const data = await res.json()
  return data as Player[]
}

async function fetchPlayers() {
  const players = await getPlayers()
  playersStore.players = players
}

onMounted(() => {
    fetchPlayers()
})



const props = defineProps({
  playersVisible: {
    type: Boolean,
    default: true
  }
})
</script>

<style scoped>
.box {
  max-width: 10%;
  width: 10%; /* volle Breite */
  background-color: #222;
  color: white;
  padding: 1rem;
  overflow-y: scroll;
}

/* Transition */
.slide-width-enter-from,
.slide-width-leave-to {
  max-width: 0;
  padding: 0;
  opacity: 0;
}

.slide-width-enter-to,
.slide-width-leave-from {
  max-width: 10%;
  padding: 1rem;
  opacity: 1;
}

.slide-width-enter-active,
.slide-width-leave-active {
  transition: max-width 0.5s ease, opacity 0.5s ease, padding 0.5s ease;
}

.player-list {
  margin-top: 1rem;
  padding: 0;
  list-style: none;
}

.player-list li {
  padding: 0.25rem 0;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}
</style>
