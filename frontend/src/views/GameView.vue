<template>
  <div ref="sceneContainer" id="scene"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import * as THREE from 'three'
import { useLobbyStore } from '@/stores/lobbies'

const lobbyStore = useLobbyStore()
const map = lobbyStore.selectedLobby?.map
const scale = 1
const wallHeight = 1

const sceneContainer = ref<HTMLDivElement | null>(null)

const scene = new THREE.Scene()
const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000)
camera.position.set(1, 0, 1)
if (map) {
  // camera.position.set(map.width / 2, 2, map.height / 2)
}
// camera.lookAt(1, 0, 0)

const renderer = new THREE.WebGLRenderer()
renderer.setSize(window.innerWidth, window.innerHeight)
sceneContainer.value?.appendChild(renderer.domElement)

// Ground
if (map) {
  const planeGeometry = new THREE.PlaneGeometry(map?.width * scale, map?.height * scale)
  const planeMaterial = new THREE.MeshBasicMaterial({ color: 0x333333 })
  const plane = new THREE.Mesh(planeGeometry, planeMaterial)
  plane.rotation.x = -Math.PI / 2
  plane.position.set((map.width / 2) * scale, -0.5 * scale, (map.height / 2) * scale)
  scene.add(plane)
}

// Tiles
if (map) {
  const geometry = new THREE.BoxGeometry(1 * scale, wallHeight * scale, 1 * scale)
  const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 })
  for (let y = 0; y < map.tiles.length; y++) {
    for (let x = 0; x < map.tiles[y].length; x++) {
      if (map.tiles[y][x]) {
        const cube = new THREE.Mesh(geometry, material)
        cube.position.set(x * scale + scale / 2, 0, y * scale + scale / 2)
        // In Szene einfügen
        scene.add(cube)
      }
    }
  }
}

// Walls --> When looking into +x direction
if (map) {
  const planeMaterial = new THREE.MeshBasicMaterial({ color: 0x999999 })
  const planeGeometry = new THREE.PlaneGeometry(map.width * scale, (map.height / 2) * scale)

  // --- Top ---
  const topPlane = new THREE.Mesh(planeGeometry, planeMaterial)
  topPlane.rotation.y = Math.PI // nach innen drehen
  topPlane.position.set(
    (map.width / 2) * scale,
    (map.height / 4) * scale - 0.5 * scale,
    map.height * scale,
  )
  scene.add(topPlane)

  // --- Bottom ---
  const bottomPlane = new THREE.Mesh(planeGeometry, planeMaterial)
  bottomPlane.position.set((map.width / 2) * scale, (map.height / 4) * scale - 0.5 * scale, 0)
  scene.add(bottomPlane)

  // --- Left ---
  const leftPlane = new THREE.Mesh(planeGeometry, planeMaterial)
  leftPlane.rotation.y = Math.PI / 2
  leftPlane.position.set(0, (map.height / 4) * scale - 0.5 * scale, (map.height / 2) * scale)
  scene.add(leftPlane)

  // --- Right ---
  const rightPlane = new THREE.Mesh(planeGeometry, planeMaterial)
  rightPlane.rotation.y = -Math.PI / 2
  rightPlane.position.set(
    map.width * scale,
    (map.height / 4) * scale - 0.5 * scale,
    (map.height / 2) * scale,
  )
  scene.add(rightPlane)
}

// Roof
if (map) {
  const planeGeometry = new THREE.PlaneGeometry(map?.width * scale, map?.height * scale)
  const planeMaterial = new THREE.MeshBasicMaterial({ color: 0xff0000 })
  const plane = new THREE.Mesh(planeGeometry, planeMaterial)
  plane.rotation.x = Math.PI / 2
  plane.position.set(
    (map.width / 2) * scale,
    (map.height / 2) * scale - 0.5 * scale,
    (map.height / 2) * scale,
  )
  scene.add(plane)
}

// MOVEMENT

const speed = 0.1

const keys: Record<string, boolean> = {}

window.addEventListener('keydown', (event) => {
  keys[event.key.toLowerCase()] = true
})

window.addEventListener('keyup', (event) => {
  keys[event.key.toLowerCase()] = false
})

function updateCamera() {
  const forward = new THREE.Vector3()
  camera.getWorldDirection(forward)

  const right = new THREE.Vector3()
  right.crossVectors(forward, camera.up).normalize()

  // forward / backward
  if (keys['w']) camera.position.add(forward.clone().multiplyScalar(speed))
  if (keys['s']) camera.position.add(forward.clone().multiplyScalar(-speed))

  // left / right
  if (keys['a']) camera.position.add(right.clone().multiplyScalar(-speed))
  if (keys['d']) camera.position.add(right.clone().multiplyScalar(speed))

  // up / down
  if (keys[' ']) camera.position.y += speed
  if (keys['shift']) camera.position.y -= speed
}

const euler = new THREE.Euler(0, 0, 0, 'YXZ')

function onMouseMove(event: MouseEvent) {
  const sensitivity = 0.002
  euler.setFromQuaternion(camera.quaternion)

  euler.y -= event.movementX * sensitivity
  euler.x -= event.movementY * sensitivity
  euler.x = Math.max(-Math.PI / 2, Math.min(Math.PI / 2, euler.x)) // Clamp

  camera.quaternion.setFromEuler(euler)
}

onMounted(() => {
  document.body.addEventListener('click', () => {
    document.body.requestPointerLock()
  })

  document.addEventListener('mousemove', (event) => {
    if (document.pointerLockElement === document.body) {
      onMouseMove(event)
    }
  })
})

onUnmounted(() => {
  document.body.removeEventListener('click', () => {
    document.body.requestPointerLock()
  })

  document.removeEventListener('mousemove', (event) => {
    if (document.pointerLockElement === document.body) {
      onMouseMove(event)
    }
  })
})

function animate() {
  updateCamera()
  renderer.render(scene, camera)
}
renderer.setAnimationLoop(animate)

onMounted(() => {
  sceneContainer.value?.appendChild(renderer.domElement)
})
</script>

<style scoped>
#scene {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}
</style>
