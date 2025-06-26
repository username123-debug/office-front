<script setup>
import { RouterView } from 'vue-router'
import photo001 from '@/assets/sky.jpg'
import photo002 from '@/assets/summer.jpg'
import photo003 from '@/assets/sun.jpg'
import photo004 from '@/assets/sunflower.jpg'
import photo005 from '@/assets/sunflower2.jpg'
import photo006 from '@/assets/cloud.png'
import api from '@/plugin/axios.js';
import {ref, onMounted,computed} from 'vue';

// 初期データ
// const initialEmployees = [
//   { id: '001', name: '田中 太郎', myDepartment: '営業部', bio: '営業を担当しています。', photo: photo001 },
//   { id: '002', name: '山田 花子', myDepartment: '人事部', bio: '人事を担当しています。', photo: photo002 },
//   { id: '003', name: '佐藤 一郎', myDepartment: 'IT部門', bio: 'ITエンジニアです。', photo: photo003 },
//   { id: '004', name: '鈴木 次郎', myDepartment: '財務部', bio: '財務管理を担当しています。', photo: photo004 },
//   { id: '005', name: '伊藤 三郎', myDepartment: '生産部門', bio: '生産ラインを担当しています。', photo: photo005 },
//   { id: '006', name: '高橋 四郎', myDepartment: '営業部', bio: '営業戦略を担当しています。', photo: photo006 }
// ]
const employees = ref([]);

//"/api/users/abstract"からidとnameだけを取得
const getData = async () => {
  const response = await api.get("/users/abstract/department");
  console.log("response: ",response);
  console.log("response.data: ",response.data);

  employees.value = response.data;

  console.log("employees.value: ", employees.value);
};

const showSubMenu = ref(false)
const toggleSubMenu = () => {
  showSubMenu.value = !showSubMenu.value
}

const selectedDepartment = ref('')

const filteredEmployees = computed(() =>
  selectedDepartment.value
    ? employees.value.filter(e => e.myDepartment === selectedDepartment.value)
    : employees.value
)
onMounted(getData);
</script>
<template>
  <div class="container">
    <aside class="sidebar">
      <h2>社員紹介</h2>
      <ul class="menu">
        <li><a href="#">ホーム</a></li>
        <li><a href="#">新入社員</a></li>

        <li>
          <a href="javascript:void(0)" @click="selectedDepartment = '営業部'">営業部門</a>
          <ul v-if="selectedDepartment === '営業部'" class="name-list">
            <li v-for="e in filteredEmployees" :key="e.id">
              <RouterLink :to="`/introduce/detail/${e.id}`">{{ e.name }}</RouterLink>
            </li>
          </ul>
        </li>

        <li>
          <a href="javascript:void(0)" @click="selectedDepartment = '人事部'">人事部門</a>
          <ul v-if="selectedDepartment === '人事部'" class="name-list">
            <li v-for="e in filteredEmployees" :key="e.id">
              <RouterLink :to="`/introduce/detail/${e.id}`">{{ e.name }}</RouterLink>
            </li>
          </ul>
        </li>

        <li>
          <a href="javascript:void(0)" @click="selectedDepartment = '財務部'">財務部門</a>
          <ul v-if="selectedDepartment === '財務部'" class="name-list">
            <li v-for="e in filteredEmployees" :key="e.id">
              <RouterLink :to="`/introduce/detail/${e.id}`">{{ e.name }}</RouterLink>
            </li>
          </ul>
        </li>

        <li>
          <a href="javascript:void(0)" @click="selectedDepartment = '生産部'">生産部門</a>
          <ul v-if="selectedDepartment === '生産部'" class="name-list">
            <li v-for="e in filteredEmployees" :key="e.id">
              <RouterLink :to="`/introduce/detail/${e.id}`">{{ e.name }}</RouterLink>
            </li>
          </ul>
        </li>

        <li>
          <a href="javascript:void(0)" @click="selectedDepartment = 'IT部'">IT部門</a>
          <ul v-if="selectedDepartment === 'IT部'" class="name-list">
            <li v-for="e in filteredEmployees" :key="e.id">
              <RouterLink :to="`/introduce/detail/${e.id}`">{{ e.name }}</RouterLink>
            </li>
          </ul>
        </li>

        <li>
          <a href="javascript:void(0)" @click="toggleSubMenu">そのほか</a>
          <ul v-if="showSubMenu" class="submenu">
            <li><router-link to="/introduce/add">社員紹介追加</router-link></li>
            <li><router-link to="/introduce/delete">社員情報削除</router-link></li>
          </ul>
        </li>
      </ul>
    </aside>

    <main class="content">
      <div class="section">
        <h2>このサイトについて</h2>
        <p>このサイトは社員の自己紹介ページです。</p>
      </div>

      <div class="section">
        <h2>更新履歴</h2>
        <ul>
          <li>2025.6.19 自己紹介サイト 作成しました</li>
        </ul>
      </div>

      <div class="section">
        <h2>新入社員</h2>
      </div>

      <div class="employee-grid">
        <div
          class="employee-card"
          v-for="employee in employees"
          :key="employee.id"
        >
          <img :src="employee.photo" alt="写真" class="employee-photo" />
          <hr class="separator" />
          <RouterLink :to="`/introduce/detail/${employee.id}`" class="employee-name">
            {{ employee.name }}
          </RouterLink>
          <hr class="separator" />
        </div>
      </div>
    </main>

    <RouterView />
  </div>
</template>


<style scoped>
.container {
  display: flex;
  height: 100vh;
  width: 100vw;
  /* font-family: sans-serif; */
}

/* 左サイドバー */
.sidebar {
  width: 250px;
  height: 180vh;
  background-color: #129350;
  color: white;
  padding: 20px;
  /* overflow-y: auto; */
}

.sidebar h2 {
  font-size: 26px;
  margin-bottom: 16px;
  border-bottom: 1px solid #666;
  padding-bottom: 8px;
  font-weight: bold;
}

.menu {
  list-style: none;
  padding: 0;
}

.menu li {
  margin: 12px 0;
}

.menu a {
  color: white;
  text-decoration: none;
  transition: color 0.2s;
  font-size: 20px;
}

.menu a:hover {
  color: #757575;
}

/* 右メインコンテンツ */
.content {
  flex: 1;
  padding: 40px;
  background-color: #fdfdfd;
  overflow-y: visible; 
}
.content {
  flex: 1;
  padding: 40px;
  background-color: #fdfdfd;
  overflow-y: visible;
}

/* 大見出し */
.content h1, .content h2 {
  color: #757575; /* 濃い青 */
  font-size: 24px;
  margin-bottom: 12px;
  border-left: 6px solid #1e3a8a;
  padding-left: 10px;
}

/* セクションごとに背景色を交互にする（グレー帯） */
.section {
  background-color: #EFEFEF;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 6px;
  border: 1px solid #757575;
  /* 右 + 下だけ太くする（上書き） */
  border-right: 6px solid #757575;
  border-bottom: 6px solid #757575;
}

/* 小見出し */
.section h3 {
  font-size: 18px;
  margin-bottom: 10px;
  color: #333;
}

/* 段落スタイル */
.section p {
  line-height: 1.6;
  color: #444;
}

.employee-grid {
  display: grid;
  grid-template-columns:  repeat(3, 1fr);
  gap: 40px;
  column-gap: 16px; 
  margin-top: 50px;
  margin-left:40px;
}

.employee-card {
  position: relative;
  overflow: hidden;
  width: 180px;       /* 横幅小さめ */
  height: 260px;      /* 縦長にする */
  background: white;
  /* border-radius: 20px; */
  padding: 0;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  border: 1px solid #A8DBA8;
  /* 右 + 下だけ太くする（上書き） */
  /* border-right: 6px solid #A8DBA8;
  border-bottom: 6px solid #A8DBA8; */
  display: flex;
  flex-direction: column; /* ← 縦並びに変更 */
  align-items: center; /* 中央揃え */
}
.employee-card::after {
  content: '';
  position: absolute;
  bottom: 0;
  right: 0;
  width: 0;
  height: 0;
  border-left: 30px solid transparent;
  border-bottom: 30px solid #A8DBA8;  /* ← 三角の色 */
}

.employee-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.employee-photo {
  width: 100%;
  /* max-width: 150px; */
   height: 66%; /* 上部3分の2 */
  object-fit: cover;
  border-bottom: 1px solid #ccc;
  margin-bottom: 12px;
}

.employee-name {
  display: inline-block;
  color: #1e3a8a;
  text-decoration: none;
  font-weight: bold;
}

.employee-name:hover {
  text-decoration: underline;
}
.separator {
  border: none;
  border-top: 1px solid #ccc;
  margin: 10px 0;
}

</style>