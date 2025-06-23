<template>
  <div class="top-page">
    <h2>チーム機能一覧</h2>
    
    <div class="button-container">
      <router-link to="/overview">
        <button>📅 スケジュール管理</button>
      </router-link>

      <router-link to="/notice">
        <button>📢 お知らせ</button>
      </router-link>

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

<style scoped>
.top-page {
  text-align: center;
  margin-top: 80px;
}

h2 {
  font-size: 1.8rem;
  color: #007acc;
  border-bottom: 2px solid #007acc;
  display: inline-block;
  padding-bottom: 5px;
}

.button-container {
  margin-top: 40px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
}

button {
  padding: 12px 24px;
  font-size: 1rem;
  background-color: #0ea5e9;
  color: white;
  border: none;
  border-radius: 12px;
  width: 220px;
  text-align: center;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: background-color 0.2s;
}

button:hover {
  background-color: #0284c7;
}
</style>
