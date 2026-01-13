import { createRouter, createWebHistory } from 'vue-router'
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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// router.beforeEach((to, from, next) => {
//   const token = localStorage.getItem('token')
  
//   if (to.meta.requiresAuth && !token) {
//     next('/login')
//   } else if (to.path === '/login' && token) {
//     next('/')
//   } else {
//     next()
//   }
// })

export default router