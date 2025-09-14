<template>

<h2>Lobby: {{ lobby.name }}</h2>
<h3>This is a lobby, 0/{{ lobby.capacity }} players</h3>
<RouterLink :to="{name: 'lobby', params: {id: lobby.id}}">Join Lobby</RouterLink>

</template>

<script lang="ts" setup>
import { defineComponent } from 'vue'
import { RouterLink } from 'vue-router';
import { Lobby } from '../assets/types/lobby';

const props = defineProps<{lobby: Lobby}>()



</script>

<style scoped>

</style>