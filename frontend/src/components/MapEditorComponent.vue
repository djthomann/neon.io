<template>
  <div class="input-div">
    <label> Name: </label>
    <input type="text" v-model="name" />
  </div>
  <div class="controls">
    <div class="input-div">
      <label> Width: </label>
      <input type="number" v-model.number="width" min="5" max="25" />
    </div>
    <div class="input-div">
      <label> Height: </label>
      <input type="number" v-model.number="height" min="5" max="25" />
    </div>
  </div>

  <div class="map">
    <div v-for="(row, y) in map" :key="y" class="row">
      <div
        v-for="(tile, x) in row"
        :key="x"
        class="tile"
        :class="{ active: tile, inactive: !tile }"
        @mousedown="startDrag(tile, y, x)"
        @mouseenter="dragOver(y, x)"
        draggable="false"
        @dragstart.prevent
      ></div>
    </div>
  </div>
  <button @click="postMap">Post Map</button>
  <button @click="downloadMap">Download</button>
</template>

<script lang="ts" setup>
import { onMounted, ref, watch } from 'vue'

const name = ref<string>('New Map')
const width = ref<number>(5)
const height = ref<number>(5)

const map = ref<boolean[][]>([])

const isDragging = ref(false)
const dragValue = ref<boolean | null>(null)

function generateMap() {
  map.value = Array.from({ length: height.value }, () =>
    Array.from({ length: width.value }, () => false),
  )
}

async function postMap() {
  const text = fileText()

  const blob = new Blob([text], { type: 'text/plain' })

  const formData = new FormData()
  formData.append('file', blob, 'map.txt')
  formData.append('name', name.value)

  const response = await fetch('/api/maps/upload', {
    method: 'POST',
    body: formData,
  })

  if (!response.ok) {
    throw new Error(`Upload fehlgeschlagen: ${response.statusText}`)
  }

  return response.json()
}

function fileText() {
  return map.value.map((row) => row.map((cell) => (cell ? '1' : '0')).join('')).join('\n')
}

function downloadMap() {
  // 0 = false, 1 = true
  const text = fileText()

  const blob = new Blob([text], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)

  const a = document.createElement('a')
  a.href = url
  a.download = name.value ? name.value + '.txt' : 'map.txt'
  a.click()

  URL.revokeObjectURL(url)
}

function toggleTile(y: number, x: number, value?: boolean) {
  map.value[y][x] = value !== undefined ? value : !map.value[y][x]
}

function startDrag(value: boolean, y: number, x: number) {
  toggleTile(y, x)
  dragValue.value = !value
  isDragging.value = true
}

function stopDrag() {
  isDragging.value = false
}

function dragOver(y: number, x: number) {
  if (isDragging.value && dragValue.value != map.value[y][x]) {
    toggleTile(y, x)
  }
}

watch([width, height], () => {
  generateMap()
})

onMounted(() => {
  window.addEventListener('mouseup', stopDrag)
})

generateMap()

onMounted(() => {})
</script>

<style scoped>
.controls {
  display: flex;
  justify-content: space-between;
}
.input-div {
  width: 40%;
  display: flex;
  flex-direction: column;
}

.map {
  width: 90%;

  padding: 5%;
}

.row {
  width: 100%;
  background-color: green;
  display: flex;
  align-items: flex-start;
}

.tile {
  flex: 1;
  aspect-ratio: 1 / 1;
  border: 1px solid black;
  box-sizing: border-box;
}

.tile:hover {
  cursor: pointer;
}

.active {
  background-color: black;
}

.inactive {
  background-color: white;
}
</style>
