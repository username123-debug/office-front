<script setup>
import { useRoute,useRouter } from 'vue-router'

import { ref } from 'vue'
import backgroundImg from '@/assets/anh nen.jpg'
const backgroundStyle = {
  backgroundImage: `url(${backgroundImg})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  height: '100vh',
  width:'100vw',
}
const route = useRoute()
const router = useRouter()
const id = route.params.id

const name = ref(route.query.name || '')
const joinDate = ref(route.query.joinDate || '')
const department = ref(route.query.department || '')
const hobby = ref(route.query.hobby || '')
const intro = ref(route.query.intro || '')

// 保存処理（今回はデモとしてログ表示）
const handleSubmit = () => {
  console.log('保存データ:', {
    name: name.value,
    joinDate: joinDate.value,
    department: department.value,
    hobby: hobby.value,
    intro: intro.value,
  })
   // 変更した内容を持って戻る
  router.push({
    name: 'IntroduceDetail',
    params: { id },  // 必ずIDを戻す
    query: {
      name: name.value,
      joinDate: joinDate.value,
      department: department.value,
      hobby: hobby.value,
      intro: intro.value
    }
  })
}
</script>

<template>
  <div class="edit-page":style="backgroundStyle">
    <div class="overlay">
    <h2>情報編集ページ</h2>
    <form @submit.prevent="handleSubmit">
        <label>
          名前：
          <input v-model="name" type="text" />
        </label>
    
        <label>
          入社年月：
          <input v-model="joinDate" type="month" />
        </label>

        <label>
          部署：
          <input v-model="department" type="text" />
        </label>

        <label>
          趣味：
          <input v-model="hobby" type="text" />
        </label>

        <label>
          メッセージ：
          <textarea v-model="intro" rows="4"></textarea>
        </label>

        <button type="submit">保存</button>
      </form>
  </div>
  </div>
</template>

<style scoped>
.edit-page {
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  min-height: 100vh;
}

.overlay {
  background-color: rgba(0, 0, 0, 0.6);
  padding: 30px;
  border-radius: 12px;
  width: 90%;
  max-width: 600px;
}

form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

label {
  display: flex;
  flex-direction: column;
  font-weight: bold;
  color: white;
}

input,
textarea {
  padding: 8px;
  border-radius: 8px;
  border: none;
  font-size: 16px;
}

button {
  margin-top: 10px;
  padding: 10px;
  background-color: #4977d8;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}

button:hover {
  background-color: #365fb3;
}
</style>