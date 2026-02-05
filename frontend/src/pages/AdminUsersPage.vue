<template>
  <AppLayout>
    <h1 class="text-2xl font-semibold mb-4">管理员 - 用户管理</h1>
    <div class="card p-4 mb-4 grid md:grid-cols-4 gap-2 items-end">
      <input class="input" v-model="form.username" placeholder="用户名" />
      <input class="input" v-model="form.password" placeholder="初始密码" />
      <select class="input" v-model="form.role"><option>USER</option><option>ADMIN</option></select>
      <button class="btn" @click="create">创建用户</button>
    </div>
    <div class="space-y-2">
      <div v-for="u in users" :key="u.id" class="card p-4 flex justify-between items-center">
        <div>{{ u.username }} ({{ u.role }}) - <span :class="u.enabled ? 'text-green-600':'text-red-600'">{{ u.enabled ? '启用':'禁用' }}</span></div>
        <div class="space-x-3 text-sm">
          <button class="text-brand" @click="toggle(u)">{{ u.enabled ? '禁用':'启用' }}</button>
          <button class="text-brand" @click="resetTemp(u)">重置临时密码</button>
          <button class="text-brand" @click="genToken(u)">生成重置Token</button>
        </div>
      </div>
    </div>
  </AppLayout>
</template>
<script setup>
import { reactive, ref } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import http from '../api/http'
import { toast } from '../components/ToastStack.vue'
const users = ref([])
const form = reactive({ username:'', password:'', role:'USER' })
async function load(){ users.value=(await http.get('/admin/users')).data }
load()
async function create(){ await http.post('/admin/users',form); toast('用户已创建'); Object.assign(form,{username:'',password:'',role:'USER'}); load() }
async function toggle(u){ await http.patch(`/admin/users/${u.id}/enabled`,{enabled:!u.enabled}); load() }
async function resetTemp(u){ const temp=prompt('输入临时密码'); if(!temp) return; await http.post(`/admin/users/${u.id}/reset-password`,{tempPassword:temp}); toast('已重置') }
async function genToken(u){ const {data}=await http.post(`/admin/users/${u.id}/reset-token`); await navigator.clipboard.writeText(data.token); toast('Token已复制') }
</script>
