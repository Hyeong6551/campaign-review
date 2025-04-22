<script setup lang="ts">
import {reactive, ref} from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const confirmPassword = ref('')
const error = ref('')
const codeSent = ref(false)
const authCode = ref('')
const message = ref('')
const user = reactive({
  id : '',
  password : '',
  email : '',
  phone : '',
  nickname : ''
})

const handleRegister = async () => {
  if (user.password !== confirmPassword.value) {
    error.value = '비밀번호가 일치하지 않습니다.'
    return
  }

  try {
    await axios.post('/api/auth/register', {
      id: user.id,
      password: user.password,
      email: user.email,
      phone: user.phone,
      nickname: user.nickname
    })
    router.push('/login')
  } catch (err: any) {
    error.value = err.response?.data?.message || '회원가입 중 오류가 발생했습니다.'
  }
}

const sendCode = async () => {
  try {
    await axios.post('/api/sms/send', { phone: user.phone })
    codeSent.value = true
    message.value = '인증번호가 전송되었습니다.'
  } catch (err) {
    message.value = '인증번호 전송 실패'
  }
}

const verifyCode = async () => {
  try {
    const res = await axios.post('/api/sms/verify', {
      phone: user.phone,
      code: authCode.value
    })
    if (res.data.success) {
      message.value = '인증 성공!'
    } else {
      message.value = '인증 실패. 코드가 틀립니다.'
    }
  } catch (err) {
    message.value = '인증 확인 중 오류'
  }
}
</script>

<template>
  <div class="register-container">
    <h1 class="register-title">회원가입</h1>

    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="id">아이디</label>
        <input type="text" id="id" v-model="user.id" />
      </div>

      <div class="form-group">
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model="user.password" />
      </div>

      <div class="form-group">
        <label for="confirmPassword">비밀번호 확인</label>
        <input type="password" id="confirmPassword" v-model="confirmPassword" />
      </div>

      <div class="form-group">
        <label for="email">이메일</label>
        <input type="email" id="email" v-model="user.email" />
      </div>

      <div class="form-group">
        <label for="nickname">닉네임</label>
        <input type="text" id="nickname" v-model="user.nickname" />
      </div>

      <div class="form-group">
        <label for="phone">전화번호</label>
        <div class="phone-auth">
          <input
              type="text"
              id="phone"
              v-model="user.phone"
              placeholder="휴대폰 번호 입력 (- 없이)"
          />
          <button type="button" class="btn-sms" @click="sendCode">인증번호 받기</button>
        </div>

        <div class="phone-auth-verify" v-if="codeSent">
          <input v-model="authCode" placeholder="인증번호 입력" />
          <button type="button" class="btn-sms" @click="verifyCode">인증 확인</button>
        </div>

        <p v-if="message">{{ message }}</p>
      </div>

      <button type="submit" class="btn-register">회원가입</button>
    </form>

    <div class="login-link">
      이미 계정이 있으신가요?
      <router-link to="/login">로그인</router-link>
    </div>
  </div>
</template>

<style scoped>
.register-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 4rem;
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.register-title {
  font-size: 2.5rem;
  font-weight: bold;
  color: var(--primary-color);
  margin-bottom: 2rem;
}

form {
  width: 100%;
  max-width: 500px;
}

.form-group {
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: var(--text-color);
}

.form-group input {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 0.75rem 1rem;
  transition: 0.3s;
}

.form-group input:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 0.2rem rgba(74, 144, 226, 0.25);
  outline: none;
}

.phone-auth {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.phone-auth-verify {
  margin-top: 0.5rem;
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.btn-sms {
  padding: 0.5rem 1rem;
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s ease;
}

.btn-sms:hover {
  background-color: #357abd;
}

.btn-register {
  width: 100%;
  padding: 0.75rem;
  font-size: 1.1rem;
  font-weight: 500;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 8px;
  transition: 0.3s;
}

.btn-register:hover {
  background-color: #357abd;
  transform: translateY(-2px);
}

.login-link {
  margin-top: 1.5rem;
  text-align: center;
}

.login-link a {
  color: var(--primary-color);
  font-weight: 500;
  text-decoration: none;
}

.login-link a:hover {
  color: #357abd;
}

.alert-danger {
  background-color: #fff5f5;
  border: 1px solid #feb2b2;
  color: #c53030;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 1rem;
}
</style>
