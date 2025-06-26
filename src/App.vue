<script setup>
import { onMounted, onUnmounted, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

// ログイン状態を管理
// 初期ログイン状態false
// 初期値：trueにしてログイン状態をスキップ
const isLoggedIn = ref(true)

// 現在のページがスケジュール関連ページかを判定
const isSchedulePage = computed(() =>
  route.path.startsWith('/schedule') ||
  route.path.startsWith('/overview') ||
  route.path.startsWith('/calendar')
)

// 現在のページがログインページ（"/"）かを判定
const isLoginPage = computed(() => route.path === '/')

// サイドバー検索用キーワード
const keyword = ref('')

// 検索ボタンクリック時の遷移処理（名前で検索）
const goSearch = () => {
  router.push({ path: '/overview', query: { keyword: keyword.value } })
}

// ホームボタンクリック時の遷移処理
const goHome = () => {
  router.push({ name: 'Introduce' })
}

// ログアウトボタンクリック時の遷移処理
const logout = () => {
  router.push({ name: 'Login' })
}

// ブラウザバック検知用フラグ
let firstLoad = true

// ログイン成功時の処理
function onLoginSuccess() {
  isLoggedIn.value = true
  router.push('/top') // ← ログイン後にTopPageへリダイレクト
}

// 初回読み込み時のブラウザバック対策
onMounted(() => {
  window.onpopstate = async () => {
    if (!firstLoad) {
      alert('ブラウザバックを検出しました。ログアウトします。')
      try {
        await axios.post('/logout', null, { withCredentials: true })
      } catch (e) {
        console.warn('ログアウト時エラー', e)
      }
      isLoggedIn.value = false
      router.replace('/')
    }
    firstLoad = false
  }
})

// ページ破棄時にリスナー解除
onUnmounted(() => {
  window.onpopstate = null
})
</script>

<template>
  <!-- ログイン前：ログインページ（login componentを描画） -->
  <RouterView v-if="!isLoggedIn" v-slot="{ Component }">
    <component :is="Component" @login-success="onLoginSuccess" />
  </RouterView>

  <!-- ログイン後：レイアウト付き全体画面 -->
  <div v-else class="with-header">
    <!-- ログインページ以外でヘッダーを表示 -->
    <header v-if="!isLoginPage" class="app-header">
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

    <!-- ヘッダーの下、全体レイアウト -->
    <div class="app-layout">
      <!-- スケジュール関連ページでのみサイドバー表示 -->
      <aside v-if="isSchedulePage && !isLoginPage" class="sidebar">
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

      <!-- メインコンテンツエリア -->
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style scoped>
/* アプリ全体レイアウト初期化 */
html, body, #app {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
}

/* ヘッダー設定 */
.app-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 60px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(6px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

/* ナビゲーションリンク */
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

/* ヘッダー右のボタン */
.header-actions button {
  margin-left: 8px;
  padding: 6px 12px;
  border: none;
  background: #79bd9a;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}
.header-actions button:hover {
  background: #67aa85;
}

/* ヘッダー分の余白を確保 */
.with-header {
  padding-top: 60px;
  height: 100vh;
  box-sizing: border-box;
}

/* 全体レイアウト構成 */
.app-layout {
  display: flex;
  height: 100%;
}

/* サイドバーのデザイン */
.sidebar {
  width: 220px;
  background-color: #f5f5f5;
  padding: 20px;
  border-right: 1px solid #ccc;
  flex-shrink: 0;
  box-sizing: border-box;
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

/* 検索エリア */
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

/* メイン表示部 */
.main-content {
  flex: 1;
  padding: 20px;
  box-sizing: border-box;
  overflow: auto;
}
</style>
