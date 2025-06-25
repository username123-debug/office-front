<template>
  <div class="detail-container" v-if="schedule">
    <h2>スケジュール詳細</h2>
    <p><strong>タイトル：</strong>{{ schedule.title }}</p>
    <p><strong>時間：</strong>{{ formatTime(schedule.date_time_start) }} ～ {{ formatTime(schedule.date_time_end) }}</p>
    <p><strong>作成者：</strong>{{ schedule.created_by_name }}</p>
    <p><strong>内容：</strong>{{ schedule.body }}</p>
    <div class="button-row">
      <button @click="goBack">戻る</button>
      <button @click="goEdit">編集</button>
      <button @click="confirmDelete">削除</button>
    </div>
  </div>
  <div v-else>
    <p>スケジュールが見つかりませんでした。</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { mockSchedules } from '../../mock/schedules'

const route = useRoute()
const router = useRouter()

const schedule = ref(null)

onMounted(() => {
  const id = route.params.id
  schedule.value = mockSchedules.find(item => item.id.toString() === id)
})

function formatTime(datetimeStr) {
  const date = new Date(datetimeStr)
  const yyyy = date.getFullYear()
  const mm = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')
  const hh = String(date.getHours()).padStart(2, '0')
  const mi = String(date.getMinutes()).padStart(2, '0')
  return `${yyyy}/${mm}/${dd} ${hh}:${mi}`
}

function goBack() {
  router.push('/schedule')
}

function goEdit() {
  router.push(`/schedule/${schedule.value.id}/edit`)
}

function confirmDelete() {
  if (confirm('本当に削除しますか？')) {
    router.push('/overview')
  }
}
</script>

<style scoped>
.detail-container {
  max-width: 960px;
  margin: 0 auto;
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
