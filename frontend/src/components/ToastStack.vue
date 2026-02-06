<template>
  <div class="fixed right-4 top-4 z-50 space-y-2">
    <div v-for="t in toasts" :key="t.id" class="rounded-xl bg-slate-900 text-white px-4 py-2 shadow">
      {{ t.message }}
    </div>
  </div>
</template>

<script setup>
import { toasts } from '../stores/toast'
</script>
