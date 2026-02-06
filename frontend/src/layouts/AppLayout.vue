<template>
  <div>
    <header class="bg-white border-b border-slate-200">
      <div class="max-w-6xl mx-auto px-4 py-3 flex justify-between items-center">
        <router-link to="/dashboard" class="font-semibold">Tools Hub</router-link>
        <div class="text-sm flex items-center gap-3">
          <span>{{ authStore.me?.username }} ({{ authStore.me?.role }})</span>
          <router-link to="/profile" class="text-brand">修改密码</router-link>
          <button class="text-red-600" @click="doLogout">退出</button>
        </div>
      </div>
    </header>
    <main class="max-w-6xl mx-auto px-4 py-6">
      <slot />
    </main>
  </div>
</template>

<script setup>
import { authStore } from '../stores/auth'
import { useRouter } from 'vue-router'
const router = useRouter()
async function doLogout() {
  await authStore.logout()
  router.push('/login')
}
</script>
