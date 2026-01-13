import { defineStore } from 'pinia'
import api from '@/services/api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null
  }),
  
  getters: {
    isAuthenticated: (state) => !!state.token,
    currentUser: (state) => state.user
  },
  
  actions: {
    async login(email, password) {
      try {
        const response = await api.login(email, password)
        this.token = response.data.token
        this.user = response.data.user
        localStorage.setItem('token', this.token)
        return true
      } catch (error) {
        console.error('Erreur de connexion:', error)
        return false
      }
    },
    
    logout() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
    }
  }
})