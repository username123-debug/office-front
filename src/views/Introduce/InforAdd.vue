<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import samplePhoto from '@/assets/anh nen.jpg'

const router = useRouter()

const employee = ref({
  id: Date.now().toString(), // 仮のID
  name: '',
  joinDate: '',
  department: '',
  hobby: '',
  message: '',
  photo: samplePhoto
})

const submitForm = () => {
  // ローカルストレージに保存（またはemitで親に渡す）
  const existing = JSON.parse(localStorage.getItem('employees') || '[]')
  existing.push(employee.value)
  localStorage.setItem('employees', JSON.stringify(existing))

  // 社員紹介ページに戻る
  router.push('/introduce')
}
</script>

<template>
  <div class="employee-form">
    <h1>新入社員追加</h1>
    <form @submit.prevent="submitForm">
      <label>名前：<input v-model="employee.name" required /></label><br />
      <label>入社年月：<input v-model="employee.joinDate" required /></label><br />
      <label>部署：
        <select v-model="employee.department" required>
          <option>営業部</option>
          <option>人事部</option>
          <option>財務部</option>
          <option>IT部門</option>
          <option>生産部門</option>
        </select>
      </label><br />
      <label>趣味：<input v-model="employee.hobby" /></label><br />
      <label>メッセージ：<textarea v-model="employee.message"></textarea></label><br />
      <button type="submit">追加</button>
    </form>
  </div>
</template>

<style scoped>
.employee-profile {
  max-width: 700px;
  margin: 50px auto;
  font-family: 'Segoe UI', sans-serif;
}

.employee-card {
  display: flex;
  gap: 20px;
  background: #f4f8ff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.employee-photo {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #4977d8;
}

.employee-info p {
  margin: 10px 0;
}
</style>
