<script setup lang="ts">
import { RouterLink } from 'vue-router';
import LobbyComponent from '../components/LobbyComponent.vue'
import { Lobby } from '../assets/types/lobby';

let lobbyList: Lobby[] = [
    {
        id: 1,
        name: "The first lobby",
        capacity: 8
    },
    {
        id: 2,
        name: "The second lobby",
        capacity: 4
    }
]

</script>

<template>
  <div id="lobby">
    <nav>
        <RouterLink to="/"><</RouterLink>
        <h1>This is the lobby page</h1>
        <button>Lobby anfordern</button>
    </nav>
    
    <LobbyComponent v-for="lobby in lobbyList" :key="lobby.id" :lobby="lobby"></LobbyComponent>
  </div>
</template>


<style scoped>
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
