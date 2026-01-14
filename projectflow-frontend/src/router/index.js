import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/admin/Dashboard.vue')
  },
  {
    path: '/affectation',
    name: 'AffectationPFE',
    component: () => import('../views/admin/AffectationPFE.vue')
  },
  {
    path: '/rapports',
    name: 'SuiviRapports',
    component: () => import('../views/admin/SuiviRapports.vue')
  },
  {
    path: '/soutenances',
    name: 'Soutenances',
    component: () => import('../views/admin/Soutenances.vue')
  },
  {
    path: '/encadrants',
    name: 'Encadrants',
    component: () => import('../views/admin/Encadrants.vue')
  },
  {
    path: '/etudiants',
    name: 'Etudiants',
    component: () => import('../views/admin/Etudiants.vue')
  },
  {
    path: '/parametres',
    name: 'Parametres',
    component: () => import('../views/admin/Parametres.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router