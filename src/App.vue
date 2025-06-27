<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter, RouterView } from 'vue-router'
import axios from 'axios'
import api from '@/plugin/axios'
import api from '@/plugin/axios';

// Vue Routerのフック
//Vue Routerのフック
const router = useRouter()
const route = useRoute()

// ログイン状態管理
const isLoggedIn = ref(true) // とりあえずtrueでスキップ
const keyword = ref('')

// 現在のページ判定
const isSchedulePage = computed(() =>
  route.path.startsWith('/schedule') ||
  route.path.startsWith('/overview') ||
  route.path.startsWith('/calendar')
)

const isLoginPage = computed(() => route.path === '/')

// 検索実行
const goSearch = () => {
  router.push({ path: '/schedule', query: { keyword: keyword.value } })
}

// ホームボタンクリック時の遷移処理
const goHome = () => {
  router.push({ name: 'Introduce' })
}


// ログイン成功時処理
const onLoginSuccess = () => {
  isLoggedIn.value = true
  router.push('/top')
}

// ブラウザバック検知
let firstLoad = true
onMounted(() => {
  window.onpopstate = async () => {
    if (!firstLoad) {
      alert('ブラウザバック検出、ログアウトします。')
      try {
        const res = await api.post('/logout', null, { withCredentials: true });
        console.log("res: ", res);
      } catch (e) {
        console.warn('ログアウト時エラー', e)
      }
      isLoggedIn.value = false
      router.replace('/')
    }
    firstLoad = false
  }
})

const logout = async () =>{
  const res = await api.post('/logout')
  console.log (res)
};

const logout = async () =>{
  const res = await api.post('/logout')
  console.log (res)
}

onUnmounted(() => {
  window.onpopstate = null
})
</script>

<template>
  <div id="app">
    <!-- ログインしていなければログインページを表示 -->
    <RouterView v-if="!isLoggedIn" v-slot="{ Component }">
      <component :is="Component" @login-success="onLoginSuccess" />
    </RouterView>

  <!-- ログイン後：レイアウト付き全体画面 -->
  <!-- <div v-else class="with-header"> -->
    <!-- ログインページ以外でヘッダーを表示 -->
    <!-- <header v-if="!isLoginPage" class="app-header">
      <nav class="main-nav">
        <router-link to="/schedule">スケジュール</router-link>
        <router-link to="/notice">お知らせ</router-link>
        <router-link to="/introduce">社員紹介</router-link>
      </nav>
      <div class="header-actions">
        <router-link to="/top"><button>ホーム</button></router-link>
        <router-link to="/"><button @click="logout">ログアウト</button></router-link>
      </div>
    </header> -->

    <!-- ヘッダーの下、全体レイアウト -->
    <!-- <div class="app-layout"> -->
      <!-- スケジュール関連ページでのみサイドバー表示 -->
      <!-- <aside v-if="isSchedulePage && !isLoginPage" class="sidebar">
        <h1>スケジュール管理</h1>
        <nav>
          <ul>
            <li><router-link to="/schedule">総覧</router-link></li>
            <li><router-link to="/calendar/1">マイカレンダー</router-link></li>
            <li><router-link to="/schedule/add">新規登録</router-link></li>
          </ul>
          <div class="search-box">
            <input v-model="keyword" placeholder="名前で検索" />
            <button @click="goSearch">検索</button>
          </div> -->
    <!-- ログインしていたら画面全体表示 -->
    <div v-else class="with-header">
      <!-- ログインページ以外ならヘッダー表示 -->
      <header v-if="!isLoginPage" class="app-header">
        <nav class="main-nav">
          <router-link to="/schedule">スケジュール</router-link>
          <router-link to="/notice">お知らせ</router-link>
          <router-link to="/introduce">社員紹介</router-link>
        </nav>
        <div class="header-actions">
          <router-link to="/top"><button>ホーム</button></router-link>
          <router-link to="/"><button @click=" logout ">ログアウト</button></router-link>
        </div>
      </header>

      <!-- ヘッダーの下にレイアウト -->
      <div class="app-layout">
        <!-- スケジュール関連のページのみサイドバー表示 -->
        <aside v-if="isSchedulePage && !isLoginPage" class="sidebar">
          <h1>スケジュール管理</h1>
          <nav>
            <ul>
              <li><router-link to="/schedule">総覧</router-link></li>
              <li><router-link to="/calendar/1">マイカレンダー</router-link></li>
              <li><router-link to="/schedule/add">新規登録</router-link></li>             
            </ul>
            <div class="search-box">
              <input v-model="keyword" placeholder="名前で検索" />
              <button @click="goSearch">検索</button>
            </div>
          </nav>
        </aside>

        <!-- メインコンテンツ -->
        <main class="main-content">
          <RouterView />
        </main>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 全体リセット */
html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
}

/* ヘッダー */
.app-header {
  position: fixed;
  top: 0; left: 0; right: 0;
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

/* ナビゲーションリンク */
.main-nav a {
  margin-right: 12px;
  padding: 8px 16px;
  border: 1px solid #2c3e50;
  border-radius: 4px;
  text-decoration: none;
  color: #2c3e50;
  font-weight: 500;
  background-color: white;
}

/* ヘッダー右のボタン */
.header-actions button {
  margin-left: 8px;
  padding: 6px 12px;
  border: none;
  background: #2c3e50;
  color: white;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
}

/* ヘッダー分の余白確保 */
.with-header {
  padding-top: 60px;
  height: 100vh;
  box-sizing: border-box;
}

/* レイアウト */
.app-layout {
  display: flex;
  height: calc(100vh - 60px);
}

/* サイドバー */
.sidebar {
  width: 220px;
  background: #f8fafc;
  padding: 20px;
  border-right: 1px solid #d0d7de;
  box-sizing: border-box;
  overflow-y: auto;
}
.sidebar h1 {
  font-size: 18px;
  margin-bottom: 20px;
  color: #2c3e50;
  border-bottom: 1px solid #ccc;
  padding-bottom: 8px;
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
  color: #2c3e50;
  font-weight: 500;
}

.sidebar nav a.router-link-exact-active {
  text-decoration: underline;
  font-weight: 700;
}

.sidebar nav li {
  margin-bottom: 20px;
}

/* 検索 */
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
  background-color: #2c3e50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* メインコンテンツ */
.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  box-sizing: border-box;
}

</style>
