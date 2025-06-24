<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref } from 'vue'
import backgroundImg from '@/assets/anh nen.jpg'

const backgroundStyle = {
  backgroundImage: `url(${backgroundImg})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  height: '100vh',
  width: '100vw',
}

const route = useRoute()
const router = useRouter()
const id = route.params.id

const name = ref(route.query.name || '')
const joinDate = ref(route.query.joinDate || '')
const department = ref(route.query.department || '')
const hobby = ref(route.query.hobby || '')
const intro = ref(route.query.intro || '')
const photo = ref(route.query.photo || '')  // 画像の初期値

const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    const reader = new FileReader()
    reader.onload = (e) => {
      photo.value = e.target.result
    }
    reader.readAsDataURL(file)
  }
}

const handleSubmit = () => {
  router.push({
    name: 'IntroduceDetail',
    params: { id },
    query: {
      name: name.value,
      joinDate: joinDate.value,
      department: department.value,
      hobby: hobby.value,
      intro: intro.value,
      photo: photo.value
    }
  })
}
</script>


<template>
  <div class="edit-page" :style="backgroundStyle">
    <div class="overlay">
      <h2>情報編集ページ</h2>
      <div class="form-content">
        <div class="photo-section">
          <img :src="photo" alt="社員写真" class="preview-photo" />
          <input type="file" @change="handleFileChange" />
        </div>

        <form @submit.prevent="handleSubmit">
          <label>名前：<input v-model="name" type="text" /></label>
          <label>入社年月：<input v-model="joinDate" type="month" /></label>
          <label>部署：<input v-model="department" type="text" /></label>
          <label>趣味：<input v-model="hobby" type="text" /></label>
          <label>メッセージ：<textarea v-model="intro" rows="4" /></label>
          <button type="submit">保存</button>
        </form>
      </div>
    </div>
  </div>
</template>


<style scoped>
.edit-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  font-family: sans-serif;
}

.overlay {
  background-color: rgba(0, 0, 0, 0.6);
  padding: 30px;
  border-radius: 12px;
  width: 90%;
  max-width: 800px;
  color: white;
}

.form-content {
  display: flex;
  gap: 40px;
  align-items: flex-start;
}

.photo-section {
  text-align: center;
}

.preview-photo {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 12px;
  border: 3px solid white;
}

form {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

label {
  display: flex;
  flex-direction: column;
  font-weight: bold;
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
