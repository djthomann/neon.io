import { ref } from 'vue'
import { defineStore } from 'pinia'
import * as THREE from 'three'

export const useSceneStore = defineStore('scene', () => {
    const camera = ref<THREE.PerspectiveCamera | null>(null)
  
    function setCamera(cam: THREE.PerspectiveCamera) {
      camera.value = cam
    }
  
    function getCameraDirection() {
      if (!camera.value) return null
      const dir = new THREE.Vector3()
      camera.value.getWorldDirection(dir)
      return dir
    }
  
    return { camera, setCamera, getCameraDirection }
  })