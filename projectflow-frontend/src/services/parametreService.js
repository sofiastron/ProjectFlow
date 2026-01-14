import api from './api'

export default {
  // Obtenir tous les paramètres
  getAllParametres() {
    return api.get('/parametres')
  },

  // Paramètres par type
  getParametresByType(type) {
    return api.get(`/parametres/type/${type}`)
  },

  // Obtenir un paramètre par clé
  getParametreByCle(cle) {
    return api.get(`/parametres/${cle}`)
  },

  // Créer ou mettre à jour un paramètre
  saveParametre(parametre) {
    return api.post('/parametres', parametre)
  },

  // Mettre à jour la valeur d'un paramètre
  updateValeur(cle, valeur) {
    return api.put(`/parametres/${cle}`, null, {
      params: { valeur }
    })
  },

  // Supprimer un paramètre
  supprimerParametre(id) {
    return api.delete(`/parametres/${id}`)
  },

  // Initialiser les paramètres par défaut
  initialiserParametres() {
    return api.post('/parametres/initialiser')
  }
}