<template>
  <div class="min-h-screen flex items-center justify-center p-4">
    <div class="card w-full max-w-md p-6 space-y-4">
      <h1 class="text-2xl font-semibold">登录 Tools Hub</h1>
      <input class="input" v-model="form.username" placeholder="用户名" />
      <input class="input" v-model="form.password" placeholder="密码" type="password" />
      <button class="btn w-full" @click="submit" :disabled="loading">{{ loading ? '登录中...' : '登录' }}</button>
      <router-link class="text-sm text-brand" to="/reset-password">我有管理员提供的重置 Token</router-link>
    </div>
  </div>
</template>
<script setup>
import { reactive, ref } from 'vue'
import http from '../api/http'
import { authStore } from '../stores/auth'
import { toast } from '../stores/toast'
import { useRouter } from 'vue-router'
const router = useRouter()
const loading = ref(false)
const form = reactive({ username: '', password: '' })
async function submit() {
  loading.value = true
  try {
    await http.post('/auth/login', form)
    await authStore.fetchMe()
    toast('登录成功')
    router.push('/dashboard')
  } catch (e) { toast(e.response?.data?.error || '登录失败') }
  finally { loading.value = false }
}
</script>
