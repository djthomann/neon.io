<script setup lang="ts">
import { onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import LobbyComponent from '../components/LobbyComponent.vue'
import type { Lobby } from '../assets/types/lobby'
import { useUserStore } from '@/stores/user'
import { useLobbyStore } from '@/stores/lobbies'
import UserComponent from '@/components/UserComponent.vue'
import LobbiesEventComponent from '@/components/communication/LobbiesEventComponent.vue'
import PlayerView from './PlayerView.vue'

const userStore = useUserStore()
const lobbyStore = useLobbyStore()

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
    <LobbiesEventComponent />
    <nav>
      <div id="lobby-navigation">
        <RouterLink to="/"><</RouterLink>
        <h1>This is the lobby page</h1>
        <button @click="fetchLobbies">Fetch lobbies</button>
        <button @click="createLobby">Create lobby</button>
      </div>
      <UserComponent />
    </nav>
    <section class="info">
      <section id="lobbies">
      <LobbyComponent
        v-for="lobby in lobbyStore.lobbyList"
        :key="lobby.id"
        :lobby="lobby"
      ></LobbyComponent>
    </section>
    <section class="players">
      <PlayerView />
    </section>
    </section>
  </div>
</template>

<style scoped>
nav {
  padding: 0 4%;
  width: 92%;
  height: 10vh;

  background-color: black;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

#lobby-navigation {
  display: flex;
  flex-direction: row;
  gap: 25px;
  align-items: center;
  justify-content: space-between;
}

.info {
  width: 100%;
  display: flex;
}

#lobbies {
  width: 80%;
  padding: 4% 4% 0 4%;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-template-rows: repeat(5, 1fr);
  grid-column-gap: 25px;
  grid-row-gap: 25px;
}

.players {
  width: 20%;
  padding: 4% 4% 0 4%;
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
