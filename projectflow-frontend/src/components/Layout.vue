<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white border-b border-gray-200 sticky top-0 z-50">
      <div class="px-6 py-4 flex items-center justify-between">
        <div class="flex items-center gap-4">
          <div class="w-12 h-12 bg-gradient-to-br from-orange-400 to-orange-600 rounded-xl flex items-center justify-center">
            <BookOpen class="text-white" :size="24" />
          </div>
          <div>
            <h1 class="text-xl font-bold text-gray-800">Tableau de Bord Encadrant</h1>
            <p class="text-sm text-gray-500">Système de Gestion de Projets Scolaires</p>
          </div>
        </div>
        
        <div class="flex items-center gap-4">
          <button class="relative p-2 hover:bg-gray-100 rounded-lg transition-colors">
            <Bell :size="24" class="text-gray-600" />
            <span class="absolute top-0 right-0 w-5 h-5 bg-orange-500 text-white text-xs rounded-full flex items-center justify-center font-bold">
              2
            </span>
          </button>
          
          <button class="p-2 hover:bg-gray-100 rounded-lg transition-colors">
            <MessageSquare :size="24" class="text-gray-600" />
          </button>
          
          <div class="relative">
            <button 
              @click="showProfileMenu = !showProfileMenu"
              class="flex items-center gap-2 p-2 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <div class="w-10 h-10 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full flex items-center justify-center">
                <User class="text-white" :size="20" />
              </div>
              <ChevronDown :size="16" class="text-gray-600" />
            </button>
            
            <div v-if="showProfileMenu" 
                 class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 py-2">
              <router-link 
                to="/profile"
                class="block px-4 py-2 text-gray-700 hover:bg-gray-100 transition-colors"
                @click="showProfileMenu = false"
              >
                <div class="flex items-center gap-2">
                  <User :size="16" />
                  <span>Mon Profil</span>
                </div>
              </router-link>
              <button 
                @click="handleLogout"
                class="w-full text-left px-4 py-2 text-red-600 hover:bg-red-50 transition-colors"
              >
                <div class="flex items-center gap-2">
                  <LogOut :size="16" />
                  <span>Déconnexion</span>
                </div>
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>

    <div class="flex">
      <!-- Sidebar -->
      <aside class="w-72 bg-white border-r border-gray-200 min-h-screen sticky top-16">
        <nav class="p-4 space-y-2">
          <router-link
            to="/"
            v-slot="{ isActive }"
          >
            <button
              :class="[
                'w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all',
                isActive 
                  ? 'bg-gradient-to-r from-orange-400 to-orange-500 text-white shadow-md' 
                  : 'text-gray-600 hover:bg-gray-100'
              ]"
            >
              <LayoutDashboard :size="20" />
              <span class="font-medium">Tableau de Bord</span>
            </button>
          </router-link>

          <router-link
            to="/profile"
            v-slot="{ isActive }"
          >
            <button
              :class="[
                'w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all',
                isActive 
                  ? 'bg-gradient-to-r from-orange-400 to-orange-500 text-white shadow-md' 
                  : 'text-gray-600 hover:bg-gray-100'
              ]"
            >
              <User :size="20" />
              <span class="font-medium">Mon Profil</span>
            </button>
          </router-link>
          <!-- ✅ NOUVEAU: Liste des Étudiants -->
<router-link to="/etudiants" v-slot="{ isActive }">
  <button
    :class="[
      'w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all',
      isActive 
        ? 'bg-gradient-to-r from-orange-400 to-orange-500 text-white shadow-md' 
        : 'text-gray-600 hover:bg-gray-100'
    ]"
  >
    <Users :size="20" />
    <span class="font-medium">Mes Étudiants</span>
  </button>
</router-link>
        </nav>
      </aside>

      <!-- Main Content -->
      <main class="flex-1 p-8">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store'
import { LayoutDashboard,Users,User, Bell, MessageSquare, ChevronDown, LogOut, BookOpen } from 'lucide-vue-next'

export default {
  name: 'Layout',
  components: { LayoutDashboard, User, Bell, MessageSquare, ChevronDown, LogOut, BookOpen },
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    const showProfileMenu = ref(false)
    
    const handleLogout = () => {
      authStore.logout()
      router.push('/login')
    }
    
    return {
      showProfileMenu,
      handleLogout
    }
  }
}
</script>