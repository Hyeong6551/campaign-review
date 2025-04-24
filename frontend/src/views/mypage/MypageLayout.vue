<template>
  <div class="mypage-wrapper">
    <aside class="sidebar">
      <router-link to="/mypage/info" class="menu-item" :class="{ active: isActive('/mypage/info') }">내 정보</router-link>
      <router-link to="/mypage/applications" class="menu-item" :class="{ active: isActive('/mypage/applications') }">내가 신청한 캠페인</router-link>
      <router-link to="/mypage/review" class="menu-item" :class="{ active: isActive('/mypage/reviews') }">내가 쓴 리뷰</router-link>
    </aside>
    <main class="content-area">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()

const isActive = (path: string) => route.path === path
</script>

<style scoped>
.mypage-wrapper {
  display: flex;
  max-width: 1000px;
  margin: 2rem auto;
  border: 1px solid #ddd;
  border-radius: 10px;
  overflow: hidden;
  background-color: #fff;
}

.sidebar {
  width: 200px;
  background-color: #f1f3f5;
  padding: 1rem;
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.menu-item {
  font-size: 1.1rem;
  color: #333;
  text-decoration: none;
  padding: 0.5rem;
  border-radius: 5px;
  transition: 0.2s;
}

.menu-item:hover {
  background-color: #e9ecef;
}

.menu-item.active {
  background-color: #dee2e6;
  font-weight: bold;
}

.content-area {
  flex-grow: 1;
  padding: 2rem;
}
</style>
