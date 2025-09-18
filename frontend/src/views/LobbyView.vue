<script setup lang="ts">
import { onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import LobbyComponent from '../components/LobbyComponent.vue'
import type { Lobby } from '../assets/types/lobby'
import { useUserStore } from '@/stores/user'
import { useLobbyStore } from '@/stores/lobbies'
import UserComponent from '@/components/UserComponent.vue'

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

  lobbyStore.lobbyList.push(lobby)
}

onMounted(() => {
  fetchLobbies()
})
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
      <UserComponent />
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
  height: 10vh;

  background-color: black;
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
