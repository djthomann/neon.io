<template>
  <div class="map-creator">
    <NavBarComponent />
  <div class="column">
    
    <h1>Welcome to the map creator</h1>
    <MapEditorComponent />
  </div>
  </div>
  
</template>

<script lang="ts" setup>
import MapEditorComponent from '@/components/MapEditorComponent.vue'
import NavBarComponent from '@/components/NavBarComponent.vue';
</script>

<style>
.map-creator {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.column {
  width: 30%;
  display: flex;
  flex-direction: column;
}
</style>
