<script setup lang="ts">

</script>

<template>
  <div id="home">
    <h1 id="title">neon.io</h1>
    <label for="username">Enter Username:</label>
    <input type="text" name="username"></input>
    <RouterLink to="/lobbies">Join</RouterLink>
  </div>
</template>

<style scoped>

#title {
  font-size: 64pt;
}

#home {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

</style>
