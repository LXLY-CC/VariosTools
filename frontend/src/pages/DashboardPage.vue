<template>
  <AppLayout>
    <h1 class="text-2xl font-semibold mb-4">工具仪表盘</h1>
    <div class="grid md:grid-cols-2 gap-4">
      <router-link v-for="t in tools" :key="t.code" :to="t.route" class="card p-5 hover:shadow-md transition">
        <h2 class="font-semibold text-lg">{{ t.name }}</h2>
        <p class="text-slate-600 mt-2">{{ t.description }}</p>
      </router-link>
    </div>
  </AppLayout>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import http from '../api/http'
const tools = ref([])
onMounted(async () => { tools.value = (await http.get('/tools')).data })
</script>
