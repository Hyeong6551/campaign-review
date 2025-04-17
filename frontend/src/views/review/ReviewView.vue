<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'

// 1. 블로그 검색 결과 아이템 타입 정의
interface BlogItem {
  title: string
  link: string
  description: string
  bloggername: string
  bloggerlink: string
  postdate: string
}

// 2. 반응형 상태 정의
const blogListNaver = ref<BlogItem[]>([])
const searchQuery = ref('') // 검색어 상태 추가

// 3. 블로그 검색 함수
const getBlogList = async (query: string) => {
  const URL = `https://openapi.naver.com/v1/search/blog.json?query=${encodeURIComponent(query)}`
  const clientId = 'NvmeYvs8dwYiKjj3KI4p'
  const clientSecret = 'NEUEosAj7R'

  try {
    const response = await axios.get<{ items: BlogItem[] }>(URL, {
      headers: {
        Accept: 'application/json',
        'X-Naver-Client-Id': clientId,
        'X-Naver-Client-Secret': clientSecret,
      },
    })

    blogListNaver.value = response.data.items
    console.log(blogListNaver.value)
  } catch (error) {
    console.error('블로그 검색 실패:', error)
  }
}

// 4. 검색어를 입력하고 검색 버튼을 클릭할 때 실행
const searchBlogs = () => {
  if (searchQuery.value.trim() === '') {
    alert('검색어를 입력해주세요!')
    return
  }
  getBlogList(searchQuery.value)
}

// 5. 초기 데이터 불러오기 (초기값: 빈 검색어로 로드하지 않음)
onMounted(() => {
  // getBlogList('초기 검색어')  // 원하는 초기 검색어가 있다면 사용
})
</script>

<template>
  <div class="p-4">
    <h2 class="text-xl font-bold mb-4">네이버 블로그 검색</h2>

    <!-- 검색어 입력 필드 -->
    <input
        v-model="searchQuery"
        type="text"
        placeholder="검색어를 입력하세요"
        class="border p-2 w-full mb-4"
    />

    <!-- 검색 버튼 -->
    <button
        @click="searchBlogs"
        class="bg-blue-500 text-white px-4 py-2 rounded"
    >
      검색
    </button>

    <!-- 블로그 목록 출력 -->
    <ul class="mt-4">
      <li v-for="(blog, index) in blogListNaver" :key="index" class="mb-6 border-b pb-4">
        <a :href="blog.link" target="_blank" class="text-blue-600 hover:underline">
          <div v-html="blog.title" class="font-semibold mb-1"></div>
        </a>
        <p v-html="blog.description" class="text-gray-700 text-sm mb-1"></p>
        <p class="text-xs text-gray-500">
          작성자: {{ blog.bloggername }} | 날짜: {{ blog.postdate }}
        </p>
      </li>
    </ul>
  </div>
</template>