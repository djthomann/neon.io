<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import LobbyComponent from '../components/LobbyComponent.vue'
import type { Lobby } from '../assets/types/lobby'
import { useUserStore } from '@/stores/user'
import { useLobbyStore } from '@/stores/lobbies'

const userStore = useUserStore()
const lobbyStore = useLobbyStore()

let lobbyList = ref<Lobby[]>([])

async function getLobbies(): Promise<Lobby[]> {
  const res = await fetch('/api/lobbies', { method: 'GET' })
  if (!res.ok) {
    // error.value = "Error when creating lobby"
  }
  const data = await res.json()
  return data as Lobby[]
}

async function fetchLobbies() {
  const lobbies = await getLobbies()

  lobbyStore.lobbyList = lobbies
}

async function postCreateLobby(): Promise<Lobby> {
  const res = await fetch('/api/lobbies/create', { method: 'POST' })
  if (!res.ok) {
    // error.value = "Error when creating lobby"
  }
  const data = await res.json()
  return data as Lobby
}

async function createLobby() {
  const lobby: Lobby = await postCreateLobby()

  lobbyStore.lobbyList.push(lobby)
  console.log(lobbyList)
}
</script>

<template>
  <div id="lobby">
    <nav id="navigation">
      <div id="lobby-navigation">
        <RouterLink to="/"><</RouterLink>
        <h1>This is the lobby page</h1>
        <button @click="fetchLobbies">Fetch lobbies</button>
        <button @click="createLobby">Create lobby</button>
      </div>
      <div id="user-info">
        <p>Welcome {{ userStore.username }}! Your id is: {{ userStore.id }}</p>
      </div>
    </nav>
    <section id="lobbies">
      <LobbyComponent
        v-for="lobby in lobbyStore.lobbyList"
        :key="lobby.id"
        :lobby="lobby"
      ></LobbyComponent>
    </section>
  </div>
</template>

<style scoped>
#navigation {
  padding: 0 4%;
  width: 92%;
  background-color: black;
  justify-content: space-between;
  align-items: center;
}

#lobby-navigation {
  width: 600px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

#user-info {
}

#lobbies {
  width: 92%;
  padding: 4% 4% 0 4%;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-template-rows: repeat(5, 1fr);
  grid-column-gap: 25px;
  grid-row-gap: 25px;
}

#lobby {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
}

nav {
  display: flex;
}
</style>
