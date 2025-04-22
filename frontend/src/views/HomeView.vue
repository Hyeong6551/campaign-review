<script setup lang="ts">
import { useAuthStore } from '../stores/auth'
import { storeToRefs } from 'pinia'

const authStore = useAuthStore()
const { id, role } = storeToRefs(authStore)
</script>

<template>
  <section class="bg-gray-50 py-10">
    <div class="container mx-auto px-6">
      <!-- 체험 목록과 리뷰 게시판을 포함한 두 개의 섹션 -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
        <!-- 체험 목록 섹션 -->
        <div class="bg-white rounded-lg shadow-lg p-6">
          <h2 class="text-3xl font-semibold text-center mb-6 text-blue-600">체험할 수 있는 카테고리</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 음식 카테고리 -->
            <div class="bg-blue-50 p-4 rounded-lg shadow-md hover:bg-blue-100 transition duration-300">
              <h3 class="font-bold text-xl text-center mb-4">🍱 음식 체험단</h3>
              <ul>
                <li class="text-sm">수제 도시락 세트</li>
                <li class="text-sm">프리미엄 한우 스테이크</li>
              </ul>
              <a href="#food" class="block text-center text-blue-600 hover:text-blue-800 mt-4">더 보기</a>
            </div>
            <!-- 제품 카테고리 -->
            <div class="bg-blue-50 p-4 rounded-lg shadow-md hover:bg-blue-100 transition duration-300">
              <h3 class="font-bold text-xl text-center mb-4">📦 제품 체험단</h3>
              <ul>
                <li class="text-sm">무선 이어폰</li>
                <li class="text-sm">스마트 체중계</li>
              </ul>
              <a href="#product" class="block text-center text-blue-600 hover:text-blue-800 mt-4">더 보기</a>
            </div>
            <!-- 뷰티 카테고리 -->
            <div class="bg-blue-50 p-4 rounded-lg shadow-md hover:bg-blue-100 transition duration-300">
              <h3 class="font-bold text-xl text-center mb-4">💄 뷰티 체험단</h3>
              <ul>
                <li class="text-sm">수분 진정 크림</li>
                <li class="text-sm">봄 네일 컬렉션</li>
              </ul>
              <a href="#beauty" class="block text-center text-blue-600 hover:text-blue-800 mt-4">더 보기</a>
            </div>
          </div>
        </div>

        <!-- 리뷰 게시판 섹션 -->
        <div class="bg-white rounded-lg shadow-lg p-6">
          <h2 class="text-3xl font-semibold text-center mb-6 text-blue-600">리뷰 게시판</h2>
          <div class="space-y-6">
            <!-- 리뷰 카드 -->
            <div class="border-b pb-4">
              <h3 class="font-semibold text-xl text-gray-800">👍 훌륭한 서비스!</h3>
              <p class="text-sm text-gray-600 mt-2">체험단 참여로 정말 좋은 경험을 했어요. 서비스도 좋고, 체험 품목도 만족스럽습니다!</p>
              <p class="text-sm text-gray-500 mt-2">작성자: 김영희 | 2025.04.16</p>
            </div>
            <div class="border-b pb-4">
              <h3 class="font-semibold text-xl text-gray-800">💖 너무 좋았어요!</h3>
              <p class="text-sm text-gray-600 mt-2">상품이 정말 좋아요! 다음에도 또 참여하고 싶습니다. 추천해요!</p>
              <p class="text-sm text-gray-500 mt-2">작성자: 이민수 | 2025.04.15</p>
            </div>
            <div>
              <h3 class="font-semibold text-xl text-gray-800">🌟 멋진 경험!</h3>
              <p class="text-sm text-gray-600 mt-2">여러분이 참여할 수 있는 기회를 주셔서 감사합니다. 품질도 만족스러웠어요!</p>
              <p class="text-sm text-gray-500 mt-2">작성자: 박준호 | 2025.04.14</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.card {
  border: none;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

.card-title {
  color: #333;
}

.btn-primary {
  width: 100%;
}
</style> 