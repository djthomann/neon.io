<template></template>

<script lang="ts" setup>
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

function sendMovement(vector: THREE.Vector3) {
  if (stompClient && stompClient.connected) {
    const url: string = `/app/game/${props.id}/move`
    const body: Movement = { playerId: userStore.id, vector: vector }

    stompClient.publish({
      destination: url,
      body: JSON.stringify(body),
    })
  } else {
    console.warn('STOMP client not connected yet.')
  }
}
const userStore = useUserStore()

const speed = 0.1

const keys: Record<string, boolean> = {}

function checkKeys() {
  const cameraDir = useSceneStore().getCameraDirection()?.normalize()
  const up = new THREE.Vector3(0, 1, 0)

  // direction on plane
  const forward = new THREE.Vector3(cameraDir?.x, 0, cameraDir?.z).normalize()
  const right = new THREE.Vector3().crossVectors(forward, up).normalize()
  const movement = new THREE.Vector3()

  if (keys['w']) movement.add(forward)
  if (keys['s']) movement.sub(forward)
  if (keys['a']) movement.sub(right)
  if (keys['d']) movement.add(right)
  if (keys[' ']) movement.add(up)
  if (keys['shift']) movement.sub(up)

  // Don't send idle movement
  if (movement.lengthSq() > 0) {
    movement.normalize()
    sendMovement(movement)
  }
}

function handleKeyDown(event: KeyboardEvent) {
  keys[event.key.toLowerCase()] = true
}

function handleKeyUp(event: KeyboardEvent) {
  keys[event.key.toLowerCase()] = false
}

let intervalId: number
onMounted(() => {
  window.addEventListener('keydown', handleKeyDown)
  window.addEventListener('keyup', handleKeyUp)

  intervalId = setInterval(checkKeys, 30)
})

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeyDown)
  window.removeEventListener('keyup', handleKeyUp)

  clearInterval(intervalId)
})

stompClient.activate()
</script>

<style></style>
