<template>
    <div v-if="visible" class="fade">
      HELLO
    </div>
  </template>
  
<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const props = defineProps({
    visible: {
    type: Boolean,
    required: true
    }
})

  
  </script>
  
  <style scoped>
  .fade {
    position: absolute;
    width: 30%;
    height: 30%;
    background-color: black;
    transition: opacity 0.3s ease;
    opacity: 1;
  }
  .fade[v-cloak], 
  .fade[style*="display: none"] {
    opacity: 0;
  }
  </style>
  