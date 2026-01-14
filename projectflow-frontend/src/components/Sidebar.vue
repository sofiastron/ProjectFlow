<template>
  <aside class="sidebar">
    <div class="sidebar-header">
      <h2 class="sidebar-title">Espace Admin</h2>
      <p class="sidebar-subtitle">Gestion des projets</p>
    </div>

    <nav class="sidebar-nav">
      <router-link
        v-for="item in menuItems"
        :key="item.path"
        :to="item.path"
        class="nav-item"
        :class="{ active: isActive(item.path) }"
      >
        <component :is="item.icon" :size="20" />
        <span>{{ item.label }}</span>
      </router-link>
    </nav>

    <div class="sidebar-footer">
      <div class="user-profile">
        <div class="avatar yellow">A</div>
        <div class="user-info">
          <div class="user-name">Admin</div>
          <div class="user-role">Administrateur</div>
        </div>
      </div>
    </div>

    <button class="help-button">
      <HelpCircle :size="20" />
    </button>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { 
  LayoutDashboard, 
  Users, 
  FileText, 
  Calendar, 
  UserCheck, 
  GraduationCap,
  Settings,
  HelpCircle
} from 'lucide-vue-next'

const route = useRoute()

const menuItems = [
  { path: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { path: '/affectation', label: 'Affectation PFE', icon: Users },
  { path: '/rapports', label: 'Suivi des rapports', icon: FileText },
  { path: '/soutenances', label: 'Soutenances', icon: Calendar },
  { path: '/encadrants', label: 'Encadrants', icon: UserCheck },
  { path: '/etudiants', label: 'Étudiants', icon: GraduationCap },
  { path: '/parametres', label: 'Paramètres', icon: Settings }
]

const isActive = (path) => {
  return route.path === path
}
</script>

<style scoped>
.sidebar {
  width: 210px;
  background: var(--bg-beige);
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #E8E3D5;
}

.sidebar-header {
  padding: 24px 20px;
  border-bottom: 1px solid #E8E3D5;
}

.sidebar-title {
  font-size: 20px;
  font-weight: 700;
  color: var(--text-dark);
  margin-bottom: 2px;
}

.sidebar-subtitle {
  font-size: 12px;
  color: var(--text-gray);
  font-weight: 500;
}

.sidebar-nav {
  flex: 1;
  padding: 16px 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  overflow-y: auto;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: var(--radius-md);
  color: var(--text-gray);
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  cursor: pointer;
}

.nav-item:hover {
  background: rgba(255, 210, 117, 0.1);
  color: var(--text-dark);
}

.nav-item.active {
  background: var(--yellow-accent);
  color: var(--text-dark);
  font-weight: 600;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid #E8E3D5;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
  line-height: 1.2;
}

.user-role {
  font-size: 12px;
  color: var(--text-gray);
}

.help-button {
  position: fixed;
  bottom: 24px;
  right: 24px;
  width: 48px;
  height: 48px;
  border-radius: var(--radius-full);
  background: var(--text-dark);
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: var(--shadow-lg);
  transition: all 0.2s ease;
  z-index: 1000;
}

.help-button:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}
</style>