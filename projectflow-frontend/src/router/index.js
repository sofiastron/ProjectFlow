import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store'

import Login from '@/components/Login.vue'
import Layout from '@/components/Layout.vue'
import Dashboard from '@/components/Dashboard.vue'
import Profile from '@/components/Profile.vue'
import EtudiantsList from '@/components/EtudiantsList.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: Dashboard
      },
      {
        path: 'profile',
        name: 'Profile',
        component: Profile
      },
      {
        path: 'etudiants',
        name: 'Etudiants',
        component: EtudiantsList
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Guard global
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const isAuthenticated = authStore.isAuthenticated

  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
  } else if (to.path === '/login' && isAuthenticated) {
    next('/')
  } else {
    next()
  }
})

export default router
