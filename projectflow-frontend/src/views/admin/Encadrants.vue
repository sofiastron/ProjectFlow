<template>
  <div class="encadrants-page fade-in">
    <div class="page-header">
      <div>
        <h1>Gestion des Encadrants</h1>
        <p class="subtitle">Liste et informations des encadrants de PFE</p>
      </div>
      <button class="btn btn-primary" @click="openAddModal">
        <Plus :size="18" />
        Ajouter Encadrant
      </button>
    </div>

    <!-- Message de chargement -->
    <div v-if="loading" class="loading-message">
      <p>⏳ Chargement des encadrants...</p>
    </div>

    <!-- Message d'erreur -->
    <div v-if="error" class="error-message">
      <p>❌ {{ error }}</p>
      <button class="btn btn-primary" @click="loadEncadrants">
        🔄 Réessayer
      </button>
    </div>

    <!-- Grille des encadrants -->
    <div v-if="!loading && !error" class="grid grid-3">
      <div
        v-for="encadrant in encadrants"
        :key="encadrant.id"
        class="encadrant-card card"
      >
        <div class="encadrant-header">
          <div class="avatar" :class="encadrant.avatarColor">
            {{ encadrant.initials }}
          </div>
          <div class="header-actions">
            <span class="badge" :class="encadrant.statusClass">
              {{ encadrant.status }}
            </span>
            <div class="card-actions">
              <button class="btn-icon" @click="editEncadrant(encadrant)" title="Modifier">
                <Edit2 :size="16" />
              </button>
              <button class="btn-icon" @click="deleteEncadrant(encadrant.id)" title="Supprimer">
                <Trash2 :size="16" />
              </button>
            </div>
          </div>
        </div>

        <div class="encadrant-info">
          <h3 class="encadrant-name">{{ encadrant.nom }}</h3>
          <p class="encadrant-specialite">{{ encadrant.specialite }}</p>

          <div class="contact-info">
            <div class="contact-item">
              <Mail :size="16" />
              <span>{{ encadrant.email }}</span>
            </div>
          </div>

          <div class="charge-section">
            <div class="charge-header">
              <Users :size="16" />
              <span>Charge</span>
              <span class="charge-count">{{ encadrant.current }}/{{ encadrant.max }}</span>
            </div>
            <ProgressBar
              :current="encadrant.current"
              :max="encadrant.max"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Message si aucun encadrant -->
    <div v-if="!loading && !error && encadrants.length === 0" class="empty-message">
      <Users :size="48" />
      <h3>Aucun encadrant trouvé</h3>
      <p class="text-gray">Commencez par ajouter un encadrant</p>
    </div>

    <!-- Modal ajout/modification -->
    <div v-if="showAddModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>{{ isEditing ? 'Modifier' : 'Ajouter' }} un Encadrant</h2>
          <button class="btn-icon" @click="closeModal">
            <X :size="20" />
          </button>
        </div>

        <form @submit.prevent="submitForm" class="form-container">
          <!-- Nom complet -->
          <div class="form-group">
            <label>Nom complet <span class="required">*</span></label>
            <input 
              v-model="formData.nom" 
              type="text" 
              placeholder="Ex: Dr. Ahmed Benali"
              required 
            />
          </div>

          <!-- Email -->
          <div class="form-group">
            <label>Email <span class="required">*</span></label>
            <input 
              v-model="formData.email" 
              type="email" 
              placeholder="Ex: ahmed.benali@university.ma"
              required 
            />
          </div>

          <!-- Spécialité -->
          <div class="form-group">
            <label>Spécialité <span class="required">*</span></label>
            <input 
              v-model="formData.specialite" 
              type="text" 
              placeholder="Ex: Intelligence Artificielle"
              required 
            />
          </div>

          <!-- Téléphone -->
          <!-- <div class="form-group">
            <label>Téléphone</label>
            <input 
              v-model="formData.telephone" 
              type="tel" 
              placeholder="Ex: +212 6 12 34 56 78"
            />
          </div> -->

          <!-- Capacité maximale -->
          <div class="form-group">
            <label>Capacité maximale d'encadrement <span class="required">*</span></label>
            <input 
              v-model.number="formData.capaciteMax" 
              type="number" 
              min="1"
              max="20"
              placeholder="Ex: 5"
              required 
            />
            <small class="help-text">Nombre maximum d'étudiants que l'encadrant peut superviser</small>
          </div>

          <!-- Département (optionnel) -->
          <div class="form-group">
            <label>Département</label>
            <select v-model="formData.departement">
              <option value="">Sélectionner un département</option>
              <option value="Informatique">Informatique</option>
              <option value="Réseaux et Télécommunications">Réseaux et Télécommunications</option>
              <option value="Génie Logiciel">Génie Logiciel</option>
              <option value="Cybersécurité">Cybersécurité</option>
              <option value="Data Science">Data Science</option>
            </select>
          </div>

          <!-- Boutons d'action -->
          <div class="form-actions">
            <button type="button" class="btn btn-secondary" @click="closeModal">
              Annuler
            </button>
            <button type="submit" class="btn btn-primary" :disabled="submitting">
              <span v-if="submitting">Enregistrement...</span>
              <span v-else>{{ isEditing ? 'Modifier' : 'Ajouter' }}</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Mail, Phone, Users, Plus, Edit2, Trash2, X } from 'lucide-vue-next'
import ProgressBar from '../../components/ProgressBar.vue'
import api from '../../services/api'

// État du composant
const showAddModal = ref(false)
const isEditing = ref(false)
const submitting = ref(false)
const loading = ref(true)
const error = ref(null)

// Données
const encadrants = ref([])

// Formulaire
const formData = ref({
  nom: '',
  email: '',
  specialite: '',
  capaciteMax: 5,
  departement: ''
})

// Charger les encadrants
const loadEncadrants = async () => {
  try {
    loading.value = true
    error.value = null

    const response = await api.get('/encadrants')
    
    const encadrantsData = Array.isArray(response.data) 
      ? response.data 
      : response.data?.data || []

    if (!Array.isArray(encadrantsData)) {
      throw new Error('Réponse API invalide')
    }

    encadrants.value = encadrantsData.map(enc => {
      const current = enc.nombreEtudiantsActuels || 0
      const max = enc.capaciteMax || 5
      const disponible = current < max

      return {
        id: enc.id,
        nom: enc.nom,
        initials: getInitials(enc.nom),
        avatarColor: getRandomColor(),
        specialite: enc.specialite ?? 'Non spécifié',
        email: enc.email ?? '—',
        departement: enc.departement,
        current: current,
        max: max,
        status: disponible ? 'Disponible' : 'Complet',
        statusClass: disponible ? 'disponible' : 'complet'
      }
    })

    console.log('✅ Encadrants chargés:', encadrants.value)

  } catch (err) {
    console.error('❌ Erreur chargement encadrants:', err)
    error.value = 'Impossible de charger les encadrants'
  } finally {
    loading.value = false
  }
}

// Obtenir les initiales
const getInitials = (nom) => {
  if (!nom) return '?'
  const parts = nom.split(' ')
  if (parts.length >= 2) {
    return parts[0][0] + parts[1][0]
  }
  return nom[0]
}

// Obtenir une couleur aléatoire
const getRandomColor = () => {
  const colors = ['blue', 'purple', 'green', 'yellow', 'pink']
  return colors[Math.floor(Math.random() * colors.length)]
}

// Réinitialiser le formulaire
const resetForm = () => {
  formData.value = {
    nom: '',
    email: '',
    specialite: '',
    capaciteMax: 5,
    departement: ''
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
      nom: formData.value.nom,
      email: formData.value.email,
      specialite: formData.value.specialite,
      capaciteMax: formData.value.capaciteMax,
      departement: formData.value.departement || null
    }

    console.log('📤 Payload envoyé:', payload)

    if (isEditing.value) {
      // Modification
      await api.put(`/encadrants/${formData.value.id}`, payload)
      alert('Encadrant modifié avec succès !')
    } else {
      // Création
      await api.post('/encadrants', payload)
      alert('Encadrant ajouté avec succès !')
    }

    closeModal()
    await loadEncadrants()

  } catch (err) {
    console.error('❌ Erreur soumission:', err)
    alert('Erreur lors de l\'enregistrement: ' + (err.response?.data?.message || err.message))
  } finally {
    submitting.value = false
  }
}

// Modifier un encadrant
const editEncadrant = (encadrant) => {
  isEditing.value = true
  formData.value = {
    id: encadrant.id,
    nom: encadrant.nom,
    email: encadrant.email === '—' ? '' : encadrant.email,
    specialite: encadrant.specialite === 'Non spécifié' ? '' : encadrant.specialite,
    capaciteMax: encadrant.max,
    departement: encadrant.departement || ''
  }
  showAddModal.value = true
}

// Supprimer un encadrant
const deleteEncadrant = async (id) => {
  if (!confirm('Êtes-vous sûr de vouloir supprimer cet encadrant ?')) {
    return
  }

  try {
    await api.delete(`/encadrants/${id}`)
    encadrants.value = encadrants.value.filter(e => e.id !== id)
    alert('Encadrant supprimé avec succès !')
  } catch (error) {
    console.error('❌ Erreur suppression:', error)
    alert('Erreur lors de la suppression: ' + (error.response?.data?.message || error.message))
  }
}

// Charger les données au montage
onMounted(() => {
  loadEncadrants()
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

/* Grille */
.grid {
  display: grid;
  gap: 24px;
}

.grid-3 {
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
}

/* Carte encadrant */
.encadrant-card {
  padding: 24px;
  transition: all 0.2s ease;
}

.encadrant-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.encadrant-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.header-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.card-actions {
  display: flex;
  gap: 4px;
}

.avatar {
  width: 64px;
  height: 64px;
  border-radius: var(--radius-full);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  font-weight: 700;
  color: white;
}

.avatar.blue { background: #3B82F6; }
.avatar.purple { background: #8B5CF6; }
.avatar.green { background: #10B981; }
.avatar.yellow { background: #F59E0B; }
.avatar.pink { background: #EC4899; }

.encadrant-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.encadrant-name {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-dark);
  margin: 0;
}

.encadrant-specialite {
  font-size: 14px;
  color: var(--text-gray);
  margin: 0;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 12px 0;
  border-top: 1px solid #F0F0F0;
  border-bottom: 1px solid #F0F0F0;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--text-gray);
}

.contact-item svg {
  color: var(--text-light);
}

.charge-section {
  margin-top: 8px;
}

.charge-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
  margin-bottom: 8px;
}

.charge-count {
  margin-left: auto;
  font-size: 13px;
  color: var(--text-gray);
}

/* Badges */
.badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.badge.disponible {
  background: #D1FAE5;
  color: #065F46;
}

.badge.complet {
  background: #FEE2E2;
  color: #991B1B;
}

/* États */
.loading-message,
.error-message,
.empty-message {
  text-align: center;
  padding: 60px 20px;
  background: var(--bg-white);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.loading-message p {
  font-size: 18px;
  color: var(--text-gray);
}

.error-message p {
  font-size: 16px;
  color: #DC2626;
  margin-bottom: 16px;
}

.empty-message svg {
  margin-bottom: 16px;
  opacity: 0.5;
  color: var(--text-gray);
}

.empty-message h3 {
  margin-bottom: 8px;
  color: var(--text-dark);
}

.text-gray {
  color: var(--text-light);
  font-size: 14px;
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
  max-width: 600px;
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

.help-text {
  display: block;
  margin-top: 4px;
  font-size: 12px;
  color: var(--text-gray);
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

/* Boutons */
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
  background: var(--yellow-accent);
  color: black;
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
  padding: 6px;
  background: transparent;
  border: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: var(--text-gray);
}

.btn-icon:hover {
  background: rgba(0, 0, 0, 0.05);
  color: var(--text-dark);
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
  .page-header {
    flex-direction: column;
    gap: 16px;
  }

  .grid-3 {
    grid-template-columns: 1fr;
  }

  .modal-content {
    width: 95%;
    max-height: 95vh;
  }
}
</style>