<script setup lang="ts">
import { ref } from 'vue';
import type {Player} from '../assets/types/player';
import { useRouter } from "vue-router"
import { useUserStore } from '@/stores/user';

const userStore = useUserStore()
const username = ref("")
const error = ref("")
const router = useRouter()

async function postUsername(name: string): Promise<Player> {
  const res = await fetch(`/api/players/new/${name}`, { method: "POST" })
  if (!res.ok) {
    error.value = "Error when creating player"
  }
  const data = await res.json()
  return data as Player
}

async function joinLobby() {
  if(!userStore.username) {

    const newPlayer = await postUsername(username.value)
    userStore.id = newPlayer.id
    userStore.username = newPlayer.name
  }
  router.push("/lobbies")
}

</script>

<template>
  <div id="home">
    <h1 id="title">neon.io</h1>
      <label v-if="!userStore.username" for="username">Enter Username:</label>
      <input v-if="!userStore.username" v-model="username" placeholder="Your username goes here" type="text" name="username"></input>
      <p v-if="userStore.username">Welcome {{ userStore.username }}</p>
    <p>{{ error }}</p>
    <button @click="joinLobby">Join Lobby</button>
  </div>
</template>

<style scoped>

#title {
  font-size: 64pt;
}

#home {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

</style>
