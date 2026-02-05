<template>
  <div class="fixed right-4 top-4 z-50 space-y-2">
    <div v-for="t in toasts" :key="t.id" class="rounded-xl bg-slate-900 text-white px-4 py-2 shadow">
      {{ t.message }}
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
export const toasts = reactive([])
let nextId = 1
export const toast = (message) => {
  const id = nextId++
  toasts.push({ id, message })
  setTimeout(() => {
    const idx = toasts.findIndex(t => t.id === id)
    if (idx >= 0) toasts.splice(idx, 1)
  }, 2500)
}
</script>
