<script setup lang="ts">
import type { Lobby } from '@/assets/types/lobby'
import UserComponent from '@/components/UserComponent.vue'
import { useLobbyStore } from '@/stores/lobbies'
import { useUserStore } from '@/stores/user'
import { RouterLink, useRouter } from 'vue-router'

const router = useRouter()

const userStore = useUserStore()

const lobbyStore = useLobbyStore()

async function postDeleteLobby(lobbyId: number): Promise<Boolean> {
  const res = await fetch(`/api/lobbies/${lobbyId}/delete`, {
    method: 'POST',
  })
  return res.ok
}

async function deleteLobby() {
  const success = await postDeleteLobby(lobbyStore.selectedLobby!.id)
  if (success) {
    router.push({ name: 'lobbies' })
    lobbyStore.selectedLobby = null
  } else {
    console.log("Couldn't delete")
  }
}

async function postLeaveLobby(lobbyId: number): Promise<Boolean> {
  const res = await fetch(`/api/lobbies/${lobbyId}/leave?playerId=${userStore.id}`, {
    method: 'POST',
  })
  return res.ok
}

async function leaveLobby() {
  const success = await postLeaveLobby(lobbyStore.selectedLobby!.id)
  if (success) {
    router.push({ name: 'lobbies' })
    lobbyStore.selectedLobby = null
  } else {
    console.log("Couldn't join")
  }
}

async function getLobby(): Promise<Lobby> {
  const res = await fetch(`/api/lobbies/${lobbyStore.selectedLobby?.id}`, { method: 'GET' })
  if (!res.ok) {
    // error.value = "Error when creating lobby"
  }
  const data = await res.json()
  return data as Lobby
}

async function fetchLobby() {
  const lobby = await getLobby()
  console.log(lobby)
  lobbyStore.selectedLobby = await getLobby()
}
</script>

<template>
  <div class="detail">
    <nav id="navigation">
      <div id="lobby-navigation">
        <a @click="leaveLobby"><</a>
        <h1 v-if="lobbyStore.selectedLobby">Welcome to Lobby {{ lobbyStore.selectedLobby.id }}</h1>
        <button @click="fetchLobby">Fetch updates</button>
        <button @click="deleteLobby">Delete lobby</button>
      </div>
      <UserComponent />
    </nav>
    <section id="information">
      <h2>Players</h2>
      <li v-for="player in lobbyStore.selectedLobby?.players">
        {{ player.name }} (#{{ player.id }})
      </li>
    </section>
  </div>
</template>

<style scoped>
a {
  cursor: pointer;
}

h2 {
  margin: 0;
}

#navigation {
  display: flex;
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

#information {
  width: 92%;
  height: 92vh;
  padding: 4vh 4%;
  background-color: green;
}

.detail {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
}
</style>
