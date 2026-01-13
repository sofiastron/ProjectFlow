<template>
  <div class="space-y-6">
    <div>
      <h2 class="text-3xl font-bold text-gray-800 mb-2">Vue d'Ensemble du Tableau de Bord</h2>
      <p class="text-gray-600">Bienvenue {{ professeurNom }} ! Voici ce qui se passe avec vos étudiants.</p>
    </div>

    <!-- Stats Cards -->
    <div v-if="loading" class="text-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-orange-500 mx-auto"></div>
      <p class="text-gray-600 mt-4">Chargement...</p>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <div v-for="(stat, index) in stats" :key="index" 
           class="bg-white rounded-2xl p-6 shadow-sm hover:shadow-md transition-shadow cursor-pointer">
        <div :class="stat.color + ' w-16 h-16 rounded-2xl flex items-center justify-center mb-4'">
          <component :is="stat.icon" class="w-8 h-8" />
        </div>
        <h3 class="text-gray-600 text-sm font-medium mb-1">{{ stat.label }}</h3>
        <p class="text-3xl font-bold text-gray-800 mb-1">{{ stat.value }}</p>
        <p class="text-sm text-gray-500">{{ stat.subtext }}</p>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Répartition Statuts -->
      <div class="bg-white rounded-2xl p-6 shadow-sm">
        <div class="flex items-center gap-2 mb-6">
          <div class="bg-orange-100 p-2 rounded-lg">
            <FileText class="text-orange-600 w-5 h-5" />
          </div>
          <h3 class="text-lg font-bold text-gray-800">Répartition des Statuts de Projets</h3>
        </div>
        <div class="space-y-4">
          <div v-for="statut in statutsProjets" :key="statut.type">
            <div class="flex justify-between mb-2">
              <span class="text-sm text-gray-600">{{ statut.type }}</span>
              <span class="text-sm font-semibold text-gray-800">
                {{ statut.count }} projets ({{ Math.round(statut.pourcentage) }}%)
              </span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-3">
              <div :class="statut.colorClass + ' h-3 rounded-full transition-all'" 
                   :style="{ width: statut.pourcentage + '%' }">
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Activité Hebdomadaire -->
      <div class="bg-white rounded-2xl p-6 shadow-sm">
        <div class="flex items-center gap-2 mb-6">
          <div class="bg-yellow-100 p-2 rounded-lg">
            <Calendar class="text-yellow-600 w-5 h-5" />
          </div>
          <h3 class="text-lg font-bold text-gray-800">Activité Hebdomadaire</h3>
        </div>
        <div class="flex items-end justify-between gap-3 h-48">
          <div v-for="item in activiteHebdo" :key="item.jour" 
               class="flex-1 flex flex-col items-center gap-2">
            <div class="w-full bg-gradient-to-t from-orange-400 to-orange-300 rounded-t-lg hover:from-orange-500 hover:to-orange-400 transition-colors cursor-pointer" 
                 :style="{ height: (item.valeur / maxActivite * 100) + '%' }">
            </div>
            <span class="text-xs text-gray-600 font-medium">{{ item.jour }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Liste des étudiants récents -->
    <div class="bg-white rounded-2xl p-6 shadow-sm">
      <h3 class="text-lg font-bold text-gray-800 mb-4">Étudiants Récemment Assignés</h3>
      <div v-if="etudiantsRecents.length === 0" class="text-center py-8 text-gray-500">
        Aucun étudiant assigné
      </div>
      <div v-else class="space-y-3">
        <div v-for="etudiant in etudiantsRecents" :key="etudiant.id" 
             class="flex items-center justify-between p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors cursor-pointer">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full flex items-center justify-center">
              <span class="text-white font-bold text-sm">{{ etudiant.initiales }}</span>
            </div>
            <div>
              <p class="font-semibold text-gray-800">{{ etudiant.nom }}</p>
              <p class="text-sm text-gray-500">{{ etudiant.filiere }}</p>
            </div>
          </div>
          <span class="text-sm text-gray-500">{{ etudiant.dateAssignation }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { Users, FileText, ClipboardCheck, Calendar } from 'lucide-vue-next'
import api from '@/services/api'

export default {
  name: 'Dashboard',
  components: { Users, FileText, ClipboardCheck, Calendar },
  setup() {
    const loading = ref(true)
    const professeurNom = ref('')
    const stats = ref([])
    const statutsProjets = ref([])
    const activiteHebdo = ref([])
    const etudiantsRecents = ref([])
    
    const maxActivite = computed(() => {
      if (activiteHebdo.value.length === 0) return 1
      return Math.max(...activiteHebdo.value.map(a => a.valeur))
    })

    const loadDashboardData = async () => {
      try {
        loading.value = true
        const response = await api.getDashboardStats()
        const data = response.data
        
        professeurNom.value = data.professeurNom
        
        stats.value = [
          {
            icon: Users,
            label: 'Total Étudiants',
            value: data.totalEtudiants,
            subtext: `+${data.nouveauxEtudiants} ce mois`,
            color: 'bg-blue-100 text-blue-600'
          },
          {
            icon: FileText,
            label: 'Projets Actifs',
            value: data.projetsActifs,
            subtext: `${data.projetsEnCours} en cours`,
            color: 'bg-purple-100 text-purple-600'
          },
          {
            icon: ClipboardCheck,
            label: 'Terminés',
            value: data.projetsTermines,
            subtext: `+${data.nouveauxTermines} cette semaine`,
            color: 'bg-green-100 text-green-600'
          },
          {
            icon: Calendar,
            label: 'En Révision',
            value: data.projetsRevision,
            subtext: 'Nécessite attention',
            color: 'bg-yellow-100 text-yellow-600'
          }
        ]
        
        statutsProjets.value = data.statutsProjets || []
        activiteHebdo.value = data.activiteHebdo || []
        etudiantsRecents.value = data.etudiantsRecents || []
      } catch (error) {
        console.error('Erreur chargement dashboard:', error)
      } finally {
        loading.value = false
      }
    }

    onMounted(() => {
      loadDashboardData()
    })

    return {
      loading,
      professeurNom,
      stats,
      statutsProjets,
      activiteHebdo,
      etudiantsRecents,
      maxActivite
    }
  }
}
</script>