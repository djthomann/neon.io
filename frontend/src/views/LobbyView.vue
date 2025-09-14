<script setup lang="ts">
import { RouterLink } from 'vue-router'
import LobbyComponent from '../components/LobbyComponent.vue'
import type { Lobby } from '../assets/types/lobby'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

let lobbyList: Lobby[] = [
  {
    id: 1,
    name: 'The first lobby',
    capacity: 8,
  },
  {
    id: 2,
    name: 'The second lobby',
    capacity: 4,
  },
]
</script>

<template>
  <div id="lobby">
    <nav id="navigation">
      <div id="lobby-navigation">
        <RouterLink to="/"><</RouterLink>
        <h1>This is the lobby page</h1>
        <button>Create lobby</button>
      </div>
      <div id="user-info">
        <p>Welcome {{ userStore.username }}! Your id is: {{ userStore.id }}</p>
      </div>
    </nav>
    <section id="lobbies">
      <LobbyComponent v-for="lobby in lobbyList" :key="lobby.id" :lobby="lobby"></LobbyComponent>
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
  padding: 0 4%;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-template-rows: repeat(5, 1fr);
  grid-column-gap: 0px;
  grid-row-gap: 0px;
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
