<template>
  <AppLayout>
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-semibold">Password Vault Lite</h1>
      <button class="btn" @click="openCreate">新增</button>
    </div>
    <input class="input mb-4" v-model="q" @input="load" placeholder="搜索 website / username / description" />
    <div v-if="items.length===0" class="card p-6 text-slate-500">暂无记录</div>
    <div class="space-y-3">
      <div v-for="it in items" :key="it.id" class="card p-4">
        <div class="flex justify-between">
          <div>
            <a :href="it.websiteUrl" target="_blank" class="font-semibold text-brand">{{ it.websiteUrl }}</a>
            <div class="text-sm text-slate-600">{{ it.description }}</div>
          </div>
          <div class="space-x-2">
            <button @click="edit(it)" class="text-brand">编辑</button>
            <button @click="removeItem(it.id)" class="text-red-600">删除</button>
          </div>
        </div>
        <div class="mt-3 text-sm">用户名: {{ it.username }} <button class="ml-2 text-brand" @click="copy(it.username)">复制</button></div>
        <div class="mt-1 text-sm">密码: {{ show[it.id] ? it.password : '••••••••' }}
          <button class="ml-2 text-brand" @click="show[it.id]=!show[it.id]">{{ show[it.id] ? '隐藏' : '显示' }}</button>
          <button class="ml-2 text-brand" @click="copy(it.password)">复制</button>
        </div>
      </div>
    </div>

    <div v-if="modal" class="fixed inset-0 bg-black/30 flex items-center justify-center p-4">
      <div class="card w-full max-w-lg p-4 space-y-3">
        <h3 class="font-semibold">{{ form.id ? '编辑记录' : '新增记录' }}</h3>
        <input class="input" v-model="form.websiteUrl" placeholder="网站 URL" />
        <input class="input" v-model="form.username" placeholder="用户名" />
        <input class="input" v-model="form.password" placeholder="密码" />
        <textarea class="input" v-model="form.description" placeholder="描述"></textarea>
        <div class="flex justify-end gap-2"><button @click="modal=false">取消</button><button class="btn" @click="save">保存</button></div>
      </div>
    </div>
  </AppLayout>
</template>
<script setup>
import { reactive, ref } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import http from '../api/http'
import { toast } from '../stores/toast'
const items = ref([]); const q = ref(''); const show = reactive({}); const modal = ref(false)
const form = reactive({ id:null, websiteUrl:'', username:'', password:'', description:'' })
async function load(){ items.value=(await http.get('/tools/vault',{params:{q:q.value||undefined}})).data }
load()
function openCreate(){ Object.assign(form,{id:null,websiteUrl:'',username:'',password:'',description:''}); modal.value=true }
function edit(it){ Object.assign(form,it); modal.value=true }
async function save(){ if(form.id) await http.put(`/tools/vault/${form.id}`,form); else await http.post('/tools/vault',form); toast('已保存'); modal.value=false; load() }
async function removeItem(id){ if(!confirm('确认删除?')) return; await http.delete(`/tools/vault/${id}`); toast('已删除'); load() }
async function copy(text){ await navigator.clipboard.writeText(text); toast('已复制') }
</script>
