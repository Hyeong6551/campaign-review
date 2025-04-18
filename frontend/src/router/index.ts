import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import BoardView from '../views/BoardView.vue'
import AdminView from '../views/AdminView.vue'
import ReviewView from "@/views/review/ReviewView.vue";
import ReviewListView from "@/views/review/ReviewListView.vue";
import ReviewFormView from "@/views/review/ReviewFormView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/reviewlist',
      name: 'reviewList',
      component: ReviewListView
    },
    {
      path: '/review/form',
      name: 'reviewform',
      component: ReviewFormView
    },
    {
      path: '/review',
      name: 'review',
      component: ReviewView
    },
    {
      path: '/board',
      name: 'board',
      component: BoardView
    },
    {
      path: '/board/write',
      name: 'write',
      component: () => import('../views/WriteView.vue')
    },
    {
      path: '/board/:id',
      name: 'view',
      component: () => import('../views/ViewView.vue')
    },
    {
      path: '/board/:id/edit',
      name: 'edit',
      component: () => import('../views/EditView.vue')
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
      meta: { requiresAuth: true, requiresAdmin: true }
    },
  ]
})

// 네비게이션 가드
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.meta.requiresAdmin && role !== 'ADMIN') {
    next('/')
  } else {
    next()
  }
})

export default router 