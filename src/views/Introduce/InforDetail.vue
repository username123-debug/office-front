<script setup>
import { useRoute, useRouter } from 'vue-router'
import photo001 from '@/assets/sky.jpg'
import photo002 from '@/assets/summer.jpg'
import photo003 from '@/assets/sun.jpg'
import photo004 from '@/assets/sunflower.jpg'
import photo005 from '@/assets/sunflower2.jpg'
import photo006 from '@/assets/cloud.png'
import backgroundImg from '@/assets/anh nen.jpg'

const backgroundStyle = {
  backgroundImage: `linear-gradient(rgba(255,255,255,0.5), rgba(255,255,255,0.5)), url(${backgroundImg})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  height: '100vh',
  width: '100vw',
}

const route = useRoute()
const router = useRouter()
const id = route.params.id

const employeeData = [
  { id: '001', name: '田中 太郎', department: '営業部', intro: '営業を担当しています。', photo: photo001 },
  { id: '002', name: '山田 花子', department: '人事部', intro: '人事を担当しています。', photo: photo002 },
  { id: '003', name: '佐藤 一郎', department: 'IT部門', intro: 'ITエンジニアです。', photo: photo003 },
  { id: '004', name: '鈴木 次郎', department: '財務部', intro: '財務管理を担当しています。', photo: photo004 },
  { id: '005', name: '伊藤 三郎', department: '生産部門', intro: '生産ラインを担当しています。', photo: photo005 },
  { id: '006', name: '高橋 四郎', department: '営業部', intro: '営業戦略を担当しています。', photo: photo006 }
]

let employee = employeeData.find(emp => emp.id === id) || {
  name: '不明',
  joinDate: '',
  department: '不明',
  hobby: '',
  intro: 'データが見つかりません。'
}

const query = route.query
employee = {
  ...employee,
  name: query.name || employee.name,
  joinDate: query.joinDate || employee.joinDate,
  department: query.department || employee.department,
  hobby: query.hobby || employee.hobby,
  intro: query.intro || employee.intro
}

const goToEdit = () => {
  router.push({
    name: 'Edit',
    params: { id: employee.id },
    query: {
      name: employee.name,
      joinDate: employee.joinDate,
      department: employee.department,
      hobby: employee.hobby,
      intro: employee.intro
    }
  })
}
</script>

<template>
  <div class="container" :style="backgroundStyle">
    <aside class="sidebar">
      <h2>社員紹介</h2>
      <ul class="menu">
        <li><router-link to="/introduce">ホーム</router-link></li>
        <li><a href="#">新入社員</a></li>
        <li><a href="#">営業部門</a></li>
        <li><a href="#">人事部門</a></li>
        <li><a href="#">財務部門</a></li>
        <li><a href="#">生産部門</a></li>
        <li><a href="#">IT部門</a></li>
      </ul>
    </aside>
    <main class="content">
      <div class="detail-wrapper">
        <div class="left-column">
          <img :src="employee.photo" alt="写真" class="small-photo" />
          <h1>{{ employee.name }}</h1>
        </div>
        <div class="curved-line-wrapper">
          <svg width="40" height="500" viewBox="0 0 40 500" xmlns="http://www.w3.org/2000/svg">
            <path d="M 5 0 Q 73 250, 5 500" fill="none" stroke="#757575" stroke-width="2"/>
          </svg>
        </div>
        <div class="right-column">
          <p class="info-item1"><strong>◆入社年月：</strong>{{ employee.joinDate }}</p>
          <p class="info-item2"><strong>◆部署：</strong>{{ employee.department }}</p>
          <p class="info-item3"><strong>◆趣味：</strong>{{ employee.hobby }}</p>
          <p class="info-item4"><strong>◆メッセージ：</strong><br />{{ employee.intro }}</p>
          <button class="edit-button" @click="goToEdit">▶ 編集</button>
        </div>
      </div>
      <RouterView/>
    </main>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  height: 100vh;
  font-family: sans-serif;
}
.sidebar {
  width: 250px;
  background-color: #79BD9A;
  color: white;
  padding: 20px;
  overflow-y: auto;
}
.sidebar h2 {
  font-size: 20px;
  margin-bottom: 16px;
  border-bottom: 1px solid #666;
  padding-bottom: 8px;
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
}
.menu a:hover {
  color: #757575;
}
.content {
  flex: 1;
  position: relative;
}
.detail-wrapper {
  display: flex;
  gap: 24px;
  align-items: flex-start;
  padding: 60px 40px;
}
.left-column {
  width: 300px;
  text-align: center;
}
.small-photo {
  width: 250px;
  height: 250px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 12px;
}
.curved-line-wrapper {
  height: 500px;
  margin: 0 20px;
}
.right-column {
  width: 600px;
  font-size: 16px;
  color: #333;
}
.info-item1,
.info-item4 {
  margin-top: 20px;
  margin-left: 10px;
}
.info-item2,
.info-item3 {
  margin-top: 20px;
  margin-left: 30px;
}
.edit-button {
  margin-top: 40px;
  padding: 8px 16px;
}
.right-column p {
  margin-bottom: 32px;
  line-height: 2; 
  font-size: 20px;
}
</style>
