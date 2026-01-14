<template>
  <div class="min-h-screen bg-gradient-to-br from-orange-400 to-orange-600 flex items-center justify-center p-4">
    <div class="bg-white rounded-2xl shadow-2xl p-8 w-full max-w-md">
      <div class="text-center mb-8">
        <div class="w-16 h-16 bg-gradient-to-br from-orange-400 to-orange-600 rounded-2xl flex items-center justify-center mx-auto mb-4">
          <BookOpen class="w-8 h-8 text-white" />
        </div>
        <h2 class="text-3xl font-bold text-gray-800">{{ mode === 'login' ? 'Connexion' : 'Inscription' }}</h2>
        <p class="text-gray-600 mt-2">Système de Gestion Scolaire</p>
      </div>

      <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-4">
        {{ error }}
      </div>

      <div class="space-y-4">
        <!-- Nom (seulement pour l'inscription) -->
        <div v-if="mode === 'register'">
          <label class="block text-sm font-medium text-gray-700 mb-2">Nom complet</label>
          <input
            v-model="nom"
            type="text"
            placeholder="Karim Alaoui"
            class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
          <input
            v-model="email"
            type="email"
            placeholder="votre.email@university.ma"
            class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Mot de passe</label>
          <input
            v-model="password"
            type="password"
            placeholder="••••••••"
            class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none"
          />
        </div>

        <!-- Role (seulement pour l'inscription) -->
        <div v-if="mode === 'register'">
          <label class="block text-sm font-medium text-gray-700 mb-2">Rôle</label>
          <select v-model="role" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none">
            <option value="Professeur">Professeur</option>
            <option value="Etudiant">Etudiant</option>
          </select>
        </div>

        <button
          @click="handleAuth"
          :disabled="loading"
          class="w-full py-3 bg-gradient-to-r from-orange-400 to-orange-500 text-white rounded-lg font-medium hover:from-orange-500 hover:to-orange-600 transition-all shadow-md hover:shadow-lg disabled:opacity-50"
        >
          {{ loading ? (mode === 'login' ? 'Connexion...' : 'Inscription...') : (mode === 'login' ? 'Se connecter' : "S'inscrire") }}
        </button>

        <p class="text-center text-sm mt-2 text-gray-500">
          <span v-if="mode === 'login'">Pas encore de compte ?</span>
          <span v-else>Déjà un compte ?</span>
          <button @click="toggleMode" class="text-orange-500 font-medium ml-1">
            {{ mode === 'login' ? "S'inscrire" : 'Se connecter' }}
          </button>
        </p>
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
  name: 'Auth',
  components: { BookOpen },
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()

    const mode = ref('login') // 'login' ou 'register'
    const nom = ref('')
    const email = ref('karim.alaoui@university.ma')
    const password = ref('password')
    const role = ref('Professeur')
    const loading = ref(false)
    const error = ref('')

    const toggleMode = () => {
      mode.value = mode.value === 'login' ? 'register' : 'login'
      error.value = ''
    }

    const handleAuth = async () => {
      if (!email.value || !password.value || (mode.value === 'register' && !nom.value)) {
        error.value = 'Veuillez remplir tous les champs'
        return
      }

      loading.value = true
      error.value = ''

      try {
        let success = false
        if (mode.value === 'login') {
          success = await authStore.login(email.value, password.value)
        } else {
          success = await authStore.register({
            nom: nom.value,
            email: email.value,
            password: password.value,
            role: role.value
          })
        }

        if (success) {
          router.push('/')
        } else {
          error.value = mode.value === 'login' ? 'Email ou mot de passe incorrect' : "Erreur lors de l'inscription"
        }
      } catch (err) {
        error.value = mode.value === 'login' ? 'Erreur de connexion. Veuillez réessayer.' : "Erreur lors de l'inscription"
        console.error(err)
      } finally {
        loading.value = false
      }
    }

    return {
      mode,
      nom,
      email,
      password,
      role,
      loading,
      error,
      handleAuth,
      toggleMode
    }
  }
}
</script>
