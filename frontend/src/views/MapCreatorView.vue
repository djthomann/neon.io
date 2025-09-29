<template>
  <div class="column">
    <h1>Welcome to the map creator</h1>
    <MapEditorComponent />
  </div>
</template>

<script lang="ts" setup>
import MapEditorComponent from '@/components/MapEditorComponent.vue'
</script>

<style>
.column {
  height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>
