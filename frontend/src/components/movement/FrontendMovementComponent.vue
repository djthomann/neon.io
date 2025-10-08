<template>
</template>

<script lang="ts" setup>
import type { Movement } from '@/assets/types/events/movement'
import { MovementDirection } from '@/assets/types/events/movementDirections'
import { useGameStore } from '@/stores/game'
import { useSceneStore } from '@/stores/scene'
import { useUserStore } from '@/stores/user'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
})

const socket = new SockJS('/ws')

const stompClient = new Client({
  webSocketFactory: () => socket,
  reconnectDelay: 5000, // optional
})


stompClient.onConnect = (frame) => {
  console.log('Client activated!')
}

function sendMovement(dir: MovementDirection) {
  if (stompClient && stompClient.connected) {

    const url: string = `/app/game/${props.id}/move`
    const body: Movement = {playerId: userStore.id, direction: dir}

    stompClient.publish({
      destination: url,
      body: JSON.stringify(body)
    })
  } else {
    console.warn('STOMP client not connected yet.')
  }
}

const gameStore = useGameStore()

const userStore = useUserStore()

const speed = 0.1

const keys: Record<string, boolean> = {}

window.addEventListener('keydown', (event) => {
  keys[event.key.toLowerCase()] = true
})

window.addEventListener('keyup', (event) => {
  keys[event.key.toLowerCase()] = false
})

function checkKeys() {
  if (keys['w']) sendMovement(MovementDirection.FORWARD)
  if (keys['a']) sendMovement(MovementDirection.LEFT)
  if (keys['s']) sendMovement(MovementDirection.BACKWARD)
  if (keys['d']) sendMovement(MovementDirection.RIGHT)
  if (keys[' ']) sendMovement(MovementDirection.UP)
  if (keys['shift']) sendMovement(MovementDirection.DOWN)
  // console.log(useSceneStore().getCameraDirection())
}
setInterval(checkKeys, 30);

stompClient.activate()

</script>

<style></style>