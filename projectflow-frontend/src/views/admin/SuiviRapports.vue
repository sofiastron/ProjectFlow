<template>
  <div class="rapports-page fade-in">
    <div class="page-header">
      <h1>Suivi des Rapports PFE</h1>
      <p class="subtitle">Gérer et valider les rapports soumis par les étudiants</p>
    </div>

    <!-- Cartes statistiques -->
    <div class="grid grid-4 mb-4">
      <div class="stat-card blue">
        <div class="stat-icon">
          <FileText :size="24" />
        </div>
        <div class="stat-value">{{ stats.total }}</div>
        <div class="stat-label">Total Rapports</div>
      </div>

      <div class="stat-card green">
        <div class="stat-icon">
          <CheckCircle :size="24" />
        </div>
        <div class="stat-value">{{ stats.valides }}</div>
        <div class="stat-label">Validés</div>
      </div>

      <div class="stat-card yellow">
        <div class="stat-icon">
          <Clock :size="24" />
        </div>
        <div class="stat-value">{{ stats.enCours }}</div>
        <div class="stat-label">En Cours</div>
      </div>

      <div class="stat-card pink">
        <div class="stat-icon">
          <AlertCircle :size="24" />
        </div>
        <div class="stat-value">{{ stats.aCorriger }}</div>
        <div class="stat-label">À Corriger</div>
      </div>
    </div>

    <!-- Tableau des rapports -->
    <div class="card">
      <div class="table-header">
        <h2>Liste des Rapports</h2>
        <select v-model="statusFilter">
          <option value="">Tous les statuts</option>
          <option value="valide">Validé</option>
          <option value="en-cours">En cours</option>
          <option value="a-corriger">À corriger</option>
        </select>
      </div>

      <table>
        <thead>
          <tr>
            <th>Étudiant</th>
            <th>Titre du Projet</th>
            <th>Encadrant</th>
            <th>Date Dépôt</th>
            <th>Statut</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="rapport in filteredRapports" :key="rapport.id">
            <td>
              <strong>{{ rapport.etudiant }}</strong>
            </td>
            <td>{{ rapport.titre }}</td>
            <td>{{ rapport.encadrant }}</td>
            <td>{{ rapport.dateDepot }}</td>
            <td>
              <span class="badge" :class="rapport.statutClass">
                <component :is="getStatusIcon(rapport.statut)" :size="14" />
                {{ rapport.statut }}
              </span>
            </td>
            <td>
              <div class="actions">
                <button class="btn-icon" title="Voir">
                  <Eye :size="18" />
                </button>
                <button class="btn-icon" title="Télécharger">
                  <Download :size="18" />
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
  FileText,
  CheckCircle,
  Clock,
  AlertCircle,
  Eye,
  Download
} from 'lucide-vue-next'
import api from '../../services/api'

const stats = ref({
  total: 0,
  valides: 0,
  enCours: 0,
  aCorriger: 0
})

const statusFilter = ref('')
const rapports = ref([])
const loading = ref(true)
const error = ref(null)

// const loadRapports = async () => {
//   try {
//     loading.value = true
//     error.value = null
    
//     // Charger les statistiques
//     const statsRes = await api.get('/rapports/statistiques')
//     if (statsRes.data.success) {
//       stats.value = statsRes.data.data
//     }

//     // Charger les rapports
//     const rapportsRes = await api.get('/rapports')
//     if (rapportsRes.data.success) {
//       rapports.value = rapportsRes.data.data.map(rapport => ({
//         id: rapport.id,
//         etudiant: rapport.etudiantNom,
//         titre: rapport.titre,
//         encadrant: rapport.encadrantNom,
//         dateDepot: formatDate(rapport.dateDepot),
//         statut: rapport.statut,
//         statutClass: rapport.statutClass
//       }))
//     }
//   } catch (err) {
//     console.error('Erreur chargement rapports:', err)
//     error.value = 'Impossible de charger les rapports'
//   } finally {
//     loading.value = false
//   }
// }






// const loadRapports = async () => {
//   try {
//     loading.value = true
//     error.value = null

//     // ✅ STATISTIQUES
//     const statsRes = await api.get('/rapports/statistiques')
//     // statsRes = { success, data }

//     if (statsRes.success) {
//       stats.value = {
//         total: statsRes.data.total ?? 0,
//         valides: statsRes.data.valides ?? 0,
//         enCours: statsRes.data.enCours ?? 0,
//         aCorriger: statsRes.data.aCorriger ?? 0
//       }
//     }

//     // ✅ RAPPORTS
//     const rapportsRes = await api.get('/rapports')
//     // rapportsRes = { success, data }

//     if (rapportsRes.success && Array.isArray(rapportsRes.data)) {
//       rapports.value = rapportsRes.data.map(r => ({
//         id: r.id,

//         // ⚠️ adapter AUX VRAIS champs backend
//         etudiant: r.etudiant?.nom ?? r.etudiantNom ?? '—',
//         titre: r.titre ?? '—',
//         encadrant: r.encadrant?.nom ?? r.encadrantNom ?? '—',

//         dateDepot: formatDate(r.dateDepot),

//         // statut calculé / sécurisé
//         statut: r.statut ?? 'En cours',
//         statutClass: r.statutClass ?? getStatutClass(r.statut)
//       }))
//     }

//     console.log('✅ Rapports affichables:', rapports.value)

//   } catch (err) {
//     console.error('Erreur chargement rapports:', err)
//     error.value = 'Impossible de charger les rapports'
//   } finally {
//     loading.value = false
//   }
// }




const loadRapports = async () => {
  try {
    loading.value = true
    error.value = null

    // ✅ STATISTIQUES
    const statsRes = await api.get('/rapports/statistiques')
    // statsRes = { success, data }

    if (statsRes.success) {
      stats.value = {
        total: statsRes.data.total ?? 0,
        valides: statsRes.data.valides ?? 0,
        enCours: statsRes.data.enCours ?? 0,
        aCorriger: statsRes.data.aCorriger ?? 0
      }
    }

    // ✅ RAPPORTS - CORRECTION ICI
    const rapportsRes = await api.get('/rapports')
    
    // ⚠️ rapportsRes.data est DIRECTEMENT le tableau, pas rapportsRes.data.data
    if (Array.isArray(rapportsRes.data)) {
      rapports.value = rapportsRes.data.map(r => ({
        id: r.id,
        
        // ✅ Utiliser les VRAIS champs de l'API
        etudiant: r.nomEtudiant ?? '—',
        titre: r.titreSujet ?? r.titre ?? '—',
        encadrant: r.nomProfesseur ?? '—',
        
        dateDepot: formatDate(r.dateDepot),
        
        statut: r.statut ?? 'En cours',
        statutClass: getStatutClass(r.statut)
      }))
      
      console.log('✅ Rapports chargés:', rapports.value)
    } else {
      console.error('❌ Format de réponse invalide:', rapportsRes.data)
    }

  } catch (err) {
    console.error('❌ Erreur chargement rapports:', err)
    error.value = 'Impossible de charger les rapports'
  } finally {
    loading.value = false
  }
}

const getStatutClass = (statut) => {
  if (!statut) return 'en-cours'
  
  const statutLower = statut.toLowerCase()
  
  if (statutLower.includes('validé') || statutLower === 'valide') {
    return 'valide'
  }
  if (statutLower.includes('corriger') || statutLower.includes('refusé')) {
    return 'a-corriger'
  }
  return 'en-cours'
}


const formatDate = (dateString) => {
  if (!dateString) return 'N/A'
  const date = new Date(dateString)
  return date.toLocaleDateString('fr-FR')
}

const filteredRapports = computed(() => {
  if (!statusFilter.value) return rapports.value
  return rapports.value.filter(r => r.statutClass === statusFilter.value)
})

const getStatusIcon = (statut) => {
  if (statut === 'Validé') return CheckCircle
  if (statut === 'En cours') return Clock
  return AlertCircle
}

onMounted(() => {
  loadRapports()
})
</script>

<style scoped>
.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.actions {
  display: flex;
  gap: 8px;
}

td strong {
  font-weight: 600;
  color: var(--text-dark);
}
</style>