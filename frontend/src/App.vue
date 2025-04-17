<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useAuthStore } from './stores/auth'
import { storeToRefs } from 'pinia'

import Header from './components/Header.vue'
import Footer from './components/Footer.vue'

const authStore = useAuthStore()
const { isAuthenticated, username, role } = storeToRefs(authStore)
import './assets/style.css';

// 로그인 상태 변경 감지
watch(() => authStore.isAuthenticated, (newValue) => {
  if (newValue) {
    username.value = localStorage.getItem('username') || ''
    role.value = localStorage.getItem('role') || ''
  } else {
    username.value = ''
    role.value = ''
  }
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  localStorage.removeItem('role')
  authStore.logout()
}

onMounted(() => {
  const token = localStorage.getItem('token')
  if (token) {
    authStore.setAuth({
      isAuthenticated: true,
      username: localStorage.getItem('username') || '',
      role: localStorage.getItem('role') || ''
    })
  }
})
</script>

<template>
  <Header />
  <div class="container main-content">
    <router-view></router-view>
  </div>
  <Footer />
</template>

<style>
html {
  scroll-behavior: smooth;
}
</style>