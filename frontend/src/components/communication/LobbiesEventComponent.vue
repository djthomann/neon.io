<template>

</template>

<script lang="ts" setup>
import type { LobbiesState } from '@/assets/types/events/lobbiesState';
import type { Lobby } from '@/assets/types/lobby';
import { useLobbyStore } from '@/stores/lobbies';
import { Client, type IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { onUnmounted } from 'vue';

const lobbyStore = useLobbyStore()

const socket = new SockJS('/ws')

const stompClient = new Client({
  webSocketFactory: () => socket,
  reconnectDelay: 5000, // optional
})


stompClient.onConnect = (frame) => {
  console.log('Subscriptions activated!')

  // Subscription for lobby updates
  stompClient.subscribe(`/topic/lobbies`, (msg: IMessage) => {
    const data: LobbiesState = JSON.parse(msg.body)
    lobbyStore.lobbyList = data.lobbies
    console.log('Lobbies:', data.lobbies)
  })

}

// Activate client
stompClient.activate()

onUnmounted(() => {
  // Deactive client
  console.log('Connection deactivated')
  stompClient.deactivate()
})

</script>

<style scoped></style>