<script setup lang="ts">
import { ref } from 'vue'
import type { Player } from '../assets/types/player'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const username = ref('')
const error = ref('')
const router = useRouter()

async function postUsername(name: string): Promise<Player> {
  const res = await fetch(`/api/players/new/${name}`, { method: 'POST' })
  if (!res.ok) {
    error.value = 'Error when creating player'
  }
  const data = await res.json()
  return data as Player
}

async function joinLobby() {
  if (!userStore.username) {
    const newPlayer = await postUsername(username.value)
    userStore.id = newPlayer.id
    userStore.username = newPlayer.name
  }
  router.push('/lobbies')
}

async function openMapCreator() {
  router.push('/map-creator')
}
</script>

<template>
  <div id="home">
    <h1 id="title">neon.io</h1>
    <label v-if="!userStore.username" for="username">Enter Username:</label>
    <input
      @keydown.enter="joinLobby"
      v-if="!userStore.username"
      v-model="username"
      placeholder="Your username goes here"
      type="text"
      name="username"
    />
    <p v-if="userStore.username">Welcome {{ userStore.username }}</p>
    <p>{{ error }}</p>
    <div class="buttons">
      <button @click="joinLobby">Join Lobby</button>
      <button @click="openMapCreator">Create Map</button>
    </div>
  </div>
</template>

<style scoped>
.buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

#title {
  font-size: 72pt;
  color: white;
  text-shadow:
    0 0 7px rgb(0, 189, 0),
    0 0 10px rgb(0, 189, 0),
    0 0 21px rgb(0, 189, 0),
    0 0 42px #0fa,
    0 0 82px #0fa,
    0 0 92px #0fa,
    0 0 102px #0fa,
    0 0 151px #0fa;
}

#home {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
</style>
