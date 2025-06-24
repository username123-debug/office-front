<template>
  <div class="edit-container" v-if="form">
    <h2>スケジュール編集</h2>

    <label>タイトル：</label>
    <input v-model="form.title" type="text" />

    <label>開始日時：</label>
    <input v-model="form.date_time_start" type="datetime-local" />

    <label>終了日時：</label>
    <input v-model="form.date_time_end" type="datetime-local" />

    <label>内容：</label>
    <textarea v-model="form.body"></textarea>

    <label>作成者：</label>
    <input v-model="form.created_by_name" type="text" />

    <div class="button-row">
      <button @click="goBack">キャンセル</button>
      <button @click="submit">保存</button>
    </div>
  </div>

  <div v-else>
    <p>スケジュールが見つかりませんでした。</p>
  </div>
</template>

<script>
import { mockSchedules } from '../../mock/schedules'

export default {
  data() {
    return {
      form: null
    }
  },
  mounted() {
    const id = this.$route.params.id
    const found = mockSchedules.find(item => item.id.toString() === id)
    if (found) {
      this.form = { ...found }
    }
  },
  methods: {
    goBack() {
      this.$router.push('/schedule')
    },
    submit() {
      alert('保存しました（※ 実際には更新されません）')
      this.$router.push('/schedule')
    }
  }
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
