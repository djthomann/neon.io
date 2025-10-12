<template>
    <nav>
      <div class="nav-elements">
        <RouterLink to="/"><</RouterLink>
        <slot name="title"><h1>{{ routeTitle }}</h1></slot>
        <slot name="buttons"></slot>
      </div>
      <UserComponent />
    </nav>
</template>

<script lang="ts" setup>
import { useRoute, useRouter } from 'vue-router';
import UserComponent from './UserComponent.vue';
import { computed } from 'vue';

const router = useRouter()
const route = useRoute()
const routeTitle = computed(() => route.meta.title ?? 'Title not found')

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

.nav-elements {
  display: flex;
  flex-direction: row;
  gap: 25px;
  align-items: center;
  justify-content: space-between;
}

</style>