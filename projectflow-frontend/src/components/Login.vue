<template>
  <div class="min-h-screen bg-gradient-to-br from-orange-400 to-orange-600 flex items-center justify-center p-4">
    <div class="bg-white rounded-2xl shadow-2xl p-8 w-full max-w-md">
      <div class="text-center mb-8">
        <div class="w-16 h-16 bg-gradient-to-br from-orange-400 to-orange-600 rounded-2xl flex items-center justify-center mx-auto mb-4">
          <BookOpen class="w-8 h-8 text-white" />
        </div>
        <h2 class="text-3xl font-bold text-gray-800">Connexion</h2>
        <p class="text-gray-600 mt-2">Système de Gestion Scolaire</p>
      </div>

      <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-4">
        {{ error }}
      </div>

      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
          <input
            v-model="email"
            type="email"
            placeholder="votre.email@university.ma"
            class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none"
            @keyup.enter="handleLogin"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Mot de passe</label>
          <input
            v-model="password"
            type="password"
            placeholder="••••••••"
            class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none"
            @keyup.enter="handleLogin"
          />
        </div>

        <button
          @click="handleLogin"
          :disabled="loading"
          class="w-full py-3 bg-gradient-to-r from-orange-400 to-orange-500 text-white rounded-lg font-medium hover:from-orange-500 hover:to-orange-600 transition-all shadow-md hover:shadow-lg disabled:opacity-50"
        >
          {{ loading ? 'Connexion...' : 'Se connecter' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store'
import { BookOpen } from 'lucide-vue-next'

export default {
  name: 'Login',
  components: { BookOpen },
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    
    const email = ref('karim.alaoui@university.ma')
    const password = ref('password')
    const loading = ref(false)
    const error = ref('')

    const handleLogin = async () => {
      if (!email.value || !password.value) {
        error.value = 'Veuillez remplir tous les champs'
        return
      }

      loading.value = true
      error.value = ''
      
      try {
        const success = await authStore.login(email.value, password.value)
        if (success) {
          router.push('/')
        } else {
          error.value = 'Email ou mot de passe incorrect'
        }
      } catch (err) {
        error.value = 'Erreur de connexion. Veuillez réessayer.'
      } finally {
        loading.value = false
      }
    }

    return {
      email,
      password,
      loading,
      error,
      handleLogin
    }
  }
}
</script>