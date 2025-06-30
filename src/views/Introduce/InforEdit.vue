<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref ,onMounted, computed} from 'vue'
import backgroundImg from '@/assets/anh nen.jpg'
import api from '@/plugin/axios.js'

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


// const saveData = () => {
//   router.push({
//     name: 'IntroduceDetail',
//     params: { id },
//     query: {
//       name: name.value,
//       joinedAt: joinedAt.value,
//       myDepartment: myDepartment.value,
//       hobby: hobby.value,
//       bio: bio.value,
//       photo: photo.value
//     }
//   })
// }



const employee = ref({
  name: '',
  email:'',
  hobby: '',
  bio: '',
  myDepartment: [{ id: '', name: '' }]
});

const getData = async () => {
  const response = await api.get("/info/" + id);
  // employee.value = response.data;
  const data = response.data; // ✅ ここでdataを定義！
  console.log("data: ", data);

  if (!data.myDepartment || data.myDepartment.length === 0) {
    data.myDepartment = [{ id: '', name: '' }];
  }

  employee.value = data; // ✅ 安全な状態で代入
  console.log("employee.value: ", employee.value);
  
};

const saveData = async () => {


  // try {
  //   const myPassword = await api.post("/users/check/me", {
  //     password: employee.value.password
  //   });
  //   console.log("パスワードの登録が可能な状態", myPassword);
  // } catch (error) {
  //   alert("現在のパスワードと同じです。新しいパスワードを登録してください。");
  //   console.error("新旧パスワードが一致", error);
  //   return;
  // }

  try{
    const res = await api.put("/info/test/" + id, {
    name: employee.value.name,
    email: employee.value.email,
    hobby: employee.value.hobby,
    bio: employee.value.bio,
    myDepartment: [{
      id: employee.value.myDepartment?.[0]?.id,
      name: employee.value.myDepartment?.[0]?.name,
    }]
  });
  console.log("res: ", res);
  router.push({
    name: 'IntroduceDetail',
    params: { id },
  });

  router.push('/introduce/detail/' + id);

  }catch(error){
    console.error("エラーが発生しました：", error);
  }
  
};



onMounted(getData);
</script>


<template>
  <div class="edit-page" :style="backgroundStyle">
    <div class="overlay">
      <h2>情報編集ページ</h2>
      <div class="form-content">
         <form @submit.prevent="saveData">
  <label>名前：<input v-model="employee.name" type="text" /></label>

  <label>メールアドレス：<input v-model="employee.email" type="text"required /></label>

  <label v-if="employee.myDepartment && employee.myDepartment.length">
    部署：<input v-model="employee.myDepartment[0].name" type="text" />
  </label>

  <label>趣味：<input v-model="employee.hobby" type="text" /></label>

  <label>メッセージ：<textarea v-model="employee.bio" rows="4"></textarea></label>

  <button type="submit">保存</button>
</form>

      </div>
    </div>
  </div>
</template>


<style scoped>
.edit-page {
  display: flex;
   justify-content: center; /* ← 横方向中央 */
  align-items: center;     /* ← 縦方向中央 */
  height: 100vh;
  width: 100vw;
  overflow-x: hidden;
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
  width: 85%;
  max-width: 450px;
  color: white;
  box-sizing: border-box; /* ← 横はみ出し防止 */
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
