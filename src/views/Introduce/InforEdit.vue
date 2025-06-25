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
const joinedAt = ref(route.query.joinedAt || '')
const myDepartment = ref(route.query.myDepartment  || '')
const hobby = ref(route.query.hobby || '')
const bio = ref(route.query.bio || '')
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
      joinedAt: joinedAt.value,
      myDepartment: myDepartment.value,
      hobby: hobby.value,
      bio: bio.value,
      photo: photo.value
    }
  })
}



const employee = ref([]);

const getData = async () => {
  const response = await api.get("/info/" + id);
  console.log("response.data: ", response.data);
  employee.value=response.data;
};

const saveData = async () => {
  const res = await api.put("/info/test/" + id,{
    name: employee.value.name,
    hobby: employee.value.hobby,
    bio: employee.value.bio,
    myDepartment: [{
      id: employee.value.myDepartment?.[0]?.id,
      name: employee.value.myDepartment?.[0]?.name,
    }]
  });
  console.log("res: ", res);
};

onMounted(getData);
</script>


<template>
  <div class="edit-page" :style="backgroundStyle">
    <div class="overlay">
      <h2>情報編集ページ</h2>
      <div class="form-content">
        <!-- <div class="photo-section">
          <img :src="photo" alt="社員写真" class="preview-photo" />
          <input type="file" @change="handleFileChange" />
        </div> -->

        <form @submit.prevent="handleSubmit">
          <label>名前：<input v-model="name" type="text" /></label>
          <!-- <label>入社年月：<input v-model="joinDate" type="month" /></label> -->
          <label>部署：<input v-model="myDepartment" type="text" /></label>
          <label>趣味：<input v-model="hobby" type="text" /></label>
          <label>メッセージ：<textarea v-model="bio" rows="4"></textarea></label>
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
h2{
  color: white;
  text-align: center;
  margin-bottom: 20px;
   font-weight: bold;
}
.overlay {
  background-color: rgba(221, 232, 222, 0.6);
  padding: 30px;
  border-radius: 12px;
  width: 90%;
  max-width: 450px;
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
  background-color: hsla(129, 70%, 30%, 0.55);
  color: rgb(18, 17, 17);
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}

button:hover {
  background-color: hsla(129, 69%, 20%, 0.67);
}
</style>
