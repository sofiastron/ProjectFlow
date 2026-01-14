<template>
  <div class="dashboard fade-in">
    <!-- En-tête avec date et heure -->
    <div class="page-header">
      <div>
        <h1>Tableau de Bord</h1>
        <p class="subtitle">{{ currentDate }}</p>
      </div>
      <div class="header-actions">
        <button class="btn-icon" @click="loadDashboardData" title="Actualiser">
          <RefreshCw :size="20" :class="{ rotating: loading }" />
        </button>
      </div>
    </div>

    <!-- Message de chargement -->
    <div v-if="loading && !stats.etudiantsTotal" class="loading-state">
      <div class="spinner"></div>
      <p>Chargement des données...</p>
    </div>

    <template v-else>
      <!-- Cartes statistiques avec animation -->
      <div class="grid grid-4 mb-4">
        <StatCard
          :icon="Users"
          :value="stats.etudiantsTotal"
          label="Étudiants Total"
          color="blue"
        />
        <StatCard
          :icon="UserCheck"
          :value="stats.encadrantsActifs"
          label="Encadrants Actifs"
          color="green"
        />
        <StatCard
          :icon="FileText"
          :value="stats.rapportsEnAttente"
          label="Rapports en Attente"
          color="yellow"
        />
        <StatCard
          :icon="Calendar"
          :value="stats.soutenancesPlanifiees"
          label="Soutenances à Venir"
          color="purple"
        />
      </div>

      <!-- Section principale: Affectation + Charge des encadrants -->
      <div class="grid grid-2 mb-4">
        <!-- Taux d'affectation - Plus large -->
        <div class="card chart-card affectation-card">
          <div class="card-header-inline">
            <h2>
              <TrendingUp :size="20" />
              Taux d'Affectation
            </h2>
          </div>
          <div class="affectation-visual">
            <div class="circular-progress">
              <svg viewBox="0 0 200 200">
                <circle
                  cx="100"
                  cy="100"
                  r="80"
                  fill="none"
                  stroke="#F0F0F0"
                  stroke-width="20"
                />
                <circle
                  cx="100"
                  cy="100"
                  r="80"
                  fill="none"
                  stroke="#10B981"
                  stroke-width="20"
                  stroke-dasharray="502.4"
                  :stroke-dashoffset="502.4 - (502.4 * tauxAffectation / 100)"
                  transform="rotate(-90 100 100)"
                  class="progress-circle"
                />
              </svg>
              <div class="circular-text">
                <span class="percentage">{{ tauxAffectation }}%</span>
                <span class="label">Affectés</span>
              </div>
            </div>
            <div class="affectation-details">
              <div class="detail-item">
                <CheckCircle2 :size="16" class="text-green" />
                <span>{{ stats.etudiantsAffectes }} Affectés</span>
              </div>
              <div class="detail-item">
                <AlertCircle :size="16" class="text-yellow" />
                <span>{{ stats.etudiantsNonAffectes }} Non affectés</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Charge des encadrants - Compact -->
        <div class="card encadrants-card">
          <div class="card-header-inline">
            <h2>
              <BarChart3 :size="20" />
              Top Encadrants
            </h2>
            <span class="badge-info">Top 5</span>
          </div>
          <div class="encadrants-list">
            <div
              v-for="(encadrant, index) in chargeEncadrants"
              :key="encadrant.id"
              class="encadrant-item"
            >
              <div class="encadrant-rank">
                <span class="rank-number" :class="getRankClass(index)">
                  {{ index + 1 }}
                </span>
              </div>
              <div class="encadrant-details">
                <div class="encadrant-info">
                  <span class="encadrant-name">{{ encadrant.nom }}</span>
                  <span class="encadrant-count">
                    <Users :size="14" />
                    {{ encadrant.current }}/{{ encadrant.max }}
                  </span>
                </div>
                <ProgressBar
                  :current="encadrant.current"
                  :max="encadrant.max"
                />
              </div>
            </div>

            <!-- Message si vide -->
            <div v-if="chargeEncadrants.length === 0" class="empty-state-small">
              <Users :size="32" />
              <p>Aucun encadrant</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Section: Soutenances à venir - Pleine largeur -->
      <div class="card soutenances-card mb-4">
        <div class="card-header-inline">
          <h2>
            <CalendarDays :size="20" />
            Soutenances à Venir
          </h2>
          <button class="btn-primary-sm" @click="$router.push('/soutenances')">
            <Plus :size="16" />
            Planifier
          </button>
        </div>
        <div class="soutenances-grid">
          <div
            v-for="soutenance in prochainsSoutenances"
            :key="soutenance.id"
            class="soutenance-mini"
          >
            <div class="soutenance-date">
              <span class="date-day">{{ soutenance.day }}</span>
              <span class="date-month">{{ soutenance.month }}</span>
            </div>
            <div class="soutenance-info">
              <h4>{{ soutenance.etudiant }}</h4>
              <p>{{ soutenance.heure }} • {{ soutenance.salle }}</p>
            </div>
            <span class="badge badge-sm badge-purple">
              {{ soutenance.statut }}
            </span>
          </div>

          <!-- Message si vide -->
          <div v-if="prochainsSoutenances.length === 0" class="empty-state-inline">
            <Calendar :size="48" />
            <div>
              <h3>Aucune soutenance planifiée</h3>
              <p>Commencez par planifier une soutenance</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Section: Activités récentes - Pleine largeur -->
      <div class="card activities-card">
        <div class="card-header-inline">
          <h2>
            <Activity :size="20" />
            Activités Récentes
          </h2>
        </div>
        <div class="activities-timeline-horizontal">
          <div
            v-for="activity in recentActivities"
            :key="activity.id"
            class="activity-card"
          >
            <div class="activity-icon-large" :class="`bg-${activity.color}`">
              <component :is="activity.icon" :size="24" />
            </div>
            <div class="activity-content">
              <p class="activity-title">{{ activity.text }}</p>
              <span class="activity-time">
                <Clock :size="14" />
                {{ activity.time }}
              </span>
            </div>
          </div>

          <!-- Message si vide -->
          <div v-if="recentActivities.length === 0" class="empty-state-inline">
            <Activity :size="48" />
            <div>
              <h3>Aucune activité récente</h3>
              <p>Les activités apparaîtront ici</p>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
// [Le script reste identique - pas de changement]
import { ref, computed, onMounted } from 'vue'
import {
  Users,
  UserCheck,
  FileText,
  Calendar,
  CheckCircle,
  Clock,
  CalendarCheck,
  RefreshCw,
  PieChart,
  TrendingUp,
  Activity,
  BarChart3,
  CalendarDays,
  Plus,
  CheckCircle2,
  AlertCircle
} from 'lucide-vue-next'
import StatCard from '../../components/StatCard.vue'
import ProgressBar from '../../components/ProgressBar.vue'
import api from '../../services/api'

const loading = ref(false)
const error = ref(null)

const currentDate = computed(() => {
  const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }
  return new Date().toLocaleDateString('fr-FR', options)
})

const stats = ref({
  etudiantsTotal: 0,
  etudiantsAffectes: 0,
  etudiantsNonAffectes: 0,
  encadrantsActifs: 0,
  rapportsEnAttente: 0,
  soutenancesPlanifiees: 0
})

const tauxAffectation = computed(() => {
  if (stats.value.etudiantsTotal === 0) return 0
  return Math.round((stats.value.etudiantsAffectes / stats.value.etudiantsTotal) * 100)
})

const recentActivities = ref([])
const chargeEncadrants = ref([])
const prochainsSoutenances = ref([])

const getRankClass = (index) => {
  if (index === 0) return 'rank-gold'
  if (index === 1) return 'rank-silver'
  if (index === 2) return 'rank-bronze'
  return 'rank-default'
}

const loadDashboardData = async () => {
  try {
    loading.value = true
    error.value = null

    const etudiantsRes = await api.get('/etudiants/statistiques')
    const etudiantsData = etudiantsRes.data || etudiantsRes
    
    if (etudiantsData) {
      stats.value.etudiantsTotal = etudiantsData.total || 0
      stats.value.etudiantsAffectes = etudiantsData.affectes || 0
      stats.value.etudiantsNonAffectes = etudiantsData.nonAffectes || 0
    }

    const encadrantsRes = await api.get('/encadrants/statistiques')
    const encadrantsData = encadrantsRes.data || encadrantsRes
    
    if (encadrantsData) {
      stats.value.encadrantsActifs = encadrantsData.total || 0
    }

    const rapportsRes = await api.get('/rapports/statistiques')
    const rapportsData = rapportsRes.data || rapportsRes
    
    if (rapportsData) {
      stats.value.rapportsEnAttente = (rapportsData.enCours || 0) + (rapportsData.aCorriger || 0)
    }

    const soutenancesRes = await api.get('/soutenances')
    const soutenancesData = Array.isArray(soutenancesRes.data) 
      ? soutenancesRes.data 
      : soutenancesRes.data?.data || []
    
    stats.value.soutenancesPlanifiees = soutenancesData.length

    prochainsSoutenances.value = soutenancesData.slice(0, 4).map(s => {
      const date = new Date(s.date)
      return {
        id: s.id,
        etudiant: s.etudiantNom || 'Étudiant',
        heure: s.heure?.substring(0, 5) || 'N/A',
        salle: s.salle ? `Salle ${s.salle}` : 'N/A',
        statut: s.statut || 'Planifiée',
        day: date.getDate(),
        month: date.toLocaleDateString('fr-FR', { month: 'short' })
      }
    })

    const professeursRes = await api.get('/encadrants')
    const professeursData = Array.isArray(professeursRes.data) 
      ? professeursRes.data 
      : professeursRes.data?.data || []
    
    chargeEncadrants.value = professeursData
      .sort((a, b) => (b.nombreEtudiantsActuels || 0) - (a.nombreEtudiantsActuels || 0))
      .slice(0, 5)
      .map(enc => ({
        id: enc.id,
        nom: enc.nom,
        current: enc.nombreEtudiantsActuels || 0,
        max: enc.capaciteMax || 5
      }))

    recentActivities.value = [
      {
        id: 1,
        icon: FileText,
        text: 'Nouveau rapport soumis',
        time: 'Il y a 2h',
        color: 'blue',
        type: 'info'
      },
      {
        id: 2,
        icon: CheckCircle,
        text: 'Affectation complétée',
        time: 'Il y a 5h',
        color: 'green',
        type: 'success'
      },
      {
        id: 3,
        icon: CalendarCheck,
        text: 'Soutenance planifiée',
        time: 'Il y a 1j',
        color: 'purple',
        type: 'info'
      }
    ]

    console.log('✅ Dashboard chargé')

  } catch (err) {
    console.error('❌ Erreur dashboard:', err)
    error.value = 'Erreur de chargement'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
/* En-tête */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 32px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.rotating {
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* Chargement */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px;
  color: var(--text-gray);
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #F0F0F0;
  border-top-color: var(--primary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Cartes avec en-tête inline */
.card-header-inline {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.card-header-inline h2 {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0;
  font-size: 18px;
}

.badge-info {
  background: #E0E7FF;
  color: #4F46E5;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

/* Carte d'affectation - Design amélioré */
.affectation-card {
  background: linear-gradient(135deg, #D1FAE5 0%, #E8FAF0 100%);
}

.affectation-visual {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 32px;
  padding: 20px 0;
}

.circular-progress {
  position: relative;
  width: 180px;
  height: 180px;
}

.progress-circle {
  transition: stroke-dashoffset 1s ease;
}

.circular-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.percentage {
  display: block;
  font-size: 42px;
  font-weight: 800;
  color: var(--text-dark);
  line-height: 1;
}

.label {
  display: block;
  font-size: 14px;
  color: var(--text-gray);
  margin-top: 4px;
  font-weight: 600;
}

.affectation-details {
  display: flex;
  gap: 32px;
  width: 100%;
  justify-content: center;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: var(--text-dark);
}

.text-green { color: #10B981; }
.text-yellow { color: #F59E0B; }

/* Encadrants - Design compact */
.encadrants-card {
  background: linear-gradient(135deg, #E0E7FF 0%, #EEF2FF 100%);
}

.encadrants-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.encadrant-item {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.encadrant-rank {
  flex-shrink: 0;
}

.rank-number {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  font-size: 14px;
  font-weight: 700;
  color: white;
}

.rank-gold { background: linear-gradient(135deg, #FFD700, #FFA500); }
.rank-silver { background: linear-gradient(135deg, #C0C0C0, #A8A8A8); }
.rank-bronze { background: linear-gradient(135deg, #CD7F32, #B87333); }
.rank-default { background: #E0E0E0; color: var(--text-gray); }

.encadrant-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.encadrant-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.encadrant-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
}

.encadrant-count {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 600;
  color: var(--text-gray);
}

/* Soutenances - Pleine largeur */
.soutenances-card {
  background: linear-gradient(135deg, #F3E5F5 0%, #FCE4EC 100%);
}

.soutenances-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.soutenance-mini {
  display: flex;
  gap: 12px;
  padding: 16px;
  background: white;
  border-radius: var(--radius-md);
  transition: all 0.2s;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.soutenance-mini:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.soutenance-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #8B5CF6, #A78BFA);
  border-radius: var(--radius-md);
  flex-shrink: 0;
}

.date-day {
  font-size: 20px;
  font-weight: 700;
  color: white;
  line-height: 1;
}

.date-month {
  font-size: 10px;
  color: rgba(255,255,255,0.9);
  text-transform: uppercase;
  font-weight: 600;
}

.soutenance-info {
  flex: 1;
}

.soutenance-info h4 {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
  margin: 0 0 6px 0;
}

.soutenance-info p {
  font-size: 12px;
  color: var(--text-gray);
  margin: 0;
}

.badge-sm {
  padding: 4px 8px;
  font-size: 11px;
  align-self: flex-start;
}

.badge-purple {
  background: #E9D5FF;
  color: #7C3AED;
}

/* Activités - Timeline horizontale */
.activities-card {
  background: linear-gradient(135deg, #FFF7ED 0%, #FEF3C7 100%);
}

.activities-timeline-horizontal {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
}

.activity-card {
  display: flex;
  gap: 12px;
  padding: 16px;
  background: white;
  border-radius: var(--radius-md);
  transition: all 0.2s;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.activity-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.activity-icon-large {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.bg-blue { background: linear-gradient(135deg, #3B82F6, #60A5FA); }
.bg-green { background: linear-gradient(135deg, #10B981, #34D399); }
.bg-purple { background: linear-gradient(135deg, #8B5CF6, #A78BFA); }
.bg-yellow { background: linear-gradient(135deg, #F59E0B, #FBBF24); }

.activity-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.activity-title {
  font-size: 14px;
  color: var(--text-dark);
  margin: 0 0 6px 0;
  font-weight: 600;
}

.activity-time {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: var(--text-gray);
  font-weight: 500;
}

/* Bouton primary small */
.btn-primary-sm {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary-sm:hover {
  background: #7A4F44;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(101, 67, 58, 0.2);
}

/* Empty states */
.empty-state-small {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24px;
  color: var(--text-gray);
  text-align: center;
}

.empty-state-small svg {
  opacity: 0.3;
  margin-bottom: 8px;
}

.empty-state-small p {
  margin: 0;
  font-size: 13px;
}

.empty-state-inline {
  grid-column: 1 / -1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 48px;
  color: var(--text-gray);
  text-align: left;
}

.empty-state-inline svg {
  opacity: 0.3;
  flex-shrink: 0;
}

.empty-state-inline h3 {
  margin: 0 0 4px 0;
  font-size: 16px;
  color: var(--text-dark);
}

.empty-state-inline p {
  margin: 0;
  font-size: 14px;
}

/* Utilitaires */
.mb-4 { margin-bottom: 32px; }
.mt-4 { margin-top: 32px; }

/* Animation fade-in */
.fade-in {
  animation: fadeIn 0.5s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Responsive */
@media (max-width: 1200px) {
  .grid-4 {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .grid-2,
  .grid-4 {
    grid-template-columns: 1fr;
  }

  .soutenances-grid,
  .activities-timeline-horizontal {
    grid-template-columns: 1fr;
  }

  .affectation-details {
    flex-direction: column;
    gap: 16px;
  }
}
</style>