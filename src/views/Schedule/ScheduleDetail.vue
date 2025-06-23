<template>
  <div class="content">
    <div class="detail-container" v-if="schedule">
      <h2>📝 スケジュール詳細</h2>
      <p><strong>タイトル：</strong>{{ schedule.title }}</p>
      <p><strong>時間：</strong>{{ formatTime(schedule.date_time_start) }} ～ {{ formatTime(schedule.date_time_end) }}</p>
      <p><strong>作成者：</strong>{{ schedule.created_by_name }}</p>
      <p><strong>内容：</strong>{{ schedule.body }}</p>

      <div class="button-row">
        <button @click="goBack">← 戻る</button>
        <button @click="goEdit">✏️ 編集</button>
        <button @click="confirmDelete">🗑️ 削除</button>
      </div>
    </div>

    <div v-else>
      <p>スケジュールが見つかりませんでした。</p>
    </div>
  </div>
</template>


<script>
import { mockSchedules } from '../../mock/schedules'

export default {
  data() {
    return {
      schedule: null
    }
  },
  mounted() {
    const id = this.$route.params.id
    this.schedule = mockSchedules.find(item => item.id.toString() === id)
  },
  methods: {
    formatTime(datetimeStr) {
      const date = new Date(datetimeStr)
      const yyyy = date.getFullYear()
      const mm = String(date.getMonth() + 1).padStart(2, '0')
      const dd = String(date.getDate()).padStart(2, '0')
      const hh = String(date.getHours()).padStart(2, '0')
      const mi = String(date.getMinutes()).padStart(2, '0')
      return `${yyyy}/${mm}/${dd} ${hh}:${mi}`
    },
    goBack() {
      this.$router.push('/')
    },
    goEdit() {
      this.$router.push(`/schedule/${this.schedule.id}/edit`)
    },
    confirmDelete() {
      if (confirm('本当に削除しますか？')) {
        this.$router.push('/')
      }
    }
  }
}
</script>

<style>
.content {
  max-width: 960px;
  margin-left: 24px;
  margin-right: auto;
  padding-bottom: 40px;
}

.detail-container {
  padding: 24px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background: #fefefe;
}

.button-row {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

button {
  padding: 8px 16px;
  border: none;
  background-color: #3c82f6;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #2563eb;
}


</style>
