<template>
  <div class="app-layout">
    <aside v-if="isSchedulePage" class="sidebar">
      <h1>📅 スケジュール管理</h1>
      <nav>
        <ul>
          <li><router-link to="/overview">総覧</router-link></li>
          <li><router-link to="/calendar/self">マイカレンダー</router-link></li>
          <li><router-link to="/schedule/add">新規登録</router-link></li>
        </ul>
        <div class="search-box">
          <input v-model="keyword" placeholder="名前で検索" />
          <button @click="goSearch">検索</button>
        </div>
      </nav>
    </aside>
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const isSchedulePage = computed(() =>
  route.path.startsWith('/schedule') ||
  route.path.startsWith('/overview') ||
  route.path.startsWith('/calendar')
)

const keyword = ref('')

const goSearch = () => {
  router.push({ path: '/overview', query: { keyword: keyword.value } })
}
</script>

<style>
.app-layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 220px;
  background-color: #f5f5f5;
  padding: 20px;
  border-right: 1px solid #ccc;
}

.sidebar h1 {
  font-size: 18px;
  margin-bottom: 20px;
}

.sidebar nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar nav li {
  margin-bottom: 12px;
}

.sidebar nav a {
  text-decoration: none;
  color: #10b981;
  font-weight: bold;
}

.sidebar nav a.router-link-exact-active {
  text-decoration: underline;
}

.search-box {
  margin-top: 24px;
}

.search-box input {
  width: 100%;
  padding: 6px;
  margin-bottom: 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.search-box button {
  width: 100%;
  padding: 6px;
  background-color: #3c82f6;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-box button:hover {
  background-color: #2563eb;
}

.main-content {
  flex: 1;
  padding: 20px;
}
</style>
