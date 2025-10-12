<template>
    <div class="overlay">
        <h1>{{ gameStore.time }}</h1>
    </div>
</template>

<script lang="ts" setup>
import { useGameStore } from '@/stores/game';


const gameStore = useGameStore()

</script>

<style scoped>
.overlay {
    position: absolute;
    top: 0;
    right: 0;
}

</style>