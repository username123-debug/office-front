<template>
  <div class="edit-container" v-if="form">
    <h2>スケジュール編集</h2>

    <label>タイトル：</label>
    <input v-model="form.title" type="text" />

    <label>開始日時：</label>
    <input v-model="form.startDateTime" type="datetime-local" />

    <label>終了日時：</label>
    <input v-model="form.endDateTime" type="datetime-local" />

    <label>内容：</label>
    <textarea v-model="form.body"></textarea>

    <label>参加者：</label>
    <select v-model="selectedUserId">
      <option disabled value="">参加者を選択</option>
      <option v-for="user in users" :key="user.id" :value="user.id">
        {{ user.name }}
      </option>
    </select>
    <button @click="addParticipant">追加</button>

    <table v-if="form.participants && form.participants.length > 0">
      <thead>
        <tr>
          <th>名前</th>
          <th>削除</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="id in form.participants" :key="id">
          <td>{{ getUserNameById(id) }}</td>
          <td><button @click="removeParticipant(id)">削除</button></td>
        </tr>
      </tbody>
    </table>

    <div class="button-row">
      <button @click="goBack">キャンセル</button>
      <button @click="submit">保存</button>
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

const form = ref(null)
const users = ref([])
const selectedUserId = ref('')

onMounted(async () => {
  const id = route.params.id

  // スケジュール取得
  const scheduleRes = await axios.get(`http://localhost:8080/schedules/${id}`)
  const data = scheduleRes.data

  form.value = {
    id: data.id,
    title: data.title,
    startDateTime: data.startDateTime?.slice(0, 16),
    endDateTime: data.endDateTime?.slice(0, 16),
    body: data.body,
    participants: Array.isArray(data.participants) ? data.participants : []
  }

  // ユーザー一覧取得
  const usersRes = await axios.get('http://localhost:8080/users/abstract')
  users.value = Object.entries(usersRes.data).map(([id, name]) => ({
    id: Number(id),
    name
  }))
})

function getUserNameById(id) {
  const user = users.value.find(u => u.id === id)
  return user ? user.name : ''
}

function addParticipant() {
  const id = Number(selectedUserId.value)
  if (!id || form.value.participants.includes(id)) return
  form.value.participants.push(id)
  selectedUserId.value = ''
}

function removeParticipant(id) {
  form.value.participants = form.value.participants.filter(pid => pid !== id)
}

function goBack() {
  router.push('/schedule')
}

async function submit() {
  await axios.put(`http://localhost:8080/schedules/${form.value.id}`, form.value)
  alert('保存しました')
  router.push('/schedule')
}
</script>

<style scoped>
.edit-container {
  max-width: 600px;
  margin: 30px auto;
  padding: 24px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background: #fefefe;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

input, textarea {
  padding: 8px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

textarea {
  resize: vertical;
  height: 100px;
}

.button-row {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 16px;
}

button {
  padding: 8px 16px;
  background-color: #3c82f6;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #2563eb;
}
</style>
