<script setup lang="ts">
import { ref } from 'vue'
import type { Player } from '../assets/types/player'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import HomeAnimationView from './HomeAnimationView.vue'
import UserComponent from '@/components/UserComponent.vue'

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

async function sendUsername() {
  if (!userStore.username) {
    const newPlayer = await postUsername(username.value)
    userStore.id = newPlayer.id
    userStore.username = newPlayer.name
  }
}

async function joinLobby() {
  router.push('/lobbies')
}

function openMapCreator() {
  router.push('/maps/new')
}

function openMaps() {
  router.push('/maps')
}
</script>

<template>
  <div id="home">
    <div id="menu">
      <h1 class="title">neon.io</h1>
      <div class="username-field" v-if="!userStore.username" >
        <label for="username">Username</label>
        <input
          @keydown.enter="sendUsername"
          v-if="!userStore.username"
          v-model="username"
          placeholder="Your username goes here"
          type="text"
          name="username"
        />
      </div>
      <UserComponent v-if="userStore.username"></UserComponent>
      <p>{{ error }}</p>
      <transition name="slide-fade">
        <div class="buttons" v-if="userStore.username">
          <button @click="joinLobby">Join Lobby</button>
          <button @click="openMaps">Open Maps</button>
          <button @click="openMapCreator">Create Map</button>
        </div>
      </transition>

    </div>
    <HomeAnimationView />
  </div>
</template>

<style scoped>
#menu {
  width: 40%;
  height: 80%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

.username-field {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.buttons {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.slide-fade-enter-active, .slide-fade-leave-active {
  transition: all 1s ease;
}
.slide-fade-enter-from, .slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
  height: 0;
}
.slide-fade-enter-to, .slide-fade-leave-from {
  opacity: 1;
  transform: translateY(0);
  height: auto;
}

.title {
  user-select: none;
  font-size: 72pt;
  color: white;
  text-shadow:
    0 0 7px var(--primary),
    0 0 10px var(--primary),
    0 0 21px var(--primary),
    0 0 42px  var(--primary),
    0 0 82px  var(--primary),
    0 0 92px  var(--primary),
    0 0 102px  var(--primary),
    0 0 151px  var(--primary);
  transition: transform 1s ease;
}

#home {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
  background-color: black;
}
</style>
