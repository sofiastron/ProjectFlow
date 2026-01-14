<template>
  <div class="max-w-4xl">
    <div>
      <h2 class="text-3xl font-bold text-gray-800 mb-2">Mon Profil</h2>
      <p class="text-gray-600 mb-8">Gérez vos informations personnelles</p>
    </div>

    <div v-if="loading" class="text-center py-12">
      <!-- ✅ Spinner orange doux -->
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-amber-500 mx-auto"></div>
      <p class="text-gray-600 mt-4">Chargement...</p>
    </div>

    <div v-else class="bg-white rounded-2xl shadow-sm overflow-hidden">
      <!-- ✅ Bannière orange douce -->
      <div class="bg-gradient-to-r from-amber-400 to-orange-500 h-32"></div>
      
      <div class="px-8 pb-8">
        <div class="flex items-end -mt-16 mb-6">
          <div class="relative">
            <div class="w-32 h-32 rounded-full bg-white p-2 shadow-lg">
              <img v-if="profile.photo" 
                   :src="profile.photo" 
                   alt="Profile" 
                   class="w-full h-full rounded-full object-cover" />
              <div v-else 
                   class="w-full h-full rounded-full bg-gradient-to-br from-amber-400 to-orange-500 flex items-center justify-center">
                <User class="text-white w-12 h-12" />
              </div>
            </div>
            <!-- ✅ Bouton orange doux -->
            <label class="absolute bottom-0 right-0 bg-amber-500 text-white p-2 rounded-full cursor-pointer hover:bg-amber-600 transition-colors shadow-lg">
              <input type="file" class="hidden" accept="image/*" @change="handlePhotoChange" />
              <Camera class="w-5 h-5" />
            </label>
          </div>
          <div class="ml-6 mb-4">
            <h3 class="text-2xl font-bold text-gray-800">{{ profile.nom }}</h3>
            <p class="text-gray-600">{{ profile.role }}</p>
          </div>
        </div>

        <div v-if="successMessage" class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded-lg mb-4">
          {{ successMessage }}
        </div>

        <div v-if="errorMessage" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-4">
          {{ errorMessage }}
        </div>

        <div class="space-y-6">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Nom Complet</label>
              <input
                type="text"
                v-model="profile.nom"
                class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-amber-500 focus:border-transparent outline-none"
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
              <input
                type="email"
                v-model="profile.email"
                class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-amber-500 focus:border-transparent outline-none"
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Spécialité</label>
              <input
                type="text"
                v-model="profile.specialite"
                class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-amber-500 focus:border-transparent outline-none"
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Date d'inscription</label>
              <input
                type="text"
                :value="formatDate(profile.createdAt)"
                disabled
                class="w-full px-4 py-3 rounded-lg border border-gray-300 bg-gray-50 text-gray-500"
              />
            </div>
          </div>

          <div class="flex gap-4 pt-4">
            <!-- ✅ Boutons orange doux -->
            <button
              @click="handleSave"
              :disabled="saving"
              class="px-6 py-3 bg-gradient-to-r from-amber-400 to-orange-500 text-white rounded-lg font-medium hover:from-amber-500 hover:to-orange-600 transition-all shadow-md hover:shadow-lg disabled:opacity-50"
            >
              {{ saving ? 'Enregistrement...' : 'Enregistrer les modifications' }}
            </button>
            <button
              @click="handleCancel"
              class="px-6 py-3 bg-gray-100 text-gray-700 rounded-lg font-medium hover:bg-gray-200 transition-all"
            >
              Annuler
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { User, Camera } from 'lucide-vue-next'
import api from '@/services/api'

export default {
  name: 'Profile',
  components: { User, Camera },
  setup() {
    const loading = ref(true)
    const saving = ref(false)
    const successMessage = ref('')
    const errorMessage = ref('')
    
    const profile = ref({
      nom: '',
      email: '',
      specialite: '',
      role: 'Professeur',
      createdAt: null,
      photo: null
    })
    
    const originalProfile = ref({})

    const loadProfile = async () => {
      try {
        loading.value = true
        const response = await api.getProfesseurProfile()
        profile.value = response.data
        originalProfile.value = { ...response.data }
      } catch (error) {
        console.error('Erreur chargement profil:', error)
        errorMessage.value = 'Erreur lors du chargement du profil'
      } finally {
        loading.value = false
      }
    }

    const handlePhotoChange = async (event) => {
      const file = event.target.files[0]
      if (file) {
        if (file.size > 10 * 1024 * 1024) {
          errorMessage.value = 'La taille du fichier ne doit pas dépasser 10MB'
          return
        }

        const formData = new FormData()
        formData.append('photo', file)
        
        try {
          const response = await api.uploadProfilePhoto(formData)
          profile.value.photo = response.data.photoUrl
          successMessage.value = 'Photo mise à jour avec succès !'
          setTimeout(() => successMessage.value = '', 3000)
        } catch (error) {
          console.error('Erreur upload photo:', error)
          errorMessage.value = 'Erreur lors du téléchargement de la photo'
        }
      }
    }

    const handleSave = async () => {
      saving.value = true
      successMessage.value = ''
      errorMessage.value = ''
      
      try {
        await api.updateProfesseurProfile({
          nom: profile.value.nom,
          email: profile.value.email,
          specialite: profile.value.specialite
        })
        successMessage.value = 'Profil mis à jour avec succès !'
        originalProfile.value = { ...profile.value }
        setTimeout(() => successMessage.value = '', 3000)
      } catch (error) {
        console.error('Erreur mise à jour profil:', error)
        errorMessage.value = 'Erreur lors de la mise à jour du profil'
      } finally {
        saving.value = false
      }
    }

    const handleCancel = () => {
      profile.value = { ...originalProfile.value }
      successMessage.value = ''
      errorMessage.value = ''
    }

    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleDateString('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
    }

    onMounted(() => {
      loadProfile()
    })

    return {
      loading,
      saving,
      profile,
      successMessage,
      errorMessage,
      handlePhotoChange,
      handleSave,
      handleCancel,
      formatDate
    }
  }
}
</script>