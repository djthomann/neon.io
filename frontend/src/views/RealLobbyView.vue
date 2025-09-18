<script setup lang="ts">
import type { Lobby } from '@/assets/types/lobby'
import type { NeonMap } from '@/assets/types/map'
import MapComponent from '@/components/MapComponent.vue'
import UserComponent from '@/components/UserComponent.vue'
import { useLobbyStore } from '@/stores/lobbies'
import { useUserStore } from '@/stores/user'
import { onMounted, ref } from 'vue'
import {  useRouter } from 'vue-router'

const router = useRouter()

const userStore = useUserStore()

const lobbyStore = useLobbyStore()

const maps = ref<NeonMap[]>()
const selectedMap = ref<NeonMap | null>(null)
let currentIndex = 0

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

async function getMaps(): Promise<NeonMap[]> {
  const res = await fetch('/api/maps', { method: 'GET' })
  if (!res.ok) {
    // error.value = "Error when getting maps"
  }
  const data = await res.json()
  return data as NeonMap[]
}

async function fetchMaps() {
  maps.value = await getMaps()

  if (!selectedMap.value) {
    selectedMap.value = maps.value?.[0] ?? null
    console.log(selectedMap)
  }
}

function nextMap() {
  if (!maps.value || maps.value.length === 0) return

  if (currentIndex < maps.value.length - 1) {
    currentIndex++
  } else {
    currentIndex = 0
  }
  selectedMap.value = maps.value[currentIndex]
}

function prevMap() {
  if (!maps.value || maps.value.length === 0) return

  if (currentIndex > 0) {
    currentIndex--
  } else {
    currentIndex = maps.value.length - 1
  }
  selectedMap.value = maps.value[currentIndex]
}

async function putMap(): Promise<Boolean> {
  const res = await fetch(
    `/api/lobbies/${lobbyStore.selectedLobby?.id}/map/${selectedMap.value?.id}`,
    {
      method: 'PUT',
    },
  )
  return res.ok
}

async function chooseMap() {
  const success = await putMap()

  if (success) {
    // Update lobby manually --> TODO: change later
    fetchLobby()
  }
}

onMounted(() => {
  fetchLobby()
  fetchMaps()
})
</script>

<template>
  <div class="detail">
    <nav id="navigation">
      <div id="lobby-navigation">
        <a @click="leaveLobby"><</a>
        <h1 v-if="lobbyStore.selectedLobby">Welcome to Lobby {{ lobbyStore.selectedLobby.id }}</h1>
        <button @click="fetchLobby">Fetch updates</button>
        <button @click="fetchMaps">Get Maps</button>
        <button @click="deleteLobby">Delete lobby</button>
      </div>
      <UserComponent />
    </nav>
    <section id="information">
      <div id="player-information">
        <h2>Players</h2>
        <li v-for="player in lobbyStore.selectedLobby?.players">
          {{ player.name }} (#{{ player.id }})
        </li>
      </div>
      <div v-if="!lobbyStore.selectedLobby?.map" class="map-information">
        <h2>Map {{ currentIndex + 1 }} / {{ maps?.length }}</h2>
        <MapComponent v-if="selectedMap" :map="selectedMap"></MapComponent>
        <div>
          <a @click="prevMap">< </a>
          <button @click="chooseMap">Choose Map</button>
          <a @click="nextMap"> ></a>
        </div>
      </div>
      <div v-if="lobbyStore.selectedLobby?.map" class="map-information">
        <h2>Chosen Map:</h2>
        <MapComponent :map="lobbyStore.selectedLobby?.map"></MapComponent>
      </div>
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
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: 1fr;
  grid-column-gap: 0;
  grid-row-gap: 0;
}

.map-information {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.detail {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
}
</style>
