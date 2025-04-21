<template>
  <div class="container my-5">
    <h2 class="title">리뷰 작성</h2>
    <form @submit.prevent="submitReview" class="review-form">
      <div class="form-group">
        <label for="title">제목</label>
        <input v-model="form.title" id="title" maxlength="40" placeholder="최대 40자 까지 입력 가능합니다" type="text" required />
      </div>
      <div class="form-group">
        <label for="title">리뷰 내용</label>
        <textarea v-model="form.content" id="content" maxlength="500" placeholder="최대 500자 까지 입력 가능합니다" required />
      </div>
      <div class="form-group">
        <label for="title">리뷰 URL</label>
        <input v-model="form.post_url" id="blogURL" type="text" placeholder="ex) https://blog.naver.com/tpgud333/223833623056" required />
      </div>
      <div class="form-group">
        <label for="file">이미지 업로드</label>
        <input @change="onFileChange" id="file" type="file" accept="image/*" required />
      </div>
      <button type="submit" class="btn">등록</button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const file = ref<File | null>(null)
const router = useRouter()

const form = reactive({
  title : '',
  content :'',
  post_url : '',
})

const onFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    file.value = target.files[0]
  }
}

const submitReview = async () => {
  if (!file.value) return alert('이미지를 선택해주세요')

  const formData = new FormData()
  formData.append(
      'review',
      new Blob([JSON.stringify({
        title: form.title,
        content: form.content,
        post_url: form.post_url
        })
      ],
      {
        type: 'application/json',
      })
  )
  formData.append('image', file.value)

  try {
    await axios.post('/api/reviews', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    alert('리뷰가 등록되었습니다!')
    router.push('/review')
  } catch (error) {
    console.error('등록 실패:', error)
    alert('등록 실패')
  }
}
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}
.title {
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
}
.review-form {
  display: flex;
  flex-direction: column;
}
.form-group {
  margin-bottom: 15px;
}
label {
  font-weight: bold;
  display: block;
  margin-bottom: 5px;
}
input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 6px;
}
textarea {
  width: 100%;
  height: 150px;
  resize: none;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 6px;
}
.btn {
  background-color: #007bff;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: 0.2s;
}
.btn:hover {
  background-color: #0056b3;
}
</style>