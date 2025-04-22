<script setup lang="ts">
import {onMounted, ref, computed, watch} from 'vue'
import axios from 'axios'
import router from "@/router";
import {useRoute} from "vue-router";

interface Review {
  id: number
  nickname: string
  title: string
  content: string
  image_url: string
  post_url: string
  date: string
}

const route = useRoute()
const reviews = ref<Review[]>([])
const currentPage = ref(1)
const maxReview = 18

// 리뷰 불러오기
const fetchReviews = async () => {
  try {
    const res = await axios.get('/api/reviews/')
    reviews.value = res.data.map((item: any) => ({
      id: item.post_no,
      nickname: item.nickname,
      title: item.title,
      content: item.content,
      image_url: item.image_url,
      post_url: item.post_url,
      date: item.createdDate.split('T')[0]
    }))
  } catch (err) {
    console.error('리뷰 불러오기 실패:', err)
    alert('리뷰를 불러오는 데 실패했어요.')
  }
}

// 페이지네이션을 위한 computed
const paginatedReviews = computed(() => {
  const start = (currentPage.value - 1) * maxReview
  return reviews.value.slice(start, start + maxReview)
})

const totalPages = computed(() => {
  return Math.ceil(reviews.value.length / maxReview)
})

const setPage = (page: number) => {
  if (page < 1 || page > totalPages.value) {
    router.replace('/error')
    return
  }

  currentPage.value = page
  const routePath = page === 1 ? '/review' : `/review/${page}`
  if (route.fullPath !== routePath) {
    router.push(routePath)
  }
}

const getPageButtonClass = (page: number) => {
  return [
    'px-3 py-1 rounded border',
    page === currentPage.value
        ? 'bg-blue-500 text-white border-blue-500'
        : 'bg-white text-gray-700 border-gray-300'
  ]
}

onMounted(async () => {
  await fetchReviews()
  const pageParam = Number(route.params.page || 1)
  setPage(pageParam)
})

watch(
    () => route.params.page,
    (newPage) => {
      const pageNum = Number(newPage || 1)
      setPage(pageNum)
    }
)

// review modal
const isModalVisible = ref(false)
const modalReview = ref<Review | null>(null)

const showModal = (review: Review) => {
  modalReview.value = review
  isModalVisible.value = true
}

const closeModal = () => {
  isModalVisible.value = false
}
</script>


<template>
  <div class="container my-5">
    <h2 class="title">체험단 리뷰 게시판</h2>
    <!-- 리뷰-->
    <div class="review-grid">
      <div class="review-card" v-for="review in paginatedReviews" :key="review.id">
        <a @click.prevent="showModal(review)">
          <p class="review-id">&nbsp No. {{ review.id }}</p>
          <img :src="`http://localhost:8004${review.image_url}?t=${Date.now()}`" alt="리뷰 이미지" />
          <div class="review-text">
            <p class="review-title">{{ review.title }}</p>
          </div>
          <div class="d-flex justify-content-between align-items-center">
            <p class="mb-0 small text-muted">{{ review.nickname }}</p>
            <p class="mb-0 small text-muted">{{ review.date }}</p>
          </div>
        </a>
      </div>
    </div>

    <!-- 페이징 -->
    <div class="container my-5 flex flex-col items-center">
      <div class="pagination">
        <button @click="setPage(currentPage - 1)" :disabled="currentPage === 1">Previous</button>
        <button
            v-for="page in totalPages"
            :key="page"
            :class="getPageButtonClass(page)"
            @click="setPage(page)"
        >
          {{ page }}
        </button>
        <button @click="setPage(currentPage + 1)" :disabled="currentPage === totalPages">Next</button>
      </div>
    </div>

    <div>
      <router-link to="/review/form" class="btn btn-primary my-5 d-inline-flex">리뷰 작성</router-link>
    </div>
  </div>

  <!-- Modal -->
  <div v-if="isModalVisible" class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <img :src="`http://localhost:8004${modalReview?.image_url}?t=${Date.now()}`" class="modal-image" alt="리뷰 이미지" />
      <div class="modal-text">
        <p class="modal-id">No. {{ modalReview?.id }}</p>
        <p class="modal-title">{{ modalReview?.title }}</p>
        <p class="modal-content">{{ modalReview?.content }}</p>
        <hr>
        <a :href="modalReview?.post_url" class="modal-content">{{ modalReview?.post_url }}</a>
        <div class="d-flex justify-content-between align-items-center">
          <p class="mb-0 small text-muted">{{ modalReview?.nickname }}</p>
          <p class="modal-date mb-0 small text-muted">{{ modalReview?.date }}</p>
        </div>
      </div>

      <button class="close-btn" @click="closeModal">X</button>
    </div>
  </div>
</template>


<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.title {
  font-size: 28px;
  text-align: center;
  margin-bottom: 24px;
}

.review-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 16px;
}

.review-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: 0.3s;
}

.review-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.review-card img {
  width: 100%;
  height: 120px;
  object-fit: cover;
}

.review-text {
  padding: 12px;
}

.review-id {
  font-size: 12px;
  color: #aaa;
  margin-bottom: 4px;
}

.review-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 16px; /* padding을 줄여서 크기 줄이기 */
  border-radius: 8px;
  position: relative;
  max-width: 400px; /* 최대 너비를 줄여서 크기 줄이기 */
  max-height: 800px; /* 최대 높이를 줄여서 크기 줄이기 */
  overflow: hidden;
}

.modal-image {
  width: 100%;
  height: auto;
  object-fit: contain;
}

.modal-text {
  padding-top: 12px;
}

.modal-id {
  font-size: 16px;
  color: #aaa;
}

.modal-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-top: 8px;
}

.modal-title {
  font-size: 14px;
  color: #333;
  margin-top: 8px;
}

.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 16px;
  cursor: pointer;
}
</style>