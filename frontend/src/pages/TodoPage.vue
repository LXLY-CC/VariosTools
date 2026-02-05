<template>
  <AppLayout>
    <h1 class="text-2xl font-semibold mb-4">Todo Memo</h1>
    <div class="card p-4 mb-4 space-y-2">
      <textarea class="input" rows="3" v-model="draft" placeholder="新增待办"></textarea>
      <button class="btn" @click="create">添加</button>
    </div>
    <div class="space-y-2">
      <div v-for="t in list" :key="t.id" class="card p-3 flex items-start justify-between gap-3">
        <div class="flex-1">
          <textarea class="input" rows="2" v-model="t.content"></textarea>
          <div class="text-xs text-slate-500 mt-1" :class="{'line-through':t.completed}">{{ t.completed ? '已完成' : '未完成' }}</div>
        </div>
        <div class="space-y-2 text-sm">
          <button class="text-brand block" @click="toggle(t)">{{ t.completed ? '取消完成' : '完成' }}</button>
          <button class="text-brand block" @click="save(t)">保存</button>
          <button class="text-red-600 block" @click="removeTodo(t.id)">删除</button>
        </div>
      </div>
    </div>
  </AppLayout>
</template>
<script setup>
import { ref } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import http from '../api/http'
import { toast } from '../stores/toast'
const list = ref([]); const draft = ref('')
async function load(){ list.value = (await http.get('/tools/todo')).data }
load()
async function create(){ await http.post('/tools/todo',{content:draft.value}); draft.value=''; load() }
async function save(t){ await http.put(`/tools/todo/${t.id}`,{content:t.content,completed:t.completed}); toast('已保存') }
async function toggle(t){ await http.patch(`/tools/todo/${t.id}/status`,null,{params:{completed:!t.completed}}); load() }
async function removeTodo(id){ if(!confirm('确认删除?')) return; await http.delete(`/tools/todo/${id}`); load() }
</script>
