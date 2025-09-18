<template>
  <div id="map-info">
    <div id="map">
      <div v-for="(row, y) in map.tiles" :key="y" class="row">
        <div
          v-for="(tile, x) in row"
          :key="x"
          class="tile"
          :class="{ active: tile, inactive: !tile }"
        ></div>
      </div>
    </div>
    <p>width: {{ map.width }} height: {{ map.height }}</p>
  </div>
</template>

<script lang="ts" setup>
import type { NeonMap } from '@/assets/types/map'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const props = defineProps<{
  map: NeonMap
}>()
</script>

<style scoped>
#map-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  width: fit-content;
}

#map {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

.row {
  display: flex;
  align-items: flex-start;
}

.tile {
  width: 20px;
  height: 20px;
}

.active {
  background-color: black;
}

.inactive {
  background-color: white;
}
</style>
