<template>
    <div class="box">
        <div>Players</div>
        <li v-for="player in playersStore.players">
          {{ player.name }} (#{{ player.id }})
        </li>
    </div>
</template>

<script lang="ts" setup>
import type { Player } from '@/assets/types/player';
import { usePlayersStore } from '@/stores/players';
import { onMounted } from 'vue';


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
  console.log(players)
  playersStore.players = players
}

onMounted(() => {
    fetchPlayers()
})

</script>

<style scoped>
.box {
  height: 100%;
}
</style>