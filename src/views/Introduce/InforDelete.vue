<template>
  <div class="container">
  <div class="delete-page">
    <h2>社員情報削除</h2>
    <ul class="employee-list">
      <li v-for="employee in selectedIds" :key="employee.id" class="employee-item">
        <span>{{ employee.name }}（{{ employee.myDepartment }}）</span>
        <button @click="deleteEmployee(employee.id)">削除</button>
      </li>
    </ul>
  </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import {useRouter} from 'vue-router';
import photo001 from '@/assets/sky.jpg'
import photo002 from '@/assets/summer.jpg'
import photo003 from '@/assets/sun.jpg'
import photo004 from '@/assets/sunflower.jpg'
import photo005 from '@/assets/sunflower2.jpg'
import photo006 from '@/assets/cloud.png'
import api from '@/plugin/axios.js';

const employees = ref([]);
const myId = ref('');
const router = useRouter();

const getData = async () => {
  try{
    const response = await api.get("/users/abstract/department");
    console.log("ユーザー取得(response): ", response);
    employees.value = response.data;
    console.log("ユーザー取得(employees.value):", employees.value);
  }catch(error){
    console.error("ユーザー情報の一括取得に失敗", error);
  }
  
};

const getMe = async () => {
  try{
    const response = await api.get("/users/me");
    console.log("ログインユーザーの情報取得(response): ", response);
    myId.value = response.data.id;
    console.log("myId.value:", myId.value)
    console.log("response",response.data);
  }catch(error){
    console.error("ログインユーザーの情報の取得に失敗: ", error);
  }
};

const deleteEmployee = async (id) => {
  try{
    const response = await api.delete(`/users/delete/${id}`);
    console.log("ユーザー削除(response): ", response);
  }catch(error){
    console.error("ユーザーの削除に失敗", error);
  }

  router.push("/introduce");
 await getData(); // 削除後にデータを再取得
};

const selectedIds = computed(() => {
  if (!myId.value) return [];
  return employees.value.filter(user =>  Number(user.id) !== myId.value && user.deleteFlag !== 'true'
  )
});

onMounted(async ()=>{
  await getMe();
  await getData();
});

</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 90vw;;
  background-color: #f0f0f0;
}
.delete-page {
  padding: 20px;
  width: 40vw;
}
.employee-list {
  list-style: none;
  padding: 0;
}
.employee-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 8px;
}
button {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background-color: #c0392b;
}
</style>
