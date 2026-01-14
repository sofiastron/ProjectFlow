<template>
  <div class="etudiants-page fade-in">
    <div class="page-header">
      <div>
        <h1>Gestion des Étudiants</h1>
        <p class="subtitle">Liste et informations des étudiants en PFE</p>
      </div>
      <button class="btn btn-primary" @click="openAddModal">
        <Plus :size="18" />
        Ajouter Étudiant
      </button>
    </div>

    <!-- Filtres -->
    <div class="filters mb-3">
      <select v-model="filiereFilter">
        <option value="">Toutes les filières</option>
        <option value="Informatique">Informatique</option>
        <option value="Réseaux">Réseaux</option>
        <option value="Génie Logiciel">Génie Logiciel</option>
      </select>

      <select v-model="statutFilter">
        <option value="">Tous les statuts</option>
        <option value="en-cours">En cours</option>
        <option value="non-affecte">Non affecté</option>
      </select>
    </div>

    <!-- État de chargement -->
    <div v-if="loading" class="loading-state">
      Chargement des étudiants...
    </div>

    <!-- État d'erreur -->
    <div v-if="error" class="error-state">
      {{ error }}
      <button @click="loadEtudiants">Réessayer</button>
    </div>

    <!-- Tableau -->
    <div v-if="!loading && !error" class="card">
      <table>
        <thead>
          <tr>
            <th>Étudiant</th>
            <th>Filière</th>
            <th>Sujet PFE</th>
            <th>Encadrant</th>
            <th>Contact</th>
            <th>Statut</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="etudiant in filteredEtudiants" :key="etudiant.id">
            <td>
              <div class="etudiant-cell">
                <div class="avatar avatar-sm" :class="etudiant.avatarColor">
                  {{ etudiant.initials }}
                </div>
                <div>
                  <div class="etudiant-nom">{{ etudiant.nom }}</div>
                  <div class="etudiant-email">{{ etudiant.email }}</div>
                </div>
              </div>
            </td>
            <td>
              <span class="badge filiere">{{ etudiant.filiere }}</span>
            </td>
            <td>{{ etudiant.sujet }}</td>
            <td>{{ etudiant.encadrant || 'Non affecté' }}</td>
            <td>
              <div class="contact-icons">
                <a :href="`mailto:${etudiant.email}`" class="btn-icon" title="Envoyer un email">
                  <Mail :size="16" />
                </a>
              </div>
            </td>
            <td>
              <span class="badge" :class="etudiant.statutClass">
                {{ etudiant.statut }}
              </span>
            </td>
            <td>
              <div class="actions">
                <button class="btn-icon" @click="editEtudiant(etudiant)" title="Modifier">
                  <Edit2 :size="18" />
                </button>
                <button class="btn-icon" @click="deleteEtudiant(etudiant.id)" title="Supprimer">
                  <Trash2 :size="18" />
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Message si aucun étudiant -->
      <div v-if="filteredEtudiants.length === 0" class="empty-state">
        <Users :size="48" />
        <h3>Aucun étudiant trouvé</h3>
        <p>{{ etudiants.length === 0 ? 'Commencez par ajouter un étudiant' : 'Aucun résultat pour ces filtres' }}</p>
      </div>
    </div>

    <!-- Modal ajout/modification -->
    <div v-if="showAddModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>{{ isEditing ? 'Modifier' : 'Ajouter' }} un Étudiant</h2>
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
              placeholder="Ex: Ahmed Benali"
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

          <!-- Filière -->
          <div class="form-group">
            <label>Filière <span class="required">*</span></label>
            <select v-model="formData.filiere" required>
              <option value="">Sélectionner une filière</option>
              <option value="Informatique">Informatique</option>
              <option value="Réseaux">Réseaux</option>
              <option value="Génie Logiciel">Génie Logiciel</option>
              <option value="Cybersécurité">Cybersécurité</option>
              <option value="Data Science">Data Science</option>
            </select>
          </div>

          <!-- Téléphone (optionnel) -->
          <!-- <div class="form-group">
            <label>Téléphone</label>
            <input 
              v-model="formData.telephone" 
              type="tel" 
              placeholder="Ex: +212 6 12 34 56 78"
            />
          </div> -->

          <!-- Encadrant (optionnel) -->
          <!-- <div class="form-group">
            <label>Encadrant</label>
            <select v-model="formData.professeurId">
              <option value="">Non affecté</option>
              <option v-for="prof in professeurs" :key="prof.id" :value="prof.id">
                {{ prof.nom }}
              </option>
            </select>
          </div> -->

          <!-- Sujet PFE (optionnel) -->
          <!-- <div class="form-group">
            <label>Sujet du PFE</label>
            <textarea 
              v-model="formData.sujet" 
              rows="3"
              placeholder="Description du projet..."
            ></textarea>
          </div> -->

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
import { ref, computed, onMounted } from 'vue'
import { Mail, Phone, Plus, Edit2, Trash2, X, Users } from 'lucide-vue-next'
import api from '../../services/api'

// État du composant
const showAddModal = ref(false)
const isEditing = ref(false)
const submitting = ref(false)
const loading = ref(true)
const error = ref(null)

// Filtres
const filiereFilter = ref('')
const statutFilter = ref('')

// Données
const etudiants = ref([])
const professeurs = ref([])

// Formulaire
const formData = ref({
  nom: '',
  email: '',
  filiere: '',
  professeurId: '',
  sujet: ''
})

// Charger les étudiants
const loadEtudiants = async () => {
  try {
    loading.value = true
    error.value = null

    const response = await api.get('/etudiants')
    
    const etudiantsData = Array.isArray(response.data) 
      ? response.data 
      : response.data?.data || []

    if (!Array.isArray(etudiantsData)) {
      throw new Error('Réponse API invalide')
    }

    etudiants.value = etudiantsData.map(e => {
      return {
        id: e.id,
        nom: e.nom,
        initials: getInitials(e.nom),
        avatarColor: getRandomColor(),
        email: e.email,
        filiere: e.filiere ?? 'Non spécifié',
        sujet: e.sujet || '—',
        professeurId: e.professeurId || null, // si tu veux garder l'id
        encadrant: e.encadrantNom || null,
        statut: e.affecte ? 'En cours' : 'Non affecté',
        statutClass: e.affecte ? 'en-cours' : 'non-affecte'
      }
    })

    

    console.log('✅ Étudiants chargés:', etudiants.value)

  } catch (err) {
    console.error('❌ Erreur chargement étudiants:', err)
    error.value = 'Impossible de charger les étudiants'
  } finally {
    loading.value = false
  }
}

// Charger les professeurs
const loadProfesseurs = async () => {
  try {
    const response = await api.get('/encadrants')
    professeurs.value = Array.isArray(response.data) 
      ? response.data 
      : response.data?.data || []
    
    console.log('✅ Professeurs chargés')
  } catch (err) {
    console.error('❌ Erreur chargement professeurs:', err)
  }
}

// Obtenir les initiales
const getInitials = (nom) => {
  if (!nom) return '?'
  const parts = nom.split(' ')
  if (parts.length >= 2) return parts[0][0] + parts[1][0]
  return nom[0]
}

// Obtenir une couleur aléatoire
const getRandomColor = () => {
  const colors = ['blue', 'purple', 'green', 'yellow', 'pink']
  return colors[Math.floor(Math.random() * colors.length)]
}

// Filtrer les étudiants
const filteredEtudiants = computed(() => {
  let result = etudiants.value

  if (filiereFilter.value) {
    result = result.filter(e => e.filiere === filiereFilter.value)
  }

  if (statutFilter.value) {
    result = result.filter(e => e.statutClass === statutFilter.value)
  }

  return result
})

// Réinitialiser le formulaire
const resetForm = () => {
  formData.value = {
    nom: '',
    email: '',
    filiere: '',
    professeurId: '',
    sujet: ''
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
      filiere: formData.value.filiere,
    }

    console.log('📤 Payload envoyé:', payload)

    if (isEditing.value) {
      // Modification
      await api.put(`/etudiants/${formData.value.id}`, payload)
      alert('Étudiant modifié avec succès !')
    } else {
      // Création
      await api.post('/etudiants', payload)
      alert('Étudiant ajouté avec succès !')
    }

    closeModal()
    await loadEtudiants()

  } catch (err) {
    console.error('❌ Erreur soumission:', err)
    alert('Erreur lors de l\'enregistrement: ' + (err.response?.data?.message || err.message))
  } finally {
    submitting.value = false
  }
}

// Modifier un étudiant
const editEtudiant = (etudiant) => {
  isEditing.value = true
  formData.value = {
    id: etudiant.id,
    nom: etudiant.nom,
    email: etudiant.email,
    filiere: etudiant.filiere,
    professeurId: etudiant.professeurId || '',
    sujet: etudiant.sujet === '—' ? '' : etudiant.sujet
  }
  showAddModal.value = true
}

// Supprimer un étudiant
const deleteEtudiant = async (id) => {
  if (!confirm('Êtes-vous sûr de vouloir supprimer cet étudiant ?')) {
    return
  }

  try {
    await api.delete(`/etudiants/${id}`)
    etudiants.value = etudiants.value.filter(e => e.id !== id)
    alert('Étudiant supprimé avec succès !')
  } catch (error) {
    console.error('❌ Erreur suppression:', error)
    alert('Erreur lors de la suppression')
  }
}

// Charger les données au montage
onMounted(() => {
  loadEtudiants()
  loadProfesseurs()
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

/* Filtres */
.filters {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.filters select {
  padding: 10px 16px;
  border: 1px solid #E0E0E0;
  border-radius: var(--radius-md);
  font-size: 14px;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filters select:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(101, 67, 58, 0.1);
}

/* États */
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

/* Tableau */
.card {
  overflow: hidden;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: #F9FAFB;
  border-bottom: 2px solid #E5E7EB;
}

th {
  padding: 12px 16px;
  text-align: left;
  font-size: 12px;
  font-weight: 600;
  color: #6B7280;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

td {
  padding: 16px;
  border-bottom: 1px solid #F0F0F0;
}

tr:hover {
  background: #FAFAFA;
}

/* Cellule étudiant */
.etudiant-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  color: white;
}

.avatar.blue { background: #3B82F6; }
.avatar.purple { background: #8B5CF6; }
.avatar.green { background: #10B981; }
.avatar.yellow { background: #F59E0B; }
.avatar.pink { background: #EC4899; }

.etudiant-nom {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
}

.etudiant-email {
  font-size: 12px;
  color: var(--text-gray);
}

/* Badges */
.badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.badge.filiere {
  background: #E0E7FF;
  color: #4F46E5;
}

.badge.en-cours {
  background: #D1FAE5;
  color: #065F46;
}

.badge.non-affecte {
  background: #FEF3C7;
  color: #92400E;
}

/* Contact icons */
.contact-icons {
  display: flex;
  gap: 8px;
}

/* Actions */
.actions {
  display: flex;
  gap: 8px;
}

/* Empty state */
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
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #E0E0E0;
  border-radius: var(--radius-md);
  font-size: 14px;
  font-family: inherit;
  transition: all 0.3s ease;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(101, 67, 58, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
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
  padding: 8px;
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
  background: #F5F5F5;
  color: var(--text-dark);
}

/* Utilitaires */
.mb-3 {
  margin-bottom: 24px;
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

  .filters {
    flex-direction: column;
  }

  table {
    font-size: 12px;
  }

  th, td {
    padding: 8px;
  }

  .modal-content {
    width: 95%;
    max-height: 95vh;
  }
}
</style>