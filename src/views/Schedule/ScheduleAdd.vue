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

.form-title {
  font-size: 1.5rem;
  font-weight: bold;
  text-align: center;
  white-space: nowrap;
}

label {
  font-weight: bold;
  margin-top: 0.5rem;
}

input,
textarea {
  width: 100%;
  padding: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 0.5rem;
  box-sizing: border-box;
}

textarea {
  resize: vertical;
  height: 6rem;
}

.button-row {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

button {
  padding: 0.5rem 1.5rem;
  background-color: #3c82f6;
  color: white;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
  font-size: 1rem;
}

button:hover {
  background-color: #2563eb;
}
</style>
