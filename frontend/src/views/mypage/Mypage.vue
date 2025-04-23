<template>
  <div class="mypage-container" v-if="user">
    <h1 class="title">마이페이지~잉</h1>
    <div class="info-box">
      <p><strong>아이디:</strong> {{ user.id }}</p>
      <p><strong>닉네임:</strong> {{ user.nickname }}</p>
      <p><strong>이메일:</strong> {{ user.email }}</p>
      <p><strong>전화번호:</strong> {{ user.phone }}</p>
      <p><strong>가입일:</strong> {{ formatDate(user.created_at) }}</p>
    </div>
  </div>
  <div v-else>
    <p>사용자 정보를 불러오는 중이야~잉...</p>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const authStore = useAuthStore()

const user = ref<any>(null)

const fetchUserInfo = async () => {
  try {
    const userNo = authStore.userNo
    if (!userNo) {
      console.error('사용자가 존재하지 않음')
      return
    }
    const response = await axios.get(`http://localhost:8005/api/mypage/info?userNo=${userNo}`)
    user.value = response.data
  } catch (error) {
    console.error('사용자 정보 조회 실패:', error)
  }
}

const formatDate = (date: string) => {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = (d.getMonth() + 1).toString().padStart(2, '0') // 1월부터 시작이므로 +1
  const day = d.getDate().toString().padStart(2, '0')
  const hours = d.getHours().toString().padStart(2, '0')
  const minutes = d.getMinutes().toString().padStart(2, '0')

  return `${year}년 ${month}월 ${day}일 ${hours}:${minutes}`
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.mypage-container {
  max-width: 600px;
  margin: 2rem auto;
  padding: 2rem;
  border: 2px solid #ccc;
  border-radius: 10px;
  background-color: #f8f9fa;
}

.title {
  text-align: center;
  margin-bottom: 1rem;
}

.info-box p {
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}
</style>
