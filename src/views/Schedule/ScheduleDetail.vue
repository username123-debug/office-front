<template>
  <div class="detail-container" v-if="schedule">
    <h2>スケジュール詳細</h2>
    <p><strong>タイトル：</strong>{{ schedule.title }}</p>
    <p><strong>時間：</strong>{{ formatTime(schedule.startDateTime) }} ～ {{ formatTime(schedule.endDateTime) }}</p>
    <p><strong>作成者：</strong>{{ getUserNameById(schedule.createdUserId) }}</p>
    <p><strong>内容：</strong>{{ schedule.body }}</p>

    <p><strong>参加者：</strong></p>
    <ul v-if="schedule.participants?.length">
      <li v-for="id in schedule.participants" :key="id">
        {{ getUserNameById(id) }}
      </li>
    </ul>
    <p v-else>なし</p>

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
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const schedule = ref(null)
const users = ref([])

onMounted(async () => {
  const id = route.params.id
  try {
    const res = await axios.get(`http://localhost:8080/schedules/${id}`, { withCredentials: true })
    schedule.value = res.data
  } catch (err) {
    console.error('スケジュール取得失敗:', err)
  }

  try {
    const usersRes = await axios.get('http://localhost:8080/users/abstract')
    users.value = Object.entries(usersRes.data).map(([id, name]) => ({
      id: Number(id),
      name
    }))
  } catch (err) {
    console.error('ユーザー一覧取得失敗:', err)
  }
})

function getUserNameById(id) {
  const user = users.value.find(u => u.id === id)
  return user ? user.name : '名前不明'
}

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
  const from = route.query.from
  if (from) {
    router.push(from)
  } else {
    router.push('/schedule')
  }
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
  max-width: 700px;
  margin: 40px auto;
  padding: 32px;
  border: 1px solid #ddd;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.04);
  font-size: 1rem;
  color: #2c3e50;
  line-height: 1.6;
}

.detail-container h2 {
  font-size: 1.4rem;
  font-weight: 600;
  margin-bottom: 20px;
  color: #2c3e50;
}

.detail-container p {
  margin-bottom: 12px;
}

.detail-container strong {
  font-weight: 600;
  margin-right: 6px;
}

.detail-container ul {
  padding-left: 1.2rem;
  margin-top: 4px;
  margin-bottom: 16px;
}

.detail-container ul li {
  margin: 4px 0;
}

.button-row {
  margin-top: 28px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

button {
  padding: 10px 20px;
  border: none;
  background-color: #2c3e50;
  color: white;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
}

button:hover {
  background-color: #1a1a1a;
}
</style>
