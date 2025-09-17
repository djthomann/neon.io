<template>
  <div id="user-info">
    <p>Welcome {{ userStore.username }}! Your id is: {{ userStore.id }}</p>
  </div>
</template>

<script lang="ts" setup>
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>

<style scoped></style>
