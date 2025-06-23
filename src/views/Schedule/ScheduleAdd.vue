<template>
  <div class="form-page">
    <div class="add-container">
      <h2 class="form-title">➕ 新規スケジュール登録</h2>

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
        <button @click="submit">登録</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        title: '',
        date_time_start: '',
        date_time_end: '',
        body: '',
        created_by_name: ''
      }
    }
  },
  methods: {
    goBack() {
      this.$router.push('/')
    },
    submit() {
      alert('登録しました（※ 実際には保存されません）')
      this.$router.push('/')
    }
  }
}
</script>

<style>
body {
  margin: 0;
  font-family: sans-serif;
}

.form-page {
  display: flex;
  justify-content: center;
  padding: 2rem 1rem;
}

.add-container {
  width: 100%;
  max-width: 36rem;
  padding: 2rem;
  border: 1px solid #ccc;
  border-radius: 1rem;
  background: #fefefe;
  display: flex;
  flex-direction: column;
  gap: 1rem;
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
