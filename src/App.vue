<template>
  <div>
    <header>
      <h1>📅 スケジュール管理</h1>
      <nav>
        <router-link to="/overview">総覧</router-link> |
        <router-link to="/calendar/self"> マイカレンダー</router-link> |
        <router-link to="/schedule/add">新規登録</router-link>
      </nav>
    </header>
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<style>
header {
  padding: 1rem;
}

nav {
  margin-top: 0.5rem;
}

nav a {
  margin: 0 0.5rem;
  text-decoration: none;
  color: #10b981;
  font-weight: bold;
}

nav a.router-link-exact-active {
  text-decoration: underline;
}
</style>
