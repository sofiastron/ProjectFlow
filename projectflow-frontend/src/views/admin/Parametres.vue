<template>
  <div class="parametres-page fade-in">
    <div class="page-header">
      <h1>Paramètres</h1>
      <p class="subtitle">Configuration des affectations, soutenances et notifications</p>
    </div>

    <div class="grid grid-2">
      <!-- Paramètres d'affectation -->
      <div class="card">
        <h2 class="section-title">
          <Users :size="20" />
          Affectation
        </h2>

        <div class="param-list">
          <div class="param-item">
            <div class="param-info">
              <div class="param-label">Capacité maximale par encadrant</div>
              <div class="param-description">Nombre maximum d'étudiants par encadrant</div>
            </div>
            <input
              type="number"
              v-model="params.capaciteMax"
              class="param-input"
              min="1"
              max="20"
            />
          </div>

          <div class="param-item">
            <div class="param-info">
              <div class="param-label">Notification d'affectation</div>
              <div class="param-description">Activer les notifications automatiques</div>
            </div>
            <label class="toggle">
              <input type="checkbox" v-model="params.notifAffectation" />
              <span class="toggle-slider"></span>
            </label>
          </div>
        </div>
      </div>

      <!-- Paramètres de soutenance -->
      <div class="card">
        <h2 class="section-title">
          <Calendar :size="20" />
          Soutenances
        </h2>

        <div class="param-list">
          <div class="param-item">
            <div class="param-info">
              <div class="param-label">Durée de soutenance (minutes)</div>
              <div class="param-description">Durée standard d'une soutenance</div>
            </div>
            <input
              type="number"
              v-model="params.dureeSoutenance"
              class="param-input"
              min="30"
              max="180"
              step="15"
            />
          </div>

          <div class="param-item">
            <div class="param-info">
              <div class="param-label">Délai minimum (jours)</div>
              <div class="param-description">Délai minimum pour planifier une soutenance</div>
            </div>
            <input
              type="number"
              v-model="params.delaiMin"
              class="param-input"
              min="1"
              max="30"
            />
          </div>

          <div class="param-item">
            <div class="param-info">
              <div class="param-label">Notification de soutenance</div>
              <div class="param-description">Rappels automatiques avant soutenance</div>
            </div>
            <label class="toggle">
              <input type="checkbox" v-model="params.notifSoutenance" />
              <span class="toggle-slider"></span>
            </label>
          </div>
        </div>
      </div>
    </div>

    <!-- Bouton sauvegarder -->
    <div class="actions-footer">
      <button class="btn btn-secondary" @click="resetParams">
        <RotateCcw :size="18" />
        Réinitialiser
      </button>
      <button class="btn btn-primary" @click="saveParams">
        <Save :size="18" />
        Enregistrer les modifications
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Users, Calendar, Save, RotateCcw } from 'lucide-vue-next'
import api from '../../services/api'

const params = ref({
  capaciteMax: 5,
  notifAffectation: true,
  dureeSoutenance: 60,
  delaiMin: 7,
  notifSoutenance: true
})

const loading = ref(true)
const error = ref(null)
const saving = ref(false)

const loadParams = async () => {
  try {
    loading.value = true
    error.value = null
    
    const response = await api.get('/parametres')
    
    if (response.data.success) {
      const parametres = response.data.data
      
      // Mapper les paramètres
      parametres.forEach(param => {
        if (param.cle === 'capacite_max_encadrant') {
          params.value.capaciteMax = parseInt(param.valeur)
        } else if (param.cle === 'duree_soutenance_minutes') {
          params.value.dureeSoutenance = parseInt(param.valeur)
        } else if (param.cle === 'notification_affectation_enabled') {
          params.value.notifAffectation = param.valeur === 'true'
        } else if (param.cle === 'notification_soutenance_enabled') {
          params.value.notifSoutenance = param.valeur === 'true'
        } else if (param.cle === 'delai_min_soutenance_jours') {
          params.value.delaiMin = parseInt(param.valeur)
        }
      })
    }
  } catch (err) {
    console.error('Erreur chargement paramètres:', err)
    error.value = 'Impossible de charger les paramètres'
  } finally {
    loading.value = false
  }
}

const saveParams = async () => {
  try {
    saving.value = true
    
    // Sauvegarder chaque paramètre
    await api.put('/parametres/capacite_max_encadrant', null, {
      params: { valeur: params.value.capaciteMax }
    })
    
    await api.put('/parametres/duree_soutenance_minutes', null, {
      params: { valeur: params.value.dureeSoutenance }
    })
    
    await api.put('/parametres/notification_affectation_enabled', null, {
      params: { valeur: params.value.notifAffectation }
    })
    
    await api.put('/parametres/notification_soutenance_enabled', null, {
      params: { valeur: params.value.notifSoutenance }
    })
    
    if (params.value.delaiMin) {
      await api.put('/parametres/delai_min_soutenance_jours', null, {
        params: { valeur: params.value.delaiMin }
      })
    }
    
    alert('Paramètres enregistrés avec succès !')
  } catch (err) {
    console.error('Erreur sauvegarde paramètres:', err)
    alert('Erreur lors de la sauvegarde')
  } finally {
    saving.value = false
  }
}

const resetParams = () => {
  params.value = {
    capaciteMax: 5,
    notifAffectation: true,
    dureeSoutenance: 60,
    delaiMin: 7,
    notifSoutenance: true
  }
}

onMounted(() => {
  loadParams()
})
</script>

<style scoped>
.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 18px;
  font-weight: 700;
  color: var(--text-dark);
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid var(--bg-beige);
}

.param-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.param-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

.param-info {
  flex: 1;
}

.param-label {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
  margin-bottom: 4px;
}

.param-description {
  font-size: 13px;
  color: var(--text-gray);
}

.param-input {
  width: 80px;
  padding: 8px 12px;
  border: 1px solid #E5E5E5;
  border-radius: var(--radius-md);
  font-size: 14px;
  font-weight: 600;
  text-align: center;
}

/* Toggle Switch */
.toggle {
  position: relative;
  display: inline-block;
  width: 52px;
  height: 28px;
}

.toggle input {
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #E5E5E5;
  transition: 0.3s;
  border-radius: 34px;
}

.toggle-slider:before {
  position: absolute;
  content: "";
  height: 22px;
  width: 22px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: 0.3s;
  border-radius: 50%;
}

.toggle input:checked + .toggle-slider {
  background-color: var(--yellow-accent);
}

.toggle input:checked + .toggle-slider:before {
  transform: translateX(24px);
}

.actions-footer {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #E5E5E5;
}
</style>