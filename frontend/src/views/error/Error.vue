<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.back()
}
</script>

<template>
  <div class="text-center my-10">
    <h1 class="text-4xl font-bold mb-4">404 - 페이지를 찾을 수 없어요 😢</h1>
    <p class="text-lg mb-6">요청하신 페이지가 존재하지 않아요.</p>

    <div class="flex justify-center gap-4 mt-6">
      <router-link to="/" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
        홈 화면으로 이동
      </router-link>
      <button @click="goBack" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">
        뒤로가기
      </button>
    </div>
  </div>
</template>

<style scoped>

</style>