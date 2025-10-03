<template>
  <section>
    <div id="content" @click="joinLobby">
      <h2>Lobby {{ lobby.id }}</h2>
      <div id="bottom">
        <h3>{{ lobby.players.length }}/{{ lobby.capacity }} players</h3>
      </div>
    </div>
    <div id="ghost"></div>
  </section>
</template>

<script lang="ts" setup>
import { useLobbyStore } from '@/stores/lobbies'
import type { Lobby } from '../assets/types/lobby'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const lobbyStore = useLobbyStore()
const userStore = useUserStore()

const props = defineProps<{ lobby: Lobby }>()

async function postJoinLobby(lobbyId: number): Promise<Boolean> {
  const res = await fetch(`/api/lobbies/${lobbyId}/join?playerId=${userStore.id}`, {
    method: 'POST',
  })
  return res.ok
}

async function joinLobby() {
  const success = await postJoinLobby(props.lobby.id)
  if (success) {
    lobbyStore.selectedLobby = props.lobby
    router.push({ name: 'lobby', params: { id: props.lobby.id } })
  } else {
    console.log("Couldn't join")
  }
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
  display: flex;
}

#content {
  padding: 10%;
  width: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 2px;
  background-color: black;
  transition: 0.2s ease-in-out;
}

#content:hover {
  cursor: pointer;
  color: black;
  background-color: var(--primary);
  border-radius: 2px;
  transform: scale(1.1);
  /* transform: translate3d(10px, -10px, 0); */
}

#ghost {
  position: absolute;
  top: 0;
  z-index: -1;
  width: 100%;
  height: 100%;
  border-radius: 2px;
  background-color: var(--primary);
  transition: 0.2s ease-in-out;
}

#content:hover + #ghost {
  box-shadow: 0 0 10px var(--primary), 0 0 30px var(--primary), 
              0 0 60px var(--primary), 0 0 100px var(--primary);
}

#bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
