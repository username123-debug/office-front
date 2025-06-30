<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import samplePhoto from '@/assets/anh nen.jpg'
import api from '@/plugin/axios.js';

const router = useRouter()

const employee = ref({
  name: '',
  email: '',
  password: '',
  joinedAt: '',
  myDepartment: '',
  hobby: '',
  bio: ''
});

const myDepartmentIds = ref({
  "人事部": 1,
  "経理部": 2,
  "営業部": 3,
  "IT部": 4,
  "財務部": 5,
  "生産部": 6
});

const confirmPassword = ref('');

const checkPassword = computed(() => {
  return employee.value.password && confirmPassword.value && employee.value.password != confirmPassword.value;
});

const saveData = async () => {

  try{
    const nameAndPass = await api.post("/users/check",{
      name: employee.value.name,
      password: employee.value.password
    });
    console.log("競合userのチェック:", nameAndPass);
  }catch(error){
    console.log("名前とパスワードの組が一致する人物が存在します:", error);
    return;
  }

  if (checkPassword.value) {
    alert("パスワードが一致しません");
    return;
  }

  try {
    const selectedName = employee.value.myDepartment;
    const selectedId = myDepartmentIds.value[selectedName];

    const res = await api.post('/users/save', {
      name: employee.value.name,
      email: employee.value.email,
      password: employee.value.password,
      joinedAt: employee.value.joinedAt + '-01',
      hobby: employee.value.hobby,
      bio: employee.value.bio,
      myDepartment: [{
        id: selectedId,
        name: selectedName,
      }]
    });

    console.log("res:", res);

  } catch (error) {
    console.error("ユーザーの追加でエラーが発生：", error);
  }
 

  router.push('/introduce'); 
};

</script>

<template>
  <div class="container">
    <div class="form-wrapper">
      <h1>新入社員追加</h1>
      <div class="employee-form">
        <form @submit.prevent="saveData">
          <div class="form-group">
            <label>名前</label>
            <input v-model="employee.name" required />
          </div>

          <div class="form-group">
            <label>入社年月</label>
            <input v-model="employee.joinedAt" type="month" required />
          </div>

          <div class="form-group">
            <label>メールアドレス</label>
            <input v-model="employee.email" type="email" required />
          </div>

          <div class="form-group">
            <label>パスワード</label>
            <input v-model="employee.password" type="password" required />
          </div>

          <div class="form-group">
            <label>パスワード（再入力）</label>
            <input v-model="confirmPassword" type="password" required />
          </div>


          <div class="form-group">
            <label>部署</label>
            <select v-model="employee.myDepartment" required>
              <option>営業部</option>
              <option>人事部</option>
              <option>財務部</option>
              <option>生産部</option>
              <option>IT部</option>
              
            </select>
          </div>

          <div class="form-group">
            <label>趣味</label>
            <input v-model="employee.hobby" />
          </div>

          <div class="form-group">
            <label>メッセージ</label>
            <textarea v-model="employee.bio"></textarea>
          </div>

          <div class="form-group center">
            <button type="submit">▶ 追加する</button>
          </div>
        </form>
      </div>

      <div v-if="checkPassword" style="color:red">
        パスワードが一致しません。
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  width: 90vw;
  background-color: #f0f4f8;
}

.form-wrapper {
  width: 50vw;
  margin: 60px auto;
  padding: 40px 30px;
  background: #f4f8ff;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  font-family: 'Segoe UI', sans-serif;
}

h1 {
  text-align: center;
  font-size: 24px;
  color: #333;
  margin-bottom: 30px;
}

.employee-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 15px;
  background-color: #fff;
  transition: border-color 0.3s;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  border-color: #4977d8;
  outline: none;
}

.form-group textarea {
  min-height: 80px;
  resize: vertical;
}

.form-group.center {
  text-align: center;
}

button[type="submit"] {
  background-color: #4977d8;
  color: white;
  padding: 12px 30px;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button[type="submit"]:hover {
  background-color: #365db5;
}

@media (max-width: 600px) {
  .form-wrapper {
    padding: 25px 20px;
    margin: 30px 15px;
  }

  h1 {
    font-size: 20px;
  }
}
</style>
