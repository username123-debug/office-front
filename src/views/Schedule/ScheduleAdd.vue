<template>
  <div class="add-container">
    <h2 class="form-title">新規スケジュール登録</h2>

    <label>タイトル：</label>
    <input v-model="form.title" type="text" />

    <label>開始日時：</label>
    <input v-model="form.date_time_start" type="datetime-local" />

    <label>終了日時：</label>
    <input v-model="form.date_time_end" type="datetime-local" />

    <label>内容：</label>
    <textarea v-model="form.body"></textarea>
        <label>参加者：</label>
    <select v-model="selectedUserId">
      <option disabled value="">-- 参加者を選択 --</option>
      <option v-for="user in availableUsers" :key="user.id" :value="user.id">
        {{ user.name }}
      </option>
    </select>
    <button @click="addParticipant" :disabled="!selectedUserId">追加</button>

      <table v-if="form.participants.length > 0" class="participant-table">
        <thead>
          <tr>
            <th>名前</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="id in form.participants" :key="id">
            <td>{{ getUserName(id) }}</td>
            <td><button @click="removeParticipant(id)">削除</button></td>
          </tr>
        </tbody>
      </table>

    <label>作成者：</label>
    <input v-model="form.created_by_name" type="text" />

    <div class="button-row">
      <button @click="goBack">キャンセル</button>
      <button @click="submit">登録</button>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const form = reactive({
  title: '',
  date_time_start: '',
  date_time_end: '',
  body: '',
  participants: []
})

const allUsers = ref([])
const selectedUserId = ref('')

onMounted(async () => {
  const res = await axios.get('http://localhost:8080/users/abstract')
  allUsers.value = Object.entries(res.data).map(([id, name]) => ({
    id: Number(id),
    name
  }))
})

const availableUsers = computed(() =>
  allUsers.value.filter(user => !form.participants.includes(user.id))
)

function getUserName(id) {
  const user = allUsers.value.find(u => u.id === id)
  return user ? user.name : ''
}

function addParticipant() {
  const id = Number(selectedUserId.value)
  if (!form.participants.includes(id)) {
    form.participants.push(id)
    selectedUserId.value = ''
  }
}

function removeParticipant(id) {
  form.participants = form.participants.filter(pid => pid !== id)
}

function goBack() {
  router.push('/schedule')
}

async function submit() {
  try {
    await axios.post('http://localhost:8080/schedules', {
      title: form.title,
      startDateTime: form.date_time_start,
      endDateTime: form.date_time_end,
      body: form.body,
      participants: form.participants
    })
    alert('登録しました')
    router.push('/schedule')
  } catch (err) {
    alert('登録に失敗しました')
    console.error(err)
  }
}
</script>


<style scoped>
.add-container {
  max-width: 650px;
  margin: 40px auto;
  padding: 32px;
  border: 1px solid #ddd;
  border-radius: 12px;
  background: #ffffff;
  display: flex;
  flex-direction: column;
  gap: 16px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.04);
}

.form-title {
  font-size: 1.5rem;
  font-weight: 600;
  text-align: center;
  color: #2c3e50;
  margin-bottom: 8px;
}

label {
  font-weight: 500;
  color: #2c3e50;
  margin-top: 0.5rem;
}

input,
textarea,
select {
  width: 100%;
  padding: 10px 12px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-sizing: border-box;
  background-color: #fafafa;
  transition: border 0.2s;
}

input:focus,
textarea:focus,
select:focus {
  border-color: #1f6feb;
  outline: none;
}

textarea {
  resize: vertical;
  height: 6rem;
}

button {
  padding: 10px 20px;
  background-color: #2c3e50;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
}

button:hover {
  background-color: #1a1a1a;
}

.button-row {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.participant-table {
  width: 100%;
  margin-top: 12px;
  border-collapse: collapse;
  font-size: 0.95rem;
}

.participant-table th,
.participant-table td {
  padding: 8px 12px;
  border: 1px solid #ccc;
  text-align: left;
}

.participant-table button {
  padding: 6px 12px;
  background-color: #e53935;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
}

.participant-table button:hover {
  background-color: #c62828;
}
</style>
