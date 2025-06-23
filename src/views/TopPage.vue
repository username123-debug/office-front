<template>
  <div style="text-align: center; margin-top: 100px;">
    <h2>チーム機能一覧</h2>
    <div style="margin: 20px;">
      <router-link to="/overview">
        <button>📅 スケジュール管理</button>
      </router-link>
    </div>
    <div style="margin: 20px;">
      <router-link to="/notice">
        <button>📢 お知らせ</button>
      </router-link>
    </div>
    <div style="margin: 20px;">
      <router-link to="/members">
        <button>👥 社員紹介</button>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TopPage'
}
</script>
