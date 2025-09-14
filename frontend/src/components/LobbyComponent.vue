<template>
  <section>
    <div id="content" @click="joinLobby">
      <h2>Lobby {{ lobby.id }}</h2>
      <div id="bottom">
        <h3>0/{{ lobby.capacity }} players</h3>
      </div>
    </div>
    <div id="ghost"></div>
  </section>
</template>

<script lang="ts" setup>
import { defineComponent } from 'vue'
import { RouterLink } from 'vue-router'
import type { Lobby } from '../assets/types/lobby'
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps<{ lobby: Lobby }>()

function joinLobby() {
  router.push({ name: 'lobby', params: { id: props.lobby.id } })
}
</script>

<style scoped>
h2,
h3 {
  padding: 0;
  margin: 0;
}

section {
  position: relative;
}

#content {
  padding: 5%;
  display: flex;
  flex-direction: column;
  background-color: black;
  transition: transform 200ms;
}

#content:hover {
  cursor: pointer;
  transform: translate3d(10px, -10px, 0);
}

#ghost {
  position: absolute;
  top: 0;
  z-index: -1;
  width: 100%;
  height: 100%;
  background-color: green;
}

#bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
