import { reactive } from 'vue'

export const toasts = reactive([])
let nextId = 1

export function toast(message) {
  const id = nextId++
  toasts.push({ id, message })
  setTimeout(() => {
    const idx = toasts.findIndex((t) => t.id === id)
    if (idx >= 0) toasts.splice(idx, 1)
  }, 2500)
}
