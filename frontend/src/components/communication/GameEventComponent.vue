<template></template>

<script lang="ts" setup>
import type { GameEnd } from '@/assets/types/events/gameEnd'
import type { GameLaser } from '@/assets/types/events/gameLaser'
import type { PlayerInfo } from '@/assets/types/events/playerInfo'
import type { GameTime } from '@/assets/types/events/gameTime'
import type { LobbiesState } from '@/assets/types/events/lobbiesState'
import { useGameStore } from '@/stores/game'
import { useUserStore } from '@/stores/user'
import { Client, type IMessage } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps({
  id: {
    type: Number,
    required: true,
  },
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

  // Subscription for game position updates
  stompClient.subscribe(`/topic/game/${props.id}/player-info`, (msg: IMessage) => {
    const data: PlayerInfo = JSON.parse(msg.body)

    if (data.id === userStore.id) {
      gameStore.x = data.x
      gameStore.y = data.y
      gameStore.z = data.z
    } else {
      for (const player of gameStore.players!) {
        if (player.id === data.id) {
          player.x = data.x
          player.y = data.y
          player.z = data.z
          player.isHit = data.isHit
        }
      }
    }
  })

  stompClient.subscribe(`/topic/game/${props.id}/lasers`, (msg: IMessage) => {
    const data: GameLaser[] = JSON.parse(msg.body)

    gameStore.lasers = data
  })

  stompClient.subscribe(`/topic/game/${props.id}/time`, (msg: IMessage) => {
    const data: GameTime = JSON.parse(msg.body)

    gameStore.time = data.time
  })

  stompClient.subscribe(`/topic/game/${props.id}/end`, (msg: IMessage) => {
    const data: GameEnd = JSON.parse(msg.body)

    router.push('/game/finished')
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
