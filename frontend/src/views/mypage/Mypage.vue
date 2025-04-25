<template>
  <div class="mypage-container" v-if="user">
    <h1 class="title">{{ user.id }}님 정보</h1>
    <div class="info-box" v-if="!isEditing">
      <p><strong>아이디:</strong> {{ user.id }}</p>
      <p><strong>닉네임:</strong> {{ user.nickname }}</p>
      <p><strong>이메일:</strong> {{ user.email }}</p>
      <p><strong>전화번호:</strong> {{ user.phone }}</p>
      <p><strong>가입일:</strong> {{ formatDate(user.created_at) }}</p>
      <button @click="editMode">수정하기</button>
      <button @click="deleteAccount">회원탈퇴</button>
    </div>

    <div class="edit-box" v-else>
      <label>닉네임: <input v-model="editUser.nickname" /></label>
      <label>이메일: <input v-model="editUser.email" /></label>
      <label>전화번호: <input v-model="editUser.phone" /></label>

      <h3>비밀번호 변경</h3>
      <label>새 비밀번호: <input type="password" v-model="editUser.newPassword" /></label>
      <label>새 비밀번호 확인: <input type="password" v-model="editUser.confirmPassword" /></label>

      <div class="btns">
        <button @click="updateUserInfo">저장</button>
        <button @click="cancelEdit">취소</button>
      </div>
    </div>
  </div>

  <div v-else>
    <p>사용자 정보를 불러오는 중</p>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import axios from 'axios'

const authStore = useAuthStore()
const router = useRouter()

const user = ref<any>(null)
const editUser = ref({
  nickname: '',
  phone: '',
  email: '',
  newPassword: '',
  confirmPassword: ''
})
const isEditing = ref(false)

//
const fetchUserInfo = async () => {
  try {
    const userNo = authStore.userNo
    if (!userNo) {
      console.error('사용자가 존재하지 않음잉~')
      return
    }
    const response = await axios.get(`http://localhost:8005/api/mypage/info?userNo=${userNo}`)
    user.value = response.data
  } catch (error) {
    console.error('사용자 정보 조회 실패잉~', error)
  }
}

const formatDate = (date: string) => {
  const d = new Date(date)
  return `${d.getFullYear()}년 ${d.getMonth() + 1}월 ${d.getDate()}일 ${d.getHours()}:${d.getMinutes()}`
}

const editMode = () => {
  isEditing.value = true
  editUser.value.nickname = user.value.nickname
  editUser.value.phone = user.value.phone
  editUser.value.email = user.value.email
}

const cancelEdit = () => {
  isEditing.value = false
  editUser.value.newPassword = ''
  editUser.value.confirmPassword = ''
}

// 회원 정보 수정
const updateUserInfo = async () => {
  if (editUser.value.newPassword && editUser.value.newPassword !== editUser.value.confirmPassword) {
    alert('새 비밀번호가 일치하지 않아요잉~')
    return
  }

  try {
    const userNo = authStore.userNo
    const payload: any = {
      userNo,
      nickname: editUser.value.nickname,
      phone: editUser.value.phone,
      email: editUser.value.email
    }

    if (editUser.value.newPassword) {
      payload.newPassword = editUser.value.newPassword
    }

    await axios.put('http://localhost:8005/api/mypage/info', payload)

    authStore.setAuth({
      id: authStore.id!,
      role: authStore.role!,
      nickname: editUser.value.nickname,
      userNo: authStore.userNo!,
      isAuthenticated: true
    })

    alert('정보가 성공적으로 수정되었어요잉~')
    isEditing.value = false
    fetchUserInfo()
  } catch (error) {
    console.error('정보 수정 실패잉~', error)
    alert('정보 수정에 실패했어요잉~')
  }
}

// 회원 탈퇴
const deleteAccount = async () => {
  if (!confirm('정말 탈퇴하시겠습니까? 😢')) return

  try {
    await axios.delete(`/api/mypage/delete/${authStore.userNo}`, {
      withCredentials: true
    })
    authStore.logout()
    alert('회원 탈퇴가 완료되었어요. 감사합니다 🫶')
    router.push('/')
  } catch (err) {
    console.error('탈퇴 실패:', err)
    alert('탈퇴 중 오류가 발생했어요 😓')
  }
}

onMounted(fetchUserInfo)
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

.edit-box {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}

.edit-box input {
  padding: 0.5rem;
  font-size: 1rem;
  border-radius: 5px;
  border: 1px solid #ccc;
  margin-top: 0.2rem;
}

.btns {
  margin-top: 1rem;
}

button {
  padding: 0.5rem 1rem;
  margin-right: 0.5rem;
}
</style>
