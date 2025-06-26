<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import samplePhoto from '@/assets/anh nen.jpg'
import api from '@/plugin/axios.js';

const router = useRouter()

// const employee = ref({
//   id: Date.now().toString(),
//   name: '',
//   joinedAt: '',
//   myDepartment: '',
//   hobby: '',
//   bio: '',
//   photo: samplePhoto
// })

// const submitForm = () => {
//   const existing = JSON.parse(localStorage.getItem('employees') || '[]')
//   existing.push(employee.value)
//   localStorage.setItem('employees', JSON.stringify(existing))
//   router.push('/introduce')
// }

const employee = ref({
  name: '',
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

// const saveData = async () => {

//   const selectedName = employee.value.myDepartment;
// const selectedId = myDepartmentIds.value[selectedName];

//   const res = await api.put("/info/test/" + id,{
//     name: employee.value.name,
//     hobby: employee.value.hobby,
//     bio: employee.value.bio,
//     myDepartment: [{
//       id: employee.value.myDepartment?.[0]?.id,
//       name: employee.value.myDepartment?.[0]?.name,
//     }]
//   });
//   console.log("res: ", res);
// };

const saveData = async () => {
  const selectedName = employee.value.myDepartment;
  const selectedId = myDepartmentIds.value[selectedName]; // .valueは不要に変更した想定

  const res = await api.post('/users/save', {
    name: employee.value.name,
    joinedAt: employee.value.joinedAt + '-01',
    hobby: employee.value.hobby,
    bio: employee.value.bio,
    myDepartment: [{
      id: selectedId,
      name: selectedName,
    }]
  });

  console.log("res:", res);
};


</script>

<template>
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
          <label>部署</label>
          <select v-model="employee.myDepartment" required>
            <option>営業部</option>
            <option>人事部</option>
            <option>経理部</option>
            <option>財務部</option>
            <option>IT部</option>
            <option>生産部</option>
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
  </div>
</template>

<style scoped>
.form-wrapper {
  max-width: 600px;
  margin: 50px auto;
  font-family: 'Segoe UI', sans-serif;
  background: #f4f8ff;
  padding: 30px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  position: center;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.employee-form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-weight: bold;
  margin-bottom: 6px;
  color: #333;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
}

.form-group textarea {
  min-height: 80px;
}

.form-group.center {
  text-align: center;
}

button[type="submit"] {
  background-color: #4977d8;
  color: white;
  padding: 10px 25px;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s ease;
}

button[type="submit"]:hover {
  background-color: #365db5;
}
</style>
