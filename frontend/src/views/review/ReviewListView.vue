<script setup lang="ts">
import { ref } from 'vue'

interface Review {
  id: number
  title: string
  content: string
  imageUrl: string
  date: string
}

const image = "https://dummyimage.com/200x200/000/fff"
const today = new Date().toISOString().split('T')[0] // 'YYYY-MM-DD' 형태 날짜

const reviews = ref<Review[]>([
  {
    id: 1,
    title: '사장님이 친절하시고 아트가 예뻐요',
    content: '카페 분위기도 좋고 라떼 아트가 인상적이었어요!',
    imageUrl: image,
    date: today
  },
  {
    id: 2,
    title: '고기가 신선하고 공간도 넓어요!',
    content: '아이들과 함께 가기에도 좋은 고깃집이었어요.',
    imageUrl: image,
    date: today
  },
  {
    id: 3,
    title: '수제 간식 맛있어요! 꼬르륵댕댕~',
    content: '반려견을 위한 간식이 너무 맛있었나봐요. 재방문 예정!',
    imageUrl: image,
    date: today
  },
  {
    id: 4,
    title: '대형 풀빌라 프라이빗 스테이 추천!',
    content: '조용하고 힐링하기 딱 좋은 숙소였어요.',
    imageUrl: image,
    date: today
  },
  {
    id: 5,
    title: '리뷰용 샘플 이미지입니다',
    content: '디자인 테스트용 샘플 데이터입니다.',
    imageUrl: image,
    date: today
  },
  {
    id: 6,
    title: '다시 방문하고 싶은 체험단 후기!',
    content: '정성껏 체험하게 해주셔서 감사합니다!',
    imageUrl: image,
    date: today
  },
  {
    id: 7,
    title: '너무 예쁜 인테리어에 반했어요',
    content: '사진 찍기 좋은 곳! 감성 충만한 공간이었어요.',
    imageUrl: image,
    date: today
  }
])

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
    <div class="review-grid">
      <div class="review-card" v-for="review in reviews" :key="review.id">
        <a @click.prevent="showModal(review)">
        <p class="review-id">&nbsp No. {{ review.id }}</p>
          <img :src="review.imageUrl" alt="리뷰 이미지" />
          <div class="review-text">
            <p class="review-title">{{ review.title }}</p>
          </div>
          <div class="d-flex justify-content-between align-items-center">
            <p class="mb-0 small text-muted">닉네임</p>
            <p class="mb-0 small text-muted">{{review.date}}</p>
          </div>
        </a>
      </div>
    </div>
    <router-link to="/review/form" class="btn btn-primary my-5 d-inline-flex">리뷰 작성</router-link>
  </div>

<!-- Modal -->
  <div v-if="isModalVisible" class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <img :src="modalReview?.imageUrl" alt="확대 이미지" class="modal-image" />
      <div class="modal-text">
        <p class="modal-id">No. {{ modalReview?.id }}</p>
        <p class="modal-title">{{ modalReview?.title }}</p>
        <p class="modal-content">{{ modalReview?.content }}</p>
        <hr>
        <div class="d-flex justify-content-between align-items-center">
          <p class="mb-0 small text-muted">이름입니다1</p>
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