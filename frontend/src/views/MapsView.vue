<script setup lang="ts">
import { onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import LobbyComponent from '../components/LobbyComponent.vue'
import type { Lobby } from '../assets/types/lobby'
import { useUserStore } from '@/stores/user'
import { useLobbyStore } from '@/stores/lobbies'
import UserComponent from '@/components/UserComponent.vue'
import { useMapStore } from '@/stores/maps'
import { storeToRefs } from 'pinia'
import { getMaps } from '@/service/mapService'
import MapComponent from '@/components/MapComponent.vue'

const userStore = useUserStore()

const mapStore = useMapStore()
const { mapList } = storeToRefs(mapStore)

async function fetchMaps() {
  mapList.value = await getMaps()
}

onMounted(() => {
  fetchMaps()
})
</script>

<template>
  <div class="root">
    <nav>
      <div class="nav-elements">
        <RouterLink to="/"><</RouterLink>
        <h1>This is the map page</h1>
        <button>Das ist ein Button</button>
      </div>
      <UserComponent />
    </nav>
    <section class="maps">
      <MapComponent v-for="map in mapList" :map="map" />
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

.nav-elements {
  display: flex;
  flex-direction: row;
  gap: 25px;
  align-items: center;
  justify-content: space-between;
}

.root {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
}

.maps {
  width: 92%;
  padding: 4% 4% 0 4%;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-template-rows: repeat(5, 1fr);
  grid-column-gap: 25px;
  grid-row-gap: 25px;
}
</style>
