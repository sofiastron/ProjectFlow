import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

const api = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' }
})

api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  },
  error => Promise.reject(error)
)

api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default {
  login(email, password) {
    return api.post('/auth/login', { email, password })
  },
  register(registerDTO) {
    return api.post('/auth/register', registerDTO)
  },
  getDashboardStats() { return api.get('/professeur/dashboard') },
  getProfesseurProfile() { return api.get('/professeur/profile') },
  updateProfesseurProfile(data) { return api.put('/professeur/profile', data) },
  uploadProfilePhoto(formData) {
    return api.post('/professeur/profile/photo', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
  },
  getEtudiantsAssignes() {
    return api.get('/professeur/etudiants')
  }
}
