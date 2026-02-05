import { createRouter, createWebHistory } from 'vue-router'
import { authStore } from '../stores/auth'
import LoginPage from '../pages/LoginPage.vue'
import DashboardPage from '../pages/DashboardPage.vue'
import VaultPage from '../pages/VaultPage.vue'
import TodoPage from '../pages/TodoPage.vue'
import AdminUsersPage from '../pages/AdminUsersPage.vue'
import ResetPasswordPage from '../pages/ResetPasswordPage.vue'
import ProfilePage from '../pages/ProfilePage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: LoginPage },
    { path: '/reset-password', component: ResetPasswordPage },
    { path: '/dashboard', component: DashboardPage, meta: { auth: true } },
    { path: '/profile', component: ProfilePage, meta: { auth: true } },
    { path: '/tools/vault', component: VaultPage, meta: { auth: true } },
    { path: '/tools/todo', component: TodoPage, meta: { auth: true } },
    { path: '/admin/users', component: AdminUsersPage, meta: { auth: true, admin: true } },
    { path: '/:pathMatch(.*)*', redirect: '/dashboard' },
  ]
})

router.beforeEach(async (to) => {
  if (!authStore.me) await authStore.fetchMe()
  if (to.meta.auth && !authStore.me) return '/login'
  if (to.meta.admin && authStore.me?.role !== 'ADMIN') return '/dashboard'
  if (to.path === '/login' && authStore.me) return '/dashboard'
})

export default router
