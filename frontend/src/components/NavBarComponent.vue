<template>
    <nav>
      <div class="nav-elements">
        <div class="main-elements">
          <a @click="routerBack"><ChevronLeft /></a>
        <slot name="title"><h1>{{ routeTitle }}</h1></slot>
        </div>
        <slot name="buttons"></slot>
      </div>
      <UserComponent />
    </nav>
</template>

<script lang="ts" setup>
import { ChevronLeft } from 'lucide-vue-next';
import { useRoute, useRouter } from 'vue-router';
import UserComponent from './UserComponent.vue';
import { computed, onMounted, onUnmounted } from 'vue';

const router = useRouter()
const route = useRoute()
const routeTitle = computed(() => route.meta.title ?? 'Title not found')

function handleEscape(event: KeyboardEvent) {
  if (event.key === 'Escape') {
    routerBack()
  }
}

function routerBack() {
  router.back()
}

onMounted(() => {
  window.addEventListener('keydown', handleEscape)
})

onUnmounted(() => {
  window.removeEventListener('keydown', handleEscape)
})

</script>

<style scoped>
nav {
    padding: 0 4%;
    width: 92%;
    height: 10vh;
    background-color: black;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.main-elements {
  display: flex;
  align-items: center;
  gap: 10px;
}

.nav-elements {
  display: flex;
  flex-direction: row;
  gap: 25px;
  align-items: center;
  justify-content: space-between;
}

</style>