<script setup lang="ts">
import '../assets/style.css';

import { ref, onMounted, watch } from 'vue'
import { useAuthStore } from '../stores/auth.ts'
import { storeToRefs } from 'pinia'

const authStore = useAuthStore()
const { isAuthenticated, id, role, nickname } = storeToRefs(authStore)

// 로그인 상태 변경 감지
watch(() => authStore.isAuthenticated, (newValue) => {
  if (newValue) {
    id.value = localStorage.getItem('id') || ''
    role.value = localStorage.getItem('role') || ''
    nickname.value = localStorage.getItem('nickname') || ''
  } else {
    id.value = ''
    role.value = ''
    nickname.value = ''
  }
})

const handleLogout = () => {
  authStore.logout()
}

onMounted(() => {
  const token = localStorage.getItem('token')
  if (token) {
    authStore.setAuth({
      isAuthenticated: true,
      id: localStorage.getItem('id') || '',
      role: localStorage.getItem('role') || '',
      nickname: localStorage.getItem('nickname') || ''
    })
  }
})
</script>

<template>
  <!-- Header  -->
  <header class="bg-gradient-to-r from-blue-500 to-indigo-600 py-6 fixed top-0 w-full z-50">
    <div class="container mx-auto px-6 flex justify-between items-center">
      <!-- Logo -->
      <div class="flex items-center space-x-6">
        <a href="/" class="flex items-center space-x-4">
          <h1 class="text-4xl font-bold text-white tracking-wide">CTsoft 체험단</h1>
          <p class="text-lg text-white opacity-80">체험단에 참여하고 다양한 혜택을 누려보세요!</p>
        </a>
      </div>

      <!-- Search -->
      <div class="flex-grow flex justify-center">
        <div class="relative w-96">
          <input type="text" class="border-2 border-white rounded-lg px-4 py-2 w-full bg-white text-gray-700 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-400" placeholder="체험 단어를 검색하세요..." />
          <button class="absolute right-2 top-2 text-blue-600 hover:text-blue-800 transition duration-200">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="h-5 w-5">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 18a8 8 0 111.743-5.84l4.764 4.764a1 1 0 001.415-1.415l-4.764-4.764A7.93 7.93 0 0010 2a8 8 0 100 16z" />
            </svg>
          </button>
        </div>
      </div>

      <!-- Login/Register -->
      <div class="flex items-center space-x-6">
        <template v-if="isAuthenticated">
          <span class="text-white font-semibold">👤 {{ nickname }}</span>
          <button @click="handleLogout" class="text-white hover:text-blue-300 font-semibold transition duration-200">로그아웃</button>
        </template>
        <template v-else>
          <a href="/login" class="text-white hover:text-blue-300 font-semibold transition duration-200">로그인</a>
          <a href="/register" class="text-white hover:text-blue-300 font-semibold transition duration-200">회원가입</a>
        </template>
      </div>

    </div>
  </header>
  <nav class="bg-white border-t border-b py-3 shadow-sm mt-24">
    <div class="container mx-auto px-6 flex justify-between">
      <div class="flex space-x-6">
        <a href="#food" class="text-gray-700 hover:text-blue-600 font-medium">🍱 음식</a>
        <a href="#product" class="text-gray-700 hover:text-blue-600 font-medium">📦 제품</a>
        <a href="#beauty" class="text-gray-700 hover:text-blue-600 font-medium">💄 뷰티</a>
      </div>
      <div class="flex space-x-6">
        <a href="/review" class="text-gray-700 hover:text-blue-600 font-medium">📝 리뷰 게시판</a>
        <a href="#notices" class="text-gray-700 hover:text-blue-600 font-medium">📢 공지사항</a>
        <a href="#faq" class="text-gray-700 hover:text-blue-600 font-medium">❓ FAQ</a>
      </div>
    </div>
  </nav>
</template>

<style scoped>

</style>