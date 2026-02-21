<template>
  <div ref="sceneContainer" id="scene"></div>

  <div class="crosshair">
    <div class="crosshair-line horizontal"></div>
    <div class="crosshair-line vertical"></div>
  </div>

  <GameTimeComponent />
  <GameEventComponent :id="props.id" />
  <FrontendMovementComponent :id="props.id" />
  <AttackComponent :id="props.id" />
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watchEffect, watch } from 'vue'
import * as THREE from 'three'
import { useLobbyStore } from '@/stores/lobbies'
import GameEventComponent from '@/components/communication/GameEventComponent.vue'
import { useGameStore } from '@/stores/game'
import FrontendMovementComponent from '@/components/controls/FrontendMovementComponent.vue'
import { useSceneStore } from '@/stores/scene'
import { useUserStore } from '@/stores/user'
import GameTimeComponent from '@/components/GameTimeComponent.vue'
import AttackComponent from '@/components/controls/AttackComponent.vue'

const props = defineProps({
  id: {
    type: Number,
    required: true,
  },
})

const gameStore = useGameStore()
const sceneStore = useSceneStore()
const lobbyStore = useLobbyStore()
const userStore = useUserStore()

const map = lobbyStore.selectedLobby?.map
const scale = 1
const wallHeight = 2.5
const roofHeight = 3

const sceneContainer = ref<HTMLDivElement | null>(null)

const scene = new THREE.Scene()

const ambientLight = new THREE.AmbientLight(0xffffff, 0.6)
scene.add(ambientLight)

// const pointLight = new THREE.PointLight(0xffffff, 1, 50)
// pointLight.position.set(5, 3, 5)
// scene.add(pointLight)

// Light Helper
// const pointLightHelper = new THREE.PointLightHelper(pointLight, 1)
// scene.add(pointLightHelper)

const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000)
sceneStore.setCamera(camera)
camera.position.set(gameStore.x, 0, gameStore.y)
if (map) {
  // camera.position.set(map.width / 2, 2, map.height / 2)
}
watch(
  () => [gameStore.x, gameStore.y, gameStore.z],
  ([newX, newY, newZ]) => {
    camera.position.set(newX * scale, newY * scale, newZ * scale)
  },
)

// camera.lookAt(1, 0, 0)

const renderer = new THREE.WebGLRenderer()
renderer.setSize(window.innerWidth, window.innerHeight)
sceneContainer.value?.appendChild(renderer.domElement)

// Players
gameStore.players = lobbyStore.selectedLobby!.players
const playerMeshes = new Map<number, THREE.Mesh>()
watch(
  () => gameStore.players,
  (players) => {
    for (const p of players!) {
      if (p.id === userStore.id) continue
      // mesh already there?
      if (!playerMeshes.has(p.id)) {
        const geometry = new THREE.CylinderGeometry(0.5 * scale, 0.5 * scale, scale, 32)
        const material = new THREE.MeshBasicMaterial({ color: 0x0000ff })
        const mesh = new THREE.Mesh(geometry, material)
        mesh.position.set(5, 5, 5)
        scene.add(mesh)
        playerMeshes.set(p.id, mesh)
      } else {
        // update mesh
        const mesh = playerMeshes.get(p.id)!
        mesh.position.set(p.x, p.y, p.z)
        const material = mesh.material as THREE.MeshBasicMaterial
        if (p.isHit) {
          material.color.set(0xff0000)
        } else {
          material.color.set(0x0000ff)
        }
      }
    }
  },
  { deep: true, immediate: true },
)

// Lasers
const laserLines = new Map<number, THREE.Line | null>()
watch(
  () => gameStore.lasers,
  (lasers) => {
    for (const l of lasers) {
      let laserline = laserLines.get(l.shotAt)

      if (!laserline) {
        const origin = new THREE.Vector3(l.origin.x, l.origin.y, l.origin.z)
        const direction = new THREE.Vector3(l.direction.x, l.direction.y, l.direction.z).normalize()

        const end = new THREE.Vector3().addVectors(origin, direction.normalize().multiplyScalar(10))

        const geometry = new THREE.BufferGeometry().setFromPoints([l.origin, end])
        const material = new THREE.LineBasicMaterial({ color: 0x00ff00 })

        const line = new THREE.Line(geometry, material)
        scene.add(line)
        laserLines.set(l.shotAt, line)
      } else {
        scene.remove(laserline)
        laserLines.set(l.shotAt, null)
      }
    }
  },
  { deep: true, immediate: true },
)

// Ground
/*if (map) {
  const planeGeometry = new THREE.PlaneGeometry(map?.width * scale, map?.height * scale)
  const planeMaterial = new THREE.MeshBasicMaterial({ color: 0x333333 })
  const plane = new THREE.Mesh(planeGeometry, planeMaterial)
  plane.rotation.x = -Math.PI / 2
  plane.position.set((map.width / 2) * scale, -0.5 * scale, (map.height / 2) * scale)
  scene.add(plane)
}*/

// Tiles
if (map) {
  const geometry = new THREE.BoxGeometry(1 * scale, wallHeight * scale, 1 * scale)
  const material = new THREE.MeshStandardMaterial({
    color: 0x00ff00,
    // emissive: 0x00ff00,
    emissiveIntensity: 0.1,
    side: THREE.DoubleSide,
    metalness: 0.1,
    roughness: 1,
  })
  for (let y = 0; y < map.tiles.length; y++) {
    for (let x = 0; x < map.tiles[y].length; x++) {
      if (map.tiles[y][x]) {
        const cube = new THREE.Mesh(geometry, material)
        cube.position.set(x * scale + scale / 2, 0, y * scale + scale / 2)

        scene.add(cube)
      } else {
        const planeGeometry = new THREE.PlaneGeometry(scale, scale)
        const planeMaterial = new THREE.MeshBasicMaterial({ color: 0x000000 })
        const plane = new THREE.Mesh(planeGeometry, planeMaterial)

        // Plane is on the ground
        plane.rotation.x = -Math.PI / 2
        plane.position.set(x * scale + scale / 2, -0.5 * scale, y * scale + scale / 2)

        scene.add(plane)
      }
    }
  }
}

// Walls --> When looking into +x direction
if (map) {
  const planeMaterial = new THREE.MeshBasicMaterial({ color: 0x222222 })
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

// Roof --> Not working correctly
// if (map) {
//   const planeGeometry = new THREE.PlaneGeometry(map?.width * scale, map?.height * scale)
//   const planeMaterial = new THREE.MeshBasicMaterial({ color: 0x111111 })
//   const plane = new THREE.Mesh(planeGeometry, planeMaterial)
//   plane.rotation.x = Math.PI / 2
//   plane.position.set((map.width / 2) * scale, (map.height / 2) * scale - 0.5 * scale, 2.5 * scale)
//   // scene.add(plane)
// }
const euler = new THREE.Euler(0, 0, 0, 'YXZ')

function onMouseMove(event: MouseEvent) {
  const sensitivity = 0.002
  euler.setFromQuaternion(camera.quaternion)

  euler.y -= event.movementX * sensitivity
  euler.x -= event.movementY * sensitivity
  euler.x = Math.max(-Math.PI / 2, Math.min(Math.PI / 2, euler.x)) // Clamp

  camera.quaternion.setFromEuler(euler)
}

const onClick = () => {
  document.body.requestPointerLock()
}

const onMouseMoveHandler = (event: MouseEvent) => {
  if (document.pointerLockElement === document.body) {
    onMouseMove(event)
  }
}

onMounted(() => {
  document.body.addEventListener('click', onClick)

  document.addEventListener('mousemove', onMouseMoveHandler)
})

onUnmounted(() => {
  document.body.removeEventListener('click', onClick)

  document.removeEventListener('mousemove', onMouseMoveHandler)
})

function animate() {
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

.crosshair {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  pointer-events: none; /* wichtig → blockiert keine Klicks */
  z-index: 10;
}

/* Linien */
.crosshair-line {
  position: absolute;
  background: white;
}

.crosshair-line.horizontal {
  width: 20px;
  height: 2px;
  left: -10px;
  top: -1px;
}

.crosshair-line.vertical {
  width: 2px;
  height: 20px;
  top: -10px;
  left: -1px;
}
</style>
