import { defineStore } from 'pinia'
import axios from 'axios'

interface AuthState {
  token: string | null
  id: string | null
  role: string | null
  nickname: string | null
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: localStorage.getItem('token'),
    id: localStorage.getItem('id'),
    role: localStorage.getItem('role'),
    nickname: localStorage.getItem('nickname')
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ADMIN'
  },

  actions: {
    setAuth(auth: { isAuthenticated: boolean; id: string; role: string; nickname: string }) {
      this.token = auth.isAuthenticated ? localStorage.getItem('token') : null
      this.id = auth.id
      this.role = auth.role
      this.nickname = auth.nickname // 추가
    },

    login(token: string, id: string, role: string, nickname: string) {
      this.token = token
      this.id = id
      this.role = role
      this.nickname = nickname

      localStorage.setItem('token', token)
      localStorage.setItem('id', id)
      localStorage.setItem('role', role)
      localStorage.setItem('nickname', nickname)
    },

    async loginWithCredentials(id: string, password: string) {
      try {
        const response = await axios.post('/api/auth/login', { id, password })
        console.log(response.data)
        const { token, id: userId, nickname, role } = response.data
        this.login(token, userId, nickname, role)
        return true
      } catch (error) {
        console.error('Login failed:', error)
        return false
      }
    },

    async register(id: string, password: string, email: string) {
      try {
        await axios.post('/api/auth/register', { id, password, email })
        return true
      } catch (error) {
        console.error('Registration failed:', error)
        return false
      }
    },

    logout() {
      this.token = null
      this.id = null
      this.role = null
      this.nickname = null

      localStorage.removeItem('token')
      localStorage.removeItem('id')
      localStorage.removeItem('role')
      localStorage.removeItem('nickname')
    }
  }
}) 