<template>
  <div class="review-list-container">
    <h2 class="text-xl font-bold mb-4">내가 작성한 리뷰 목록</h2>

    <div v-if="reviews.length > 0" class="review-card" v-for="review in reviews" :key="review.id">
      <p><strong>제목:</strong> {{ review.title }}</p>
      <p><strong>내용:</strong> {{ review.content }}</p>
      <p><strong>작성일:</strong> {{ formatDate(review.createdAt) }}</p>
      <button class="mx-2" @click="deleteReview(review.postNo)">삭제</button>
      <button class="mx-2" @click="editReview(review.postNo)">수정</button>
    </div>

    <div v-else>
      <p>작성한 리뷰가 없습니다.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'
import router from "@/router";

const authStore = useAuthStore()
const reviews = ref<any[]>([])

// 리뷰 조회
const fetchReviews = async () => {
  try {
    const response = await axios.get(`http://localhost:8004/api/reviews/${authStore.userNo}`)
    console.log(console.log)
    reviews.value = response.data.map((item: any) => ({
      postNo: item.postNo,
      title: item.title,
      nickname: item.nickname,
      content: item.content,
      date: item.createdDate,
      image_url: item.image_url,
      post_url: item.post_url
    }))
    console.log(response.data)
  } catch (err) {
    console.error('리뷰 불러오기 실패:', err)
  }
}

// 리뷰 삭제
const deleteReview = async (postNo: number) => {
  if (!confirm('정말 삭제하시겠습니까?')) return

  try {
    await axios.delete(`http://localhost:8004/api/reviews/${postNo}`, {
      withCredentials: true
    })
    reviews.value = reviews.value.filter(r => r.postNo !== postNo)
    alert('삭제 완료!')
  } catch (err) {
    alert('삭제 실패 ㅠㅠ')
    console.error(err)
  }
}

const formatDate = (dateStr: string) => {
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${d.getMonth() + 1}-${d.getDate()}`
}

// 리뷰 수정
const editReview = (postNo: number) => {
  router.push({ name: 'ReviewEdit', params: { postNo } })
}

onMounted(fetchReviews)
</script>

<style scoped>
.review-list-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 1rem;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 0 5px rgba(0,0,0,0.1);
}
.review-card {
  padding: 1rem;
  border-bottom: 1px solid #ccc;
}
button {
  margin-top: 0.5rem;
  color: white;
  background: crimson;
  border: none;
  padding: 0.5rem 1rem;
  cursor: pointer;
  border-radius: 4px;
}
</style>
