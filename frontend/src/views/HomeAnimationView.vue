<template>
  <div ref="sceneContainer" id="scene"></div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as THREE from 'three'

const sceneContainer = ref<HTMLDivElement | null>(null)
let renderer: THREE.WebGLRenderer
let animationFrameId: number
let time: number = 0
let isAnimationActive = true

onMounted(async () => {
  if (!sceneContainer.value) return

  const width = sceneContainer.value.clientWidth
  const height = sceneContainer.value.clientHeight

  const scene = new THREE.Scene()

  const camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 1000)
  camera.position.z = 3

  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(width, height)
  sceneContainer.value.appendChild(renderer.domElement)

  const ambientLight = new THREE.AmbientLight(0xffffff, 0.5)
  scene.add(ambientLight)

  const directionalLight = new THREE.DirectionalLight(0xffffff, 1)
  directionalLight.position.set(5, 5, 5)
  scene.add(directionalLight)

  const geometry = new THREE.OctahedronGeometry(1.5)
  const material = new THREE.MeshPhongMaterial({
    color: 0x00ff00,
    emissive: 0x00ff00,
    emissiveIntensity: 0.6,
  })

  const cube = new THREE.Mesh(geometry, material)
  scene.add(cube)

  const animate = () => {
    animationFrameId = requestAnimationFrame(animate)

    cube.rotation.x += 0.0025
    cube.rotation.y += 0.0025

    time += 0.01
    cube.position.y = Math.sin(time) * 0.25

    renderer.render(scene, camera)
  }
  if (isAnimationActive) {
    animate()
  }

  const onResize = () => {
    if (!sceneContainer.value) return
    const width = sceneContainer.value.clientWidth
    const height = sceneContainer.value.clientHeight
    camera.aspect = width / height
    camera.updateProjectionMatrix()
    renderer.setSize(width, height)
  }

  window.addEventListener('resize', onResize)

  onBeforeUnmount(() => {
    cancelAnimationFrame(animationFrameId)
    window.removeEventListener('resize', onResize)
    renderer.dispose()
    sceneContainer.value?.removeChild(renderer.domElement)
  })
})
</script>

<style>
#scene {
  width: 60%;
  height: 100vh;
}
</style>
