import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'

import AdminDashboard from '../views/admin/AdminDashboard.vue'
import EncadrantDashboard from '../views/encadrant/EncadrantDashboard.vue'
import EtudiantDashboard from '../views/etudiant/EtudiantDashboard.vue'

const routes = [
  { path: '/', component: Login },

  { path: '/admin', component: AdminDashboard, meta: { role: 'ADMIN' } },
  { path: '/encadrant', component: EncadrantDashboard, meta: { role: 'ENCADRANT' } },
  { path: '/etudiant', component: EtudiantDashboard, meta: { role: 'ETUDIANT' } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
