<template>
  <AppLayout>
    <h1 class="text-2xl font-semibold mb-4">修改我的密码</h1>
    <div class="card p-4 max-w-lg space-y-2">
      <input class="input" v-model="currentPassword" type="password" placeholder="当前密码" />
      <input class="input" v-model="newPassword" type="password" placeholder="新密码（至少8位）" />
      <button class="btn" @click="submit">更新密码</button>
    </div>
  </AppLayout>
</template>
<script setup>
import { ref } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import http from '../api/http'
import { toast } from '../stores/toast'
const currentPassword = ref('')
const newPassword = ref('')
async function submit(){ await http.post('/auth/change-password',{currentPassword:currentPassword.value,newPassword:newPassword.value}); toast('密码已更新'); currentPassword.value=''; newPassword.value='' }
</script>
