<script setup lang="ts">
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
import './assets/style.css';
</script>

<template>
  <Header />
  <div class="container main-content">
    <router-view></router-view>
  </div>
  <Footer />
</template>

<style>
html {
  scroll-behavior: smooth;
}
</style>