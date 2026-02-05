<template>
  <div class="min-h-screen flex items-center justify-center p-4">
    <div class="card w-full max-w-md p-6 space-y-3">
      <h1 class="text-xl font-semibold">使用管理员提供的重置 Token</h1>
      <input class="input" v-model="token" placeholder="Reset Token" />
      <input class="input" v-model="newPassword" placeholder="新密码" type="password" />
      <button class="btn w-full" @click="submit">提交</button>
      <router-link class="text-sm text-brand" to="/login">返回登录</router-link>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import http from '../api/http'
import { toast } from '../components/ToastStack.vue'
const token = ref(''); const newPassword = ref('')
async function submit(){ await http.post('/auth/reset-password/complete',{token:token.value,newPassword:newPassword.value}); toast('重置成功，请登录') }
</script>
