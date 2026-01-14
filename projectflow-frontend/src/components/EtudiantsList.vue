<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-3xl font-bold text-gray-800 mb-2">Mes Étudiants</h2>
        <p class="text-gray-600">Liste des étudiants qui vous sont assignés</p>
      </div>
      <div class="flex items-center gap-3">
        <div class="relative">
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Rechercher un étudiant..."
            class="pl-10 pr-4 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none"
          />
          <Search class="absolute left-3 top-2.5 text-gray-400" :size="20" />
        </div>
      </div>
    </div>

    <!-- Stats rapides -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <div class="bg-gradient-to-r from-blue-500 to-blue-600 rounded-xl p-4 text-white">
        <p class="text-sm opacity-90">Total Étudiants</p>
        <p class="text-3xl font-bold mt-1">{{ etudiants.length }}</p>
      </div>
      <div class="bg-gradient-to-r from-green-500 to-green-600 rounded-xl p-4 text-white">
        <p class="text-sm opacity-90">Projets en cours</p>
        <p class="text-3xl font-bold mt-1">{{ etudiantsAvecProjet }}</p>
      </div>
      <div class="bg-gradient-to-r from-purple-500 to-purple-600 rounded-xl p-4 text-white">
        <p class="text-sm opacity-90">Sans projet</p>
        <p class="text-3xl font-bold mt-1">{{ etudiantsSansProjet }}</p>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="text-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-orange-500 mx-auto"></div>
      <p class="text-gray-600 mt-4">Chargement des étudiants...</p>
    </div>

    <!-- Liste des étudiants -->
    <div v-else class="bg-white rounded-2xl shadow-sm overflow-hidden">
      <!-- Table header -->
      <div class="bg-gray-50 px-6 py-4 border-b border-gray-200">
        <div class="grid grid-cols-12 gap-4 font-semibold text-gray-700 text-sm">
          <div class="col-span-4">Étudiant</div>
          <div class="col-span-3">Filière</div>
          <div class="col-span-2">CIN</div>
          <div class="col-span-2">Statut Projet</div>
          <div class="col-span-1 text-center">Actions</div>
        </div>
      </div>

      <!-- Table body -->
      <div class="divide-y divide-gray-200">
        <div 
          v-for="etudiant in filteredEtudiants" 
          :key="etudiant.id"
          class="px-6 py-4 hover:bg-gray-50 transition-colors cursor-pointer"
        >
          <div class="grid grid-cols-12 gap-4 items-center">
            <!-- Étudiant -->
            <div class="col-span-4 flex items-center gap-3">
              <div class="w-10 h-10 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full flex items-center justify-center flex-shrink-0">
                <span class="text-white font-bold text-sm">{{ getInitiales(etudiant.nom) }}</span>
              </div>
              <div>
                <p class="font-semibold text-gray-800">{{ etudiant.nom }}</p>
                <p class="text-sm text-gray-500">{{ etudiant.email }}</p>
              </div>
            </div>

            <!-- Filière -->
            <div class="col-span-3">
              <span class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800">
                {{ etudiant.filiere }}
              </span>
            </div>

            <!-- CIN -->
            <div class="col-span-2">
              <p class="text-sm text-gray-600">{{ etudiant.cin || 'N/A' }}</p>
            </div>

            <!-- Statut Projet -->
            <div class="col-span-2">
              <span 
                v-if="etudiant.hasProjet"
                class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-800"
              >
                <div class="w-2 h-2 bg-green-500 rounded-full mr-2"></div>
                En cours
              </span>
              <span 
                v-else
                class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-gray-100 text-gray-600"
              >
                Sans projet
              </span>
            </div>

            <!-- Actions -->
            <div class="col-span-1 flex justify-center gap-2">
              <button 
                @click="viewDetails(etudiant)"
                class="p-2 hover:bg-blue-100 rounded-lg transition-colors"
                title="Voir les détails"
              >
                <Eye :size="18" class="text-blue-600" />
              </button>
              <button 
                class="p-2 hover:bg-orange-100 rounded-lg transition-colors"
                title="Envoyer un message"
              >
                <Mail :size="18" class="text-orange-600" />
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty state -->
      <div v-if="filteredEtudiants.length === 0" class="text-center py-12">
        <Users class="mx-auto text-gray-400 mb-4" :size="48" />
        <h3 class="text-lg font-semibold text-gray-800 mb-2">Aucun étudiant trouvé</h3>
        <p class="text-gray-600">{{ searchQuery ? 'Essayez une autre recherche' : 'Aucun étudiant assigné' }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { Users, Search, Eye, Mail } from 'lucide-vue-next'
import api from '@/services/api'

export default {
  name: 'EtudiantsList',
  components: { Users, Search, Eye, Mail },
  setup() {
    const loading = ref(true)
    const etudiants = ref([])
    const searchQuery = ref('')

    const filteredEtudiants = computed(() => {
      if (!searchQuery.value) return etudiants.value
      
      const query = searchQuery.value.toLowerCase()
      return etudiants.value.filter(e => 
        e.nom.toLowerCase().includes(query) ||
        e.email.toLowerCase().includes(query) ||
        e.filiere.toLowerCase().includes(query) ||
        (e.cin && e.cin.toLowerCase().includes(query))
      )
    })

    const etudiantsAvecProjet = computed(() => {
      return etudiants.value.filter(e => e.hasProjet).length
    })

    const etudiantsSansProjet = computed(() => {
      return etudiants.value.filter(e => !e.hasProjet).length
    })

    const loadEtudiants = async () => {
      try {
        loading.value = true
        const response = await api.getEtudiantsAssignes()
        etudiants.value = response.data
      } catch (error) {
        console.error('Erreur chargement étudiants:', error)
      } finally {
        loading.value = false
      }
    }

    const getInitiales = (nom) => {
      const parts = nom.split(' ')
      if (parts.length >= 2) {
        return parts[0].substring(0, 1).toUpperCase() + 
               parts[1].substring(0, 1).toUpperCase()
      }
      return nom.substring(0, 2).toUpperCase()
    }

    const viewDetails = (etudiant) => {
      console.log('Voir détails de:', etudiant)
      // TODO: Naviguer vers page de détails ou ouvrir une modal
    }

    onMounted(() => {
      loadEtudiants()
    })

    return {
      loading,
      etudiants,
      searchQuery,
      filteredEtudiants,
      etudiantsAvecProjet,
      etudiantsSansProjet,
      getInitiales,
      viewDetails
    }
  }
}
</script>