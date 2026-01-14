import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../views/DashboardView.vue'
import Profile from '../views/Profile.vue'
import ProjectDetails from '../views/ProjectDetails.vue'



const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: DashboardView
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile
  },
  {
  path: '/project/:id',
  name: 'ProjectDetails',
  component: ProjectDetails
}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
