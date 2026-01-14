<template>
  <div class="affectation-page fade-in">
    <div class="page-header">
      <h1>Affectation des Étudiants</h1>
      <p class="subtitle">Gérer l'affectation des étudiants aux encadrants</p>
    </div>

    <!-- Onglets -->
    <div class="tabs mb-3">
      <button
        class="tab"
        :class="{ active: activeTab === 'manuelle' }"
        @click="activeTab = 'manuelle'"
      >
        Affectation Manuelle
      </button>
      <button
        class="tab"
        :class="{ active: activeTab === 'automatique' }"
        @click="activeTab = 'automatique'"
      >
        Affectation Automatique
      </button>
    </div>

    <div class="grid grid-2">
      <!-- Liste des étudiants -->
      <div class="card" style="background: linear-gradient(135deg, #CFE8FF 0%, #E8F4FF 100%);">
        <div class="card-header">
          <h2>Liste des Étudiants</h2>
          <select v-model="selectedFiliere" class="filter-select">
            <option value="">Toutes les filières</option>
            <option value="Informatique">Informatique</option>
            <option value="Réseaux">Réseaux</option>
            <option value="Génie Logiciel">Génie Logiciel</option>
          </select>
        </div>

        <div class="etudiants-list">
          <div
            v-for="etudiant in filteredEtudiants"
            :key="etudiant.id"
            class="etudiant-card"
            :class="{ selected: selectedEtudiant?.id === etudiant.id }"
            @click="selectEtudiant(etudiant)"
          >
            <div class="etudiant-info">
              <div class="etudiant-name">{{ etudiant.nom }}</div>
              <div class="etudiant-filiere">{{ etudiant.filiere }}</div>
              <div v-if="etudiant.encadrant" class="etudiant-encadrant">
                Encadrant: {{ etudiant.encadrant }}
              </div>
            </div>
            <span
              class="badge"
              :class="etudiant.affecte ? 'affecte' : 'non-affecte'"
            >
              {{ etudiant.affecte ? 'Affecté' : 'Non affecté' }}
            </span>
          </div>
        </div>
      </div>

      <!-- Charge des encadrants -->
      <div class="card" style="background: linear-gradient(135deg, #D7F6E4 0%, #E8FAF0 100%);">
        <h2>Charge des Encadrants</h2>

        <div class="encadrants-list">
          <div
            v-for="encadrant in encadrants"
            :key="encadrant.id"
            class="encadrant-card"
            :class="{ selected: selectedEncadrant?.id === encadrant.id }"
            @click="selectEncadrant(encadrant)"
          >
            <div class="encadrant-header">
              <div>
                <div class="encadrant-name">{{ encadrant.nom }}</div>
                <div class="encadrant-count">{{ encadrant.current }}/{{ encadrant.max }} étudiants</div>
              </div>
              <span class="badge" :class="encadrant.statusClass">
                {{ encadrant.status }}
              </span>
            </div>
            <ProgressBar
              :current="encadrant.current"
              :max="encadrant.max"
            />
          </div>
        </div>

        <button
          v-if="activeTab === 'manuelle' && selectedEtudiant && selectedEncadrant"
          class="btn btn-primary mt-3"
          style="width: 100%;"
          @click="affecterManuellement"
        >
          <UserPlus :size="18" />
          Affecter {{ selectedEtudiant.nom }} à {{ selectedEncadrant.nom }}
        </button>

        <button
          v-if="activeTab === 'automatique'"
          class="btn btn-primary mt-3"
          style="width: 100%;"
          @click="affecterAutomatiquement"
        >
          <Zap :size="18" />
          Lancer l'affectation automatique
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { UserPlus, Zap } from 'lucide-vue-next'
import ProgressBar from '../../components/ProgressBar.vue'
import affectationService from '../../services/affectationService'
import api from '../../services/api'

const activeTab = ref('manuelle')
const selectedFiliere = ref('')
const selectedEtudiant = ref(null)
const selectedEncadrant = ref(null)
const etudiants = ref([])
const encadrants = ref([])
const loading = ref(true)

const loadData = async () => {
  try {
    loading.value = true
    
    // Charger les étudiants
    const etudiantsRes = await api.get('/etudiants')
    // if (etudiantsRes.data.success) {
    //   etudiants.value = etudiantsRes.data.data.map(e => ({
    //     id: e.id,
    //     nom: e.nom,
    //     filiere: e.filiere,
    //     affecte: !!e.encadrantNom,
    //     encadrant: e.encadrantNom
    //   }))
    // }
    if (etudiantsRes.success) {
        etudiants.value = etudiantsRes.data.map(e => ({
            id: e.id,
            nom: e.nom,
            filiere: e.filiere,
            affecte: !!e.encadrantNom,
            encadrant: e.encadrantNom
        }))
    }
    
    // Charger les encadrants avec leur charge
    const encadrantsRes = await api.get('/affectations/encadrants/charge')
    // if (encadrantsRes.data.success) {
    //   encadrants.value = encadrantsRes.data.data.map(enc => ({
    //     id: enc.id,
    //     nom: enc.nom,
    //     current: enc.nombreEtudiants,
    //     max: enc.capaciteMax,
    //     status: enc.statut,
    //     statusClass: enc.statut === 'Disponible' ? 'disponible' : 'sature'
    //   }))
    // }
    if (encadrantsRes.success) {
        encadrants.value = encadrantsRes.data.map(enc => ({
            id: enc.id,
            nom: enc.nom,
            current: enc.nombreEtudiants,
            max: enc.capaciteMax,
            status: enc.statut,
            statusClass: enc.statut === 'Disponible' ? 'disponible' : 'sature'
        }))
    }
  } catch (err) {
    console.error('Erreur chargement données:', err)
  } finally {
    loading.value = false
  }
}

const filteredEtudiants = computed(() => {
  if (!selectedFiliere.value) return etudiants.value
  return etudiants.value.filter(e => e.filiere === selectedFiliere.value)
})

const selectEtudiant = (etudiant) => {
  if (!etudiant.affecte) {
    selectedEtudiant.value = etudiant
  }
}

const selectEncadrant = (encadrant) => {
  if (encadrant.current < encadrant.max) {
    selectedEncadrant.value = encadrant
  }
}

const affecterManuellement = async () => {
  try {
    await affectationService.affecterManuellement(
      selectedEtudiant.value.id,
      selectedEncadrant.value.id
    )
    alert('Affectation réussie !')
    loadData() // Recharger les données
  } catch (error) {
    alert('Erreur lors de l\'affectation')
  }
}

const affecterAutomatiquement = async () => {
  try {
    const result = await affectationService.affecterAutomatiquement({
      filiere: selectedFiliere.value || null,
      repartitionEquitable: true
    })
    alert(`Affectation automatique terminée: ${result.data.nombreAffectations} succès`)
    loadData() // Recharger les données
  } catch (error) {
    alert('Erreur lors de l\'affectation automatique')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.tabs {
  display: flex;
  gap: 8px;
  background: var(--bg-white);
  padding: 4px;
  border-radius: var(--radius-md);
  width: fit-content;
}

.tab {
  padding: 10px 24px;
  border: none;
  background: transparent;
  border-radius: var(--radius-md);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  color: var(--text-gray);
}

.tab.active {
  background: var(--yellow-accent);
  color: var(--text-dark);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-select {
  padding: 8px 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: var(--radius-md);
  font-size: 14px;
  background: var(--bg-white);
}

.etudiants-list,
.encadrants-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 500px;
  overflow-y: auto;
}

.etudiant-card,
.encadrant-card {
  background: var(--bg-white);
  padding: 16px;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.2s ease;
  border: 2px solid transparent;
}

.etudiant-card:hover,
.encadrant-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.etudiant-card.selected,
.encadrant-card.selected {
  border-color: var(--yellow-accent);
  box-shadow: var(--shadow-md);
}

.etudiant-info {
  flex: 1;
}

.etudiant-name,
.encadrant-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-dark);
  margin-bottom: 4px;
}

.etudiant-filiere {
  font-size: 13px;
  color: var(--text-gray);
  margin-bottom: 4px;
}

.etudiant-encadrant {
  font-size: 12px;
  color: var(--text-gray);
  font-style: italic;
}

.encadrant-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.encadrant-count {
  font-size: 13px;
  color: var(--text-gray);
  font-weight: 500;
}
</style>