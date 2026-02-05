import { reactive } from 'vue'
import http from '../api/http'

export const authStore = reactive({
  me: null,
  loading: false,
  async fetchMe() {
    try {
      const { data } = await http.get('/auth/me')
      this.me = data
    } catch {
      this.me = null
    }
  },
  async logout() {
    await http.post('/auth/logout')
    this.me = null
  }
})
