<!-- <template>
  <div>
    <header class="app-header">
      <nav class="main-nav">
        <router-link to="/schedule">スケジュール</router-link>
        <router-link to="/notice">お知らせ</router-link>
        <router-link to="/introduce">社員紹介</router-link>
      </nav>
      <div class="header-actions">
        <router-link to="/top"><button>ホーム</button></router-link>
        <router-link to="/"><button>ログアウト</button></router-link>
      </div>
    </header>
    <div class="app-layout with-header">
      <aside v-if="isSchedulePage" class="sidebar">
        <h1>スケジュール管理</h1>
        <nav>
          <ul>
            <li><router-link to="/schedule">総覧</router-link></li>
            <li><router-link to="/calendar/000">マイカレンダー</router-link></li>
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

const goHome = () => {
  router.push({ name: 'Introduce' })
}

const logout = () => {
  router.push({ name: 'Login' })
}
</script>

<style scoped>
.app-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 60px;
  background: rgba(255,255,255,0.9);
  backdrop-filter: blur(6px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  z-index: 100;
}
.main-nav a {
  margin-right: 12px;
  padding: 8px 16px;
  border: 1px solid #666;
  border-radius: 4px;
  text-decoration: none;
  color: #333;
}
.main-nav a:hover {
  background: #f0f0f0;
}
.header-actions button {
  margin-left: 8px;
  padding: 6px 12px;
  border: none;
  background: #79BD9A;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}
.header-actions button:hover {
  background: #67aa85;
}
.with-header {
  margin-top: 60px;
}
.app-layout {
  display: flex;
  min-height: calc(100vh - 60px);
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
</style> -->


<script setup>
import { onMounted, onUnmounted, ref } from 'vue';
import { useRouter, RouterLink, RouterView } from 'vue-router';
import axios from 'axios';

const router = useRouter();

const isLoggedIn = ref(false); // 初期ログイン状態false

let firstLoad = true;

function onLoginSuccess(){
  isLoggedIn.value = true;
  // console.log('trueにしたぜ！！')
}

onMounted(() => {
  window.onpopstate = async () => {
    if (!firstLoad) {
      alert('ブラウザバックを検出しました。ログアウトします。');
      try {
        await axios.post('/logout', null, { withCredentials: true });
      } catch (e) {
        console.warn('ログアウト時エラー', e);
      }
      router.replace('/');
    }
    firstLoad = false;
  };
});

onUnmounted(() => {
  window.onpopstate = null;
});
</script>

<!-- <template>
  <Login @login-success="onLoginSuccess" />
  <header v-if="isLoggedIn">
    <div class="wrapper">
      <nav>
        <RouterLink to="/">ログアウト</RouterLink>
        <RouterLink to="/schedule">スケジュール</RouterLink>
        <RouterLink to="/notice">お知らせ</RouterLink>
        <RouterLink to="/bio">社員紹介</RouterLink>
      </nav>
    </div>
  </header>
  <main>
    <RouterView />
  </main>
</template> -->

<template>
  <header v-if="isLoggedIn">
    <div class="wrapper">
      <nav>
        <RouterLink to="/">ログアウト</RouterLink>
        <RouterLink to="/schedule">スケジュール</RouterLink>
        <RouterLink to="/notice">お知らせ</RouterLink>
        <RouterLink to="/bio">社員紹介</RouterLink>
      </nav>
    </div>
  </header>

  <!-- RouterViewのv-slotでComponentを受け取ってイベントを渡す -->
  <main>
    <RouterView v-slot="{ Component }">
      <component :is="Component" @login-success="onLoginSuccess" />
    </RouterView>
  </main>
</template>

<style>
  html,
  body,
  #app {
    height: 100%;
    width: 100%;
    margin: 0;
    padding: 0;
  }


  .app-layout {
    min-height: 100vh;
    width: 100%;
    display: flex;
    flex-direction: column;
  }

  header {
    /* もともとのスタイル */
    width: 100%;
    background-color: #fff;
    border-bottom: 1px solid #ccc;
    flex-shrink: 0;
    /* ヘッダーが縮まないように */
  }

  main {
    flex-grow: 1;
    width: 100%;
    padding: 1rem;
    box-sizing: border-box;
  }
</style>