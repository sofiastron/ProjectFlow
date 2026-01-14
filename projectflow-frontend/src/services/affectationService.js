import api from './api'

export default {
  // Obtenir toutes les affectations
  getAllAffectations() {
    return api.get('/affectations')
  },

  // Affectations par filière
  getAffectationsByFiliere(filiere) {
    return api.get(`/affectations/filiere/${filiere}`)
  },

  // Affectation manuelle
  affecterManuellement(etudiantId, encadrantId) {
    return api.post('/affectations/manuelle', {
      etudiantId,
      encadrantId
    })
  },

  // Affectation automatique
  affecterAutomatiquement(params) {
    return api.post('/affectations/automatique', params)
  },

  // Obtenir la charge des encadrants
  getChargeEncadrants() {
    return api.get('/affectations/encadrants/charge')
  },

  // Encadrants par filière
  getEncadrantsByFiliere(filiere) {
    return api.get(`/affectations/encadrants/filiere/${filiere}`)
  },

  // Désaffecter un étudiant
  desaffecter(etudiantId) {
    return api.delete(`/affectations/${etudiantId}`)
  }
}