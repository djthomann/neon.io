<template>

</template>

<script lang="ts" setup>
import type { GamePosition } from '@/assets/types/events/gamePosition';
import type { LobbiesState } from '@/assets/types/events/lobbiesState';
import { useGameStore } from '@/stores/game';
import { useUserStore } from '@/stores/user';
import { Client, type IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { onUnmounted } from 'vue';

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
})

const gameStore = useGameStore()
const userStore = useUserStore()

const socket = new SockJS('/ws')

const stompClient = new Client({
  webSocketFactory: () => socket,
  reconnectDelay: 5000, // optional
})


stompClient.onConnect = (frame) => {
  console.log('Subscriptions activated!')

  // Subscription for game updates
  stompClient.subscribe(`/topic/game/${props.id}`, (msg: IMessage) => {
    const data: GamePosition = JSON.parse(msg.body)
  
    if(data.id === userStore.id ) {
      gameStore.x = data.x
      gameStore.y = data.y
      gameStore.z = data.z
    } else {
      for(const player of gameStore.players!) {
        if(player.id === data.id) {
          player.x = data.x
          player.y = data.y
          player.z = data.z
        }
      }
    }
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