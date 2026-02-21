<template></template>

<script lang="ts" setup>
import type { Attack } from '@/assets/types/events/attack'
import type { Movement } from '@/assets/types/events/movement'
import { MovementDirection } from '@/assets/types/events/movementDirections'
import { useGameStore } from '@/stores/game'
import { useSceneStore } from '@/stores/scene'
import { useUserStore } from '@/stores/user'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import * as THREE from 'three'
import { onMounted, onUnmounted } from 'vue'

const props = defineProps({
  id: {
    type: Number,
    required: true,
  },
})

const socket = new SockJS('/ws')

const stompClient = new Client({
  webSocketFactory: () => socket,
  reconnectDelay: 5000, // optional
})

stompClient.onConnect = (frame) => {
  console.log('Client activated!')
}

function sendAttack() {
  const cameraDir = useSceneStore().getCameraDirection()?.normalize()

  if (cameraDir && stompClient && stompClient.connected) {
    const url: string = `/app/game/${props.id}/attack`
    const body: Attack = { playerId: userStore.id, vector: cameraDir }

    console.log('SENDING ATTACK')

    stompClient.publish({
      destination: url,
      body: JSON.stringify(body),
    })
  } else {
    console.warn('STOMP client not connected yet.')
  }
}
const userStore = useUserStore()

const keys: Record<string, boolean> = {}

onMounted(() => {
  window.addEventListener('click', sendAttack)
})

onUnmounted(() => {
  window.removeEventListener('click', sendAttack)
})

stompClient.activate()
</script>

<style></style>
