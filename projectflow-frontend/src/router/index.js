import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store'

import Login from '@/components/Login.vue'
import Layout from '@/components/Layout.vue'
import Dashboard from '@/components/Dashboard.vue'
import Profile from '@/components/Profile.vue'

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
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*', // route pour gérer 404
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Guard global
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore() // utilise Pinia pour vérifier l'auth
  const isAuthenticated = authStore.isAuthenticated

  if (to.meta.requiresAuth && !isAuthenticated) {
    // si la page nécessite auth et que l'utilisateur n'est pas connecté
    next('/login')
  } else if (to.path === '/login' && isAuthenticated) {
    // si l'utilisateur connecté veut aller sur login
    next('/')
  } else {
    next()
  }
})

export default router
