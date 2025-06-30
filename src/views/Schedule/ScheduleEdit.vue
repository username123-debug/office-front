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
    <option disabled value="">-- 参加者を選択 --</option>
    <option v-for="user in users" :key="user.id" :value="user.id">
      {{ user.name }}
    </option>
  </select>
  <button class="add-button" @click="addParticipant">追加</button>

  <table class="participant-table" v-if="form.participants && form.participants.length > 0">
    <thead>
      <tr>
        <th>名前</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="id in form.participants" :key="id">
        <td>{{ getUserNameById(id) }}</td>
        <td><button class="remove-button" @click="removeParticipant(id)">削除</button></td>
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
  try {
    await axios.put(`http://localhost:8080/schedules/${form.value.id}`, form.value)
    alert('保存しました')
    router.push('/schedule')
  } catch (error) {
    console.error('保存エラー:', error)
    alert('保存に失敗しました')
  }
}

</script>

<style scoped>
.edit-container {
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

h2 {
  font-size: 1.5rem;
  font-weight: 600;
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

.add-button {
  width: 100%;
  margin-top: 10px;
  padding: 12px;
  background-color: #2c3e50;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
}

.add-button:hover {
  background-color: #1a1a1a;
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

.remove-button {
  padding: 6px 12px;
  background-color: #e53935;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
}

.remove-button:hover {
  background-color: #c62828;
}

.button-row {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
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
</style>