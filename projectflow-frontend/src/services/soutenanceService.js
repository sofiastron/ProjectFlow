import api from './api'

export default {
  // Obtenir toutes les soutenances
  getAllSoutenances() {
    return api.get('/soutenances')
  },

  // Soutenances à venir
  getSoutenancesAVenir() {
    return api.get('/soutenances/a-venir')
  },

  // Soutenances par période
  getSoutenancesByPeriode(dateDebut, dateFin) {
    return api.get('/soutenances/periode', {
      params: { dateDebut, dateFin }
    })
  },

  // Calendrier des soutenances
  getCalendrierSoutenances(dateDebut, dateFin) {
    return api.get('/soutenances/calendrier', {
      params: { dateDebut, dateFin }
    })
  },

  // Soutenances par salle
  getSoutenancesBySalle(salle, date) {
    return api.get('/soutenances/salle', {
      params: { salle, date }
    })
  },

  // Planifier une soutenance
  planifierSoutenance(data) {
    return api.post('/soutenances', data)
  },

  // Modifier une soutenance
  modifierSoutenance(id, data) {
    return api.put(`/soutenances/${id}`, data)
  },

  // Supprimer une soutenance
  supprimerSoutenance(id) {
    return api.delete(`/soutenances/${id}`)
  },

  // Attribuer une note
  attribuerNote(id, note) {
    return api.put(`/soutenances/${id}/note`, null, {
      params: { note }
    })
  },

  // Vérifier disponibilité jury
  isJuryDisponible(professeurId, date, heure) {
    return api.get('/soutenances/jury/disponible', {
      params: { professeurId, date, heure }
    })
  }
}