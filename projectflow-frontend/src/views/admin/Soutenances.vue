<template>
  <div class="soutenances-page fade-in">
    <div class="page-header">
      <div>
        <h1>Gestion des Soutenances</h1>
        <p class="subtitle">Planifier et gérer les soutenances de PFE</p>
      </div>
      <button class="btn btn-primary" @click="openAddModal">
        <Plus :size="18" />
        Nouvelle Soutenance
      </button>
    </div>

    <!-- État de chargement -->
    <div v-if="loading" class="loading-state">
      Chargement des soutenances...
    </div>

    <!-- État d'erreur -->
    <div v-if="error" class="error-state">
      {{ error }}
      <button @click="loadSoutenances">Réessayer</button>
    </div>

    <!-- Liste des soutenances -->
    <div v-if="!loading && !error" class="soutenances-list">
      <div
        v-for="soutenance in soutenances"
        :key="soutenance.id"
        class="soutenance-card card"
      >
        <div class="soutenance-header">
          <div>
            <h3 class="etudiant-name">{{ soutenance.etudiant }}</h3>
            <p class="projet-titre">{{ soutenance.projetTitre }}</p>
            <span class="badge status-badge" :class="getStatusClass(soutenance.statut)">
              {{ soutenance.statut }}
            </span>
          </div>
          <div class="actions">
            <button class="btn-icon" @click="editSoutenance(soutenance)" title="Modifier">
              <Edit2 :size="18" />
            </button>
            <button class="btn-icon" @click="deleteSoutenance(soutenance.id)" title="Supprimer">
              <Trash2 :size="18" />
            </button>
          </div>
        </div>

        <!-- Infos date/heure/salle -->
        <div class="grid grid-3 mt-3">
          <div class="info-card blue">
            <Calendar :size="20" class="info-icon" />
            <div>
              <div class="info-label">Date</div>
              <div class="info-value">{{ soutenance.date }}</div>
            </div>
          </div>

          <div class="info-card yellow">
            <Clock :size="20" class="info-icon" />
            <div>
              <div class="info-label">Heure</div>
              <div class="info-value">{{ soutenance.heure }}</div>
            </div>
          </div>

          <div class="info-card purple">
            <MapPin :size="20" class="info-icon" />
            <div>
              <div class="info-label">Salle</div>
              <div class="info-value">{{ soutenance.salle }}</div>
            </div>
          </div>
        </div>

        <!-- Composition du jury -->
        <div class="jury-section mt-3">
          <div class="jury-header">
            <Users :size="18" />
            <span>Composition du Jury</span>
          </div>
          
          <div v-if="hasJury(soutenance.jury)" class="jury-grid">
            <div class="jury-member">
              <span class="badge jury">Président</span>
              <span class="jury-name">{{ soutenance.jury.president }}</span>
            </div>
            <div class="jury-member">
              <span class="badge jury">Rapporteur</span>
              <span class="jury-name">{{ soutenance.jury.rapporteur }}</span>
            </div>
            <div class="jury-member">
              <span class="badge jury">Examinateur</span>
              <span class="jury-name">{{ soutenance.jury.examinateur }}</span>
            </div>
          </div>
          
          <div v-else class="jury-empty">
            <AlertCircle :size="16" />
            <span>Jury non encore constitué</span>
          </div>
        </div>
      </div>

      <!-- Message si aucune soutenance -->
      <div v-if="soutenances.length === 0" class="empty-state">
        <Calendar :size="48" />
        <h3>Aucune soutenance planifiée</h3>
        <p>Commencez par créer une nouvelle soutenance</p>
      </div>
    </div>

    <!-- Modal ajout/modification -->
    <div v-if="showAddModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>{{ isEditing ? 'Modifier' : 'Nouvelle' }} Soutenance</h2>
          <button class="btn-icon" @click="closeModal">
            <X :size="20" />
          </button>
        </div>

        <form @submit.prevent="submitForm" class="form-container">
          <!-- Sélection de l'étudiant -->
          <div class="form-group">
            <label>Étudiant <span class="required">*</span></label>
            <select v-model="formData.etudiantId" required :disabled="isEditing">
              <option value="">Sélectionner un étudiant</option>
              <option v-for="etudiant in etudiants" :key="etudiant.id" :value="etudiant.id">
                {{ etudiant.nom }} - {{ etudiant.filiere }}
              </option>
            </select>
          </div>

          <!-- Titre du projet -->
          <div class="form-group">
            <label>Titre du Projet</label>
            <input 
              v-model="formData.projetTitre" 
              type="text" 
              placeholder="Ex: Système de gestion..."
            />
          </div>

          <!-- Date et Heure -->
          <div class="grid grid-2">
            <div class="form-group">
              <label>Date <span class="required">*</span></label>
              <input v-model="formData.date" type="date" required />
            </div>

            <div class="form-group">
              <label>Heure <span class="required">*</span></label>
              <input v-model="formData.heure" type="time" required />
            </div>
          </div>

          <!-- Salle -->
          <div class="form-group">
            <label>Salle <span class="required">*</span></label>
            <input 
              v-model="formData.salle" 
              type="text" 
              placeholder="Ex: A101"
              required 
            />
          </div>

          <!-- Composition du Jury -->
          <div class="jury-section-form">
            <h3>
              <Users :size="18" />
              Composition du Jury
            </h3>

            <div class="form-group">
              <label>Président du Jury</label>
              <select v-model="formData.presidentId">
                <option value="">Sélectionner un professeur</option>
                <option v-for="prof in professeurs" :key="prof.id" :value="prof.id">
                  {{ prof.nom }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label>Rapporteur</label>
              <select v-model="formData.rapporteurId">
                <option value="">Sélectionner un professeur</option>
                <option v-for="prof in professeurs" :key="prof.id" :value="prof.id">
                  {{ prof.nom }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label>Examinateur</label>
              <select v-model="formData.examinateurId">
                <option value="">Sélectionner un professeur</option>
                <option v-for="prof in professeurs" :key="prof.id" :value="prof.id">
                  {{ prof.nom }}
                </option>
              </select>
            </div>
          </div>

          <!-- Boutons d'action -->
          <div class="form-actions">
            <button type="button" class="btn btn-secondary" @click="closeModal">
              Annuler
            </button>
            <button type="submit" class="btn btn-primary" :disabled="submitting">
              <span v-if="submitting">Enregistrement...</span>
              <span v-else>{{ isEditing ? 'Modifier' : 'Créer' }}</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  Plus,
  Edit2,
  Trash2,
  Calendar,
  Clock,
  MapPin,
  Users,
  X,
  AlertCircle
} from 'lucide-vue-next'
import api from '../../services/api'

// État du composant
const showAddModal = ref(false)
const isEditing = ref(false)
const submitting = ref(false)
const loading = ref(true)
const error = ref(null)

// Données
const soutenances = ref([])
const etudiants = ref([])
const professeurs = ref([])

// Formulaire
const formData = ref({
  etudiantId: '',
  projetTitre: '',
  date: '',
  heure: '',
  salle: '',
  presidentId: '',
  rapporteurId: '',
  examinateurId: ''
})

// Charger les soutenances
const loadSoutenances = async () => {
  try {
    loading.value = true
    error.value = null
    
    const response = await api.get('/soutenances')
    
    const soutenancesData = Array.isArray(response.data) 
      ? response.data 
      : response.data?.data || []
    
    if (Array.isArray(soutenancesData)) {
      soutenances.value = soutenancesData.map(soutenance => ({
        id: soutenance.id,
        etudiantId: soutenance.etudiantId,
        etudiant: soutenance.etudiantNom || '—',
        projetTitre: soutenance.projetTitre || 'Projet PFE',
        date: formatDate(soutenance.date),
        dateOriginal: soutenance.date,
        heure: formatHeure(soutenance.heure),
        heureOriginal: soutenance.heure,
        salle: soutenance.salle ? `Salle ${soutenance.salle}` : 'Non définie',
        salleOriginal: soutenance.salle,
        statut: soutenance.statut || 'Planifiée',
        filiere: soutenance.filiere,
        encadrant: soutenance.encadrantNom,
        jury: {
          presidentId: soutenance.jury?.presidentId,
          president: soutenance.jury?.presidentNom || 'Non défini',
          rapporteurId: soutenance.jury?.rapporteurId,
          rapporteur: soutenance.jury?.rapporteurNom || 'Non défini',
          examinateurId: soutenance.jury?.examinateurId,
          examinateur: soutenance.jury?.examinateurNom || 'Non défini'
        }
      }))
      
      console.log('✅ Soutenances chargées:', soutenances.value)
    }
    
  } catch (err) {
    console.error('❌ Erreur chargement soutenances:', err)
    error.value = 'Impossible de charger les soutenances'
  } finally {
    loading.value = false
  }
}

// Charger les étudiants et professeurs
const loadFormData = async () => {
  try {
    // Charger les étudiants
    const etudiantsRes = await api.get('/etudiants')
    etudiants.value = Array.isArray(etudiantsRes.data) 
      ? etudiantsRes.data 
      : etudiantsRes.data?.data || []

    // Charger les professeurs
    const profsRes = await api.get('/encadrants')
    professeurs.value = Array.isArray(profsRes.data) 
      ? profsRes.data 
      : profsRes.data?.data || []

    console.log('✅ Données formulaire chargées')
  } catch (err) {
    console.error('❌ Erreur chargement formulaire:', err)
  }
}

// Formater la date
const formatDate = (dateString) => {
  if (!dateString) return 'N/A'
  
  try {
    const date = new Date(dateString)
    if (isNaN(date.getTime())) return 'N/A'
    
    return date.toLocaleDateString('fr-FR', {
      weekday: 'long',
      day: 'numeric',
      month: 'long',
      year: 'numeric'
    })
  } catch {
    return 'N/A'
  }
}

// Formater l'heure
const formatHeure = (heureString) => {
  if (!heureString) return 'N/A'
  
  try {
    const [heure, minute] = heureString.split(':')
    return `${heure}h${minute}`
  } catch {
    return heureString
  }
}

// Vérifier si le jury est constitué
const hasJury = (jury) => {
  return jury && (
    (jury.president && jury.president !== 'Non défini') ||
    (jury.rapporteur && jury.rapporteur !== 'Non défini') ||
    (jury.examinateur && jury.examinateur !== 'Non défini')
  )
}

// Obtenir la classe CSS du statut
const getStatusClass = (statut) => {
  const statutMap = {
    'Planifiée': 'en-cours',
    'Terminée': 'valide',
    'Annulée': 'a-corriger'
  }
  return statutMap[statut] || 'en-cours'
}

// Réinitialiser le formulaire
const resetForm = () => {
  formData.value = {
    etudiantId: '',
    projetTitre: '',
    date: '',
    heure: '',
    salle: '',
    presidentId: '',
    rapporteurId: '',
    examinateurId: ''
  }
  isEditing.value = false
}

// Ouvrir le modal d'ajout
const openAddModal = () => {
  resetForm()
  showAddModal.value = true
}

// Fermer le modal
const closeModal = () => {
  showAddModal.value = false
  resetForm()
}

// Soumettre le formulaire
const submitForm = async () => {
  try {
    submitting.value = true

    const payload = {
      etudiantId: parseInt(formData.value.etudiantId),
      date: formData.value.date,
      heure: formData.value.heure + ':00', // Ajouter les secondes
      salle: formData.value.salle,
      presidentId: formData.value.presidentId ? parseInt(formData.value.presidentId) : null,
      rapporteurId: formData.value.rapporteurId ? parseInt(formData.value.rapporteurId) : null,
      examinateurId: formData.value.examinateurId ? parseInt(formData.value.examinateurId) : null
    }

    console.log('📤 Payload envoyé:', payload)

    if (isEditing.value) {
      // Modification
      await api.put(`/soutenances/${formData.value.id}`, payload)
      alert('Soutenance modifiée avec succès !')
    } else {
      // Création
      await api.post('/soutenances', payload)
      alert('Soutenance créée avec succès !')
    }

    closeModal()
    await loadSoutenances()

  } catch (err) {
    console.error('❌ Erreur soumission:', err)
    alert('Erreur lors de l\'enregistrement: ' + (err.response?.data?.message || err.message))
  } finally {
    submitting.value = false
  }
}

// Modifier une soutenance existante
const editSoutenance = (soutenance) => {
  isEditing.value = true
  formData.value = {
    id: soutenance.id,
    etudiantId: soutenance.etudiantId || '',
    projetTitre: soutenance.projetTitre || '',
    date: soutenance.dateOriginal || '',
    heure: soutenance.heureOriginal?.substring(0, 5) || '', // Format HH:MM
    salle: soutenance.salleOriginal || '',
    presidentId: soutenance.jury?.presidentId || '',
    rapporteurId: soutenance.jury?.rapporteurId || '',
    examinateurId: soutenance.jury?.examinateurId || ''
  }
  showAddModal.value = true
}

// Supprimer une soutenance
const deleteSoutenance = async (id) => {
  if (!confirm('Êtes-vous sûr de vouloir supprimer cette soutenance ?')) {
    return
  }

  try {
    await api.delete(`/soutenances/${id}`)
    soutenances.value = soutenances.value.filter(s => s.id !== id)
    alert('Soutenance supprimée avec succès !')
  } catch (error) {
    console.error('❌ Erreur suppression:', error)
    alert('Erreur lors de la suppression')
  }
}

// Charger les données au montage
onMounted(() => {
  loadSoutenances()
  loadFormData()
})
</script>

<style scoped>
/* Page header */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 32px;
}

/* États de chargement et erreur */
.loading-state,
.error-state {
  text-align: center;
  padding: 48px;
  color: var(--text-gray);
}

.error-state button {
  margin-top: 16px;
  padding: 8px 16px;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
}

/* Liste des soutenances */
.soutenances-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.soutenance-card {
  padding: 24px;
}

.soutenance-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding-bottom: 16px;
  border-bottom: 1px solid #F0F0F0;
}

.etudiant-name {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-dark);
  margin-bottom: 4px;
}

.projet-titre {
  font-size: 14px;
  color: var(--text-gray);
  margin: 0 0 8px 0;
}

.status-badge {
  display: inline-block;
  margin-top: 4px;
}

.actions {
  display: flex;
  gap: 8px;
}

/* Info cards */
.info-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  border-radius: var(--radius-md);
}

.info-card.blue {
  background: #E3F2FD;
}

.info-card.yellow {
  background: #FFF9C4;
}

.info-card.purple {
  background: #F3E5F5;
}

.info-icon {
  color: var(--text-dark);
}

.info-label {
  font-size: 12px;
  color: var(--text-gray);
  margin-bottom: 2px;
}

.info-value {
  font-size: 16px;
  font-weight: 700;
  color: var(--text-dark);
}

/* Section jury */
.jury-section {
  padding-top: 16px;
  border-top: 1px solid #F0F0F0;
}

.jury-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
  margin-bottom: 16px;
}

.jury-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.jury-member {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 12px;
  background: #FFF8E1;
  border-radius: var(--radius-md);
}

.jury-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
}

.badge.jury {
  background: #FFE082;
  color: #F57C00;
  font-size: 11px;
  padding: 4px 8px;
}

.jury-empty {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px;
  background: #F5F5F5;
  border-radius: var(--radius-md);
  color: var(--text-gray);
  font-size: 14px;
}

/* État vide */
.empty-state {
  text-align: center;
  padding: 64px 32px;
  color: var(--text-gray);
}

.empty-state svg {
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-state h3 {
  margin-bottom: 8px;
  color: var(--text-dark);
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-content {
  background: var(--bg-white);
  border-radius: var(--radius-lg);
  max-width: 700px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid #F0F0F0;
}

.modal-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: var(--text-dark);
}

/* Formulaire */
.form-container {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
  margin-bottom: 8px;
}

.required {
  color: #E74C3C;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #E0E0E0;
  border-radius: var(--radius-md);
  font-size: 14px;
  font-family: inherit;
  transition: all 0.3s ease;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(101, 67, 58, 0.1);
}

.form-group input:disabled,
.form-group select:disabled {
  background: #F5F5F5;
  cursor: not-allowed;
}

.jury-section-form {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #F0F0F0;
}

.jury-section-form h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-dark);
  margin-bottom: 16px;
}

/* Actions du formulaire */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #F0F0F0;
}

.btn {
  padding: 12px 24px;
  border-radius: var(--radius-md);
  font-size: 14px;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.btn-primary {
  background: #FFD275;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: #7A4F44;
  transform: translateY(-1px);
}

.btn-secondary {
  background: #F5F5F5;
  color: var(--text-dark);
}

.btn-secondary:hover {
  background: #E0E0E0;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-icon {
  padding: 8px;
  background: transparent;
  border: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.btn-icon:hover {
  background: #F5F5F5;
}

/* Utilitaires */
.mt-3 {
  margin-top: 16px;
}

.grid {
  display: grid;
  gap: 16px;
}

.grid-2 {
  grid-template-columns: repeat(2, 1fr);
}

.grid-3 {
  grid-template-columns: repeat(3, 1fr);
}

/* Badges de statut */
.badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.badge.valide {
  background: #d4edda;
  color: #155724;
}

.badge.en-cours {
  background: #fff3cd;
  color: #856404;
}

.badge.a-corriger {
  background: #f8d7da;
  color: #721c24;
}

/* Animation */
.fade-in {
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Responsive */
@media (max-width: 768px) {
  .grid-2,
  .grid-3 {
    grid-template-columns: 1fr;
  }

  .jury-grid {
    grid-template-columns: 1fr;
  }

  .soutenance-header {
    flex-direction: column;
    gap: 16px;
  }

  .modal-content {
    width: 95%;
    max-height: 95vh;
  }
}
</style>