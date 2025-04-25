import { defineStore } from 'pinia'
import axios from 'axios'
import router from "@/router";

interface AuthState {
  token: string | null
  id: string | null
  role: string | null
  nickname: string | null
  userNo: number | null
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: localStorage.getItem('token'),
    id: localStorage.getItem('id'),
    role: localStorage.getItem('role'),
    nickname: localStorage.getItem('nickname'),
    userNo: localStorage.getItem('userNo') ? Number(localStorage.getItem('userNo')) : null
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ADMIN'
  },

  actions: {
    setAuth(auth: { isAuthenticated: boolean; id: string; role: string; nickname: string; userNo: number }) {
      this.token = auth.isAuthenticated ? localStorage.getItem('token') : null
      this.id = auth.id
      this.role = auth.role
      this.nickname = auth.nickname
      this.userNo = auth.userNo

      localStorage.setItem('id', auth.id)
      localStorage.setItem('role', auth.role)
      localStorage.setItem('nickname', auth.nickname)
      localStorage.setItem('userNo', String(auth.userNo))
    },

    login(token: string, id: string, role: string, nickname: string, userNo: number) {
      this.token = token
      this.id = id
      this.role = role
      this.nickname = nickname
      this.userNo = userNo

      localStorage.setItem('token', token)
      localStorage.setItem('id', id)
      localStorage.setItem('role', role)
      localStorage.setItem('nickname', nickname)
      localStorage.setItem('userNo', String(userNo))
    },

    async loginWithCredentials(id: string, password: string) {
      try {
        const response = await axios.post('/api/auth/login', { id, password }, {  withCredentials: true })
        const { token, id: userId, role, nickname, userNo } = response.data
        this.login(token, userId, role, nickname, userNo)
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
      this.userNo = null

      localStorage.removeItem('token')
      localStorage.removeItem('id')
      localStorage.removeItem('role')
      localStorage.removeItem('nickname')
      localStorage.removeItem('userNo')

      router.push('/')
    }
  }
}) 