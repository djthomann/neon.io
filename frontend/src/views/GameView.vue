<template>
    <div ref="sceneContainer" id="scene" @click="lockPointer">
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as THREE from 'three'
import { PointerLockControls } from 'three/examples/jsm/controls/PointerLockControls'

const sceneContainer = ref<HTMLDivElement | null>(null)

const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 0.1, 1000 );
camera.position.set(5, 2, 5);
camera.lookAt(0, 0, 0);

const renderer = new THREE.WebGLRenderer();
renderer.setSize( window.innerWidth, window.innerHeight );
sceneContainer.value?.appendChild(renderer.domElement)

// Test cubes
const geometry = new THREE.BoxGeometry( 1, 1, 1 );
const material = new THREE.MeshBasicMaterial( { color: 0x00ff00 } );
const cubeLD = new THREE.Mesh( geometry, material );
cubeLD.position.set(0, 0, 0)
scene.add( cubeLD );
const cubeRT = new THREE.Mesh( geometry, material );
cubeRT.position.set(10, 0, 10)
scene.add(cubeRT);

// Test plane
const planeGeometry = new THREE.PlaneGeometry(10, 10);

const planeMaterial = new THREE.MeshBasicMaterial( {color: 0xff00ff });
const plane = new THREE.Mesh( planeGeometry, planeMaterial);
plane.rotation.x = -Math.PI / 2;
plane.position.set(5, 0, 5);

scene.add(plane);


const lockPointer = () => {
  controls.lock() // Klick aktiviert Maus
}

const controls = new PointerLockControls(camera, renderer.domElement)
controls.lock()

function animate() {
    // plane.rotateX(-0.001)
  renderer.render( scene, camera );
}
renderer.setAnimationLoop( animate );

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