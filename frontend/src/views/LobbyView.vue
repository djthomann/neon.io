<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import LobbyComponent from '../components/LobbyComponent.vue'
import type { Lobby } from '../assets/types/lobby'
import { useUserStore } from '@/stores/user'
import { useLobbyStore } from '@/stores/lobbies'
import UserComponent from '@/components/UserComponent.vue'
import LobbiesEventComponent from '@/components/communication/LobbiesEventComponent.vue'
import PlayerView from './PlayerView.vue'
import NavBarComponent from '@/components/NavBarComponent.vue'

const userStore = useUserStore()
const lobbyStore = useLobbyStore()

const playersVisible = ref<boolean>(true)

function showPlayers() {
  playersVisible.value = !playersVisible.value
}

async function getLobbies(): Promise<Lobby[]> {
  const res = await fetch('/api/lobbies', { method: 'GET' })
  if (!res.ok) {
    // error.value = "Error when getting lobbies"
  }
  const data = await res.json()
  return data as Lobby[]
}

async function fetchLobbies() {
  lobbyStore.lobbyList = await getLobbies()
}

async function postCreateLobby(): Promise<Lobby> {
  const res = await fetch(`/api/lobbies/create?ownerId=${userStore.id}`, { method: 'POST' })
  if (!res.ok) {
    // error.value = "Error when creating lobby"
  }
  const data = await res.json()
  return data as Lobby
}

async function createLobby() {
  const lobby: Lobby = await postCreateLobby()
}

onMounted(() => {
  fetchLobbies()
})
</script>

<template>
  <div id="lobby">
    <LobbiesEventComponent/>
    <NavBarComponent>
      <template #buttons>
        <button @click="fetchLobbies">Fetch lobbies</button>
        <button @click="createLobby">Create lobby</button>
        <button @click="showPlayers">Show players</button>
      </template>
    </NavBarComponent>
    <section class="info">
      <section id="lobbies" class="sidebar">
      <LobbyComponent
        v-for="lobby in lobbyStore.lobbyList"
        :key="lobby.id"
        :lobby="lobby"
      ></LobbyComponent>
      </section>
      <PlayerView :players-visible="playersVisible" />
    </section>
  </div>
</template>

<style scoped>

.sidebar {
  overflow-y: auto;
  scrollbar-width: none;
}

.sidebar::-webkit-scrollbar {
  display: none;
}

.info {
  width: 100%;
  height: 90vh;
  display: flex;
}

#lobbies {
  width: 100%;
  padding: 4% 4% 0 4%;
  display: grid;
  overflow-y:scroll;
  grid-template-columns: repeat(5, 1fr);
  grid-template-rows: repeat(5, 1fr);
  grid-column-gap: 25px;
  grid-row-gap: 25px;
}

.players {
  width: 10%;
  padding: 2%;
  background-color: gray;
}

#lobby {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
}
</style>
