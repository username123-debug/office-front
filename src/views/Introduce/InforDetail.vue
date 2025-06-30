<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref, computed, onMounted } from 'vue'
import photoinit from '@/assets/takiguchi.jpg'
import photoinitadmin from '@/assets/summer.jpg'
import phototaro from '@/assets/sun.jpg'
import photoishihara from '@/assets/takiguchi.jpg'
import photofuruta from '@/assets/furuta.jpg'
import phototakiguchi from '@/assets/taki.jpg'
import photoarai from '@/assets/cloud.png'
import phototanguyen from '@/assets/nguyen.jpg'
import phototawang from '@/assets/cloud.png'

import backgroundImg from '@/assets/anh nen.jpg'
import api from '@/plugin/axios.js';

const route = useRoute();
const router = useRouter();
const id = route.params.id;

const photoMap = {
  init: photoinit,
  initadmin: photoinitadmin,
  taro: phototaro,
  ishihara: photoishihara,
  furuta: photofuruta,
  takiguchi: phototakiguchi,
  arai: photoarai,
  nguyen: phototanguyen,
  wang: phototawang
};

const backgroundStyle = {
  backgroundImage: `url(${backgroundImg})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  height: '100vh',
  width: '100vw',
}
//  ✅ 社員一覧を格納する（employeeData は使わない）
const employeeList = ref([]);

// ✅ APIから社員一覧を取得してemployeeListに代入
const getEmployeeList = async () => {
  const response = await api.get("/users/abstract/department");
  employeeList.value = response.data;
};
onMounted(getEmployeeList);

// ✅ 部門ごとに社員をフィルター
const selectedDepartment = ref('');
const filteredEmployees = computed(() =>
  selectedDepartment.value
    ? employeeList.value.filter(
        e =>
          e.myDepartment === selectedDepartment.value || // myDepartment が文字列の場合
          e.myDepartment?.[0]?.name === selectedDepartment.value // 配列＋オブジェクト形式の場合
      )
    : employeeList.value
);

const employee = ref([]);
const myRole = ref('');


const getData = async () => {
  const response = await api.get("/info/"+id);
  console.log("response: ",response);
  console.log("response.data: ",response.data);
  console.log("response.data.id: ",response.data.id);
  const data = response.data;
  // ★ 写真の割り当てを追加 ★
  data.photo = photoMap[data.name?.toLowerCase()] || photoinit;

  employee.value = data;
  console.log("employee.value: ", employee.value);
  console.log("employee.value.name: ", employee.value.name);
  console.log("employee.value.myDepartment[0].name: ", employee.value.myDepartment[0].name);
};

const selectedEmployee = ref(null)

const selectEmployee = async (selected) => {
  try {
    const response = await api.get(`/info/${selected.id}`);
   
    const data = response.data;

    // 写真をセット
    data.photo = photoMap[data.name?.toLowerCase()] || photoinit;
     employee.value = data;
    console.log('詳細取得:', employee.value);
  } catch (error) {
    console.error('詳細情報の取得に失敗しました:', error);
  }
};



// const employeeData = [
//   { id: '001', name: '田中 太郎', myDepartment: '営業部', bio: '営業を担当しています。hhhhh', photo: photo001 },
//   { id: '002', name: '山田 花子', myDepartment: '人事部', bio: '人事を担当しています。', photo: photo002 },
//   { id: '003', name: '佐藤 一郎', myDepartment: 'IT部門', bio: 'ITエンジニアです。', photo: photo003 },
//   { id: '004', name: '鈴木 次郎', myDepartment: '財務部', bio: '財務管理を担当しています。', photo: photo004 },
//   { id: '005', name: '伊藤 三郎', myDepartment: '生産部門', bio: '生産ラインを担当しています。', photo: photo005 },
//   { id: '006', name: '高橋 四郎', myDepartment: '営業部', bio: '営業戦略を担当しています。', photo: photo006 }
// ]



const goToEdit = () => {
  router.push({
    name: 'Edit',
    params: { id: employee.id },
  })
}
const showSubMenu = ref(false)
const toggleSubMenu = () => {
  showSubMenu.value = !showSubMenu.value
}


const editJoinedAt = (string) => {
  if(!string){
    return '';//joinedAtを取ってこれない場合の処理.
  }
  const [year, month] = string.split('T')[0].split('-');// const[year, month] = ['2025', '06', '24'];
  return `${year}年${parseInt(month)}日`;
};

const isFaded = ref(false);

const getRole = async () => {
  const role = await api.get("/users/myrole");
  console.log("ログイン中のユーザーロール",role);
  myRole.value = role.data;
};

onMounted(()=>{
  getData();
  getRole();
});

</script>

<template>

  <div class="container">
    <!-- 左側：サイドバー -->
  <div class="container" :style="backgroundStyle">
    

    <aside class="sidebar">
      <h2>社員紹介</h2>
      <ul class="menu">
         <li><router-link to="/introduce">ホーム</router-link></li>
        <!-- <li><a href="#">新入社員</a></li> -->

        <li>
          <a href="javascript:void(0)" @click="selectedDepartment = '営業部'">営業部門</a>
          <ul v-if="selectedDepartment === '営業部'" class="name-list">
           <div v-for="e in filteredEmployees" :key="e.id">
              <div v-if="e.deleteFlag !== 'true'">
               <a href="javascript:void(0)" @click="selectEmployee(e)">{{ e.name }}</a>
              </div>
           </div>
          </ul>
        </li>

        <li>
          <a href="javascript:void(0)" @click="selectedDepartment = '人事部'">人事部門</a>
          <ul v-if="selectedDepartment === '人事部'" class="name-list">
           <div v-for="e in filteredEmployees" :key="e.id">
              <div v-if="e.deleteFlag !== 'true'">
              <a href="javascript:void(0)" @click="selectEmployee(e)">{{ e.name }}</a>
              </div>
           </div>
          </ul>
        </li>

        <li>
          <a href="javascript:void(0)" @click="selectedDepartment = '財務部'">財務部門</a>
          <ul v-if="selectedDepartment === '財務部'" class="name-list">
           <div v-for="e in filteredEmployees" :key="e.id">
              <div v-if="e.deleteFlag !== 'true'">
              <a href="javascript:void(0)" @click="selectEmployee(e)">{{ e.name }}</a>
              </div>
           </div>
          </ul>
        </li>

        <li>
          <a href="javascript:void(0)" @click="selectedDepartment = '生産部'">生産部門</a>
          <ul v-if="selectedDepartment === '生産部'" class="name-list">
            <div v-for="e in filteredEmployees" :key="e.id">
              <div v-if="e.deleteFlag !== 'true'">
              <a href="javascript:void(0)" @click="selectEmployee(e)">{{ e.name }}</a>
              </div>
            </div>
          </ul>
        </li>

        <li>
          <a href="javascript:void(0)" @click="selectedDepartment = 'IT部'">IT部門</a>
          <ul v-if="selectedDepartment === 'IT部'" class="name-list">
            <div v-for="e in filteredEmployees" :key="e.id">
              <div v-if="e.deleteFlag !== 'true'">
              <a href="javascript:void(0)" @click="selectEmployee(e)">{{ e.name }}</a>
              </div>
            </div>
          </ul>
        </li>

        <li>
          <a href="javascript:void(0)" @click="toggleSubMenu" v-if="myRole == 'ROLE_ADMIN'">そのほか</a>
          <ul v-if="showSubMenu" class="submenu">
            <li><router-link to="/introduce/add">社員紹介追加</router-link></li>
            <li><router-link to="/introduce/delete">社員情報削除</router-link></li>
          </ul>
        </li>
      </ul>
    </aside>

    <main class="content":class="{ faded: isFaded }">
      <div class="detail-wrapper">
  <div class="left-column">
    <transition name="fade" appear @before-enter="isFaded = true"
  @after-enter="isFaded = true">
      <img :src="employee.photo" alt="写真" class="small-photo" />
    </transition>
    <transition name="fade" appear >
      <h1>{{ employee.name }}</h1>
    </transition>
  </div>

  <div class="right-column">
    <transition name="fade" appear>
      <p><strong>◆入社年月：</strong> {{ employee.joinedAt }}</p>
    </transition>
    <transition name="fade" appear>
      <p><strong>◆メールアドレス：</strong> {{ employee.email }}</p>
    </transition>
    <transition name="fade" appear >
      <p v-if="employee.myDepartment && employee.myDepartment.length>0"><strong>◆部署：</strong> {{ employee.myDepartment[0].name }}</p>
    </transition>
    <transition name="fade" appear >
      <p><strong>◆趣味：</strong> {{ employee.hobby }}</p>
    </transition>
    <transition name="fade" appear >
      <p><strong>◆メッセージ：</strong><br />{{ employee.bio }}</p>
    </transition>

    <transition name="fade" appear >
      <button class="edit-button" @click="goToEdit" v-if="myRole == 'ROLE_ADMIN'">▶ 編集</button>
    </transition>
  </div>

</div>

      <RouterView/>
    </main>
  </div>
  </div>
</template>

<style scoped>
.container {
   display: flex;
  height: 100vh;
  width: 90vw;
}
.sidebar {
  width: 250px;

  /* background-color: #129350; */
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
.content {
  flex: 1;
  position: relative;
}

.content {
  padding: 30px;
  transition: background-color 2s ease;
  background-color: transparent; /* 最初は透明（写真だけ見える） */
}

.content.faded {
  background-color: rgba(255, 255, 255, 0.6); /* 薄い白を上にかぶせる */
}

.detail-wrapper {
  display: flex;
  align-items: flex-start;
  padding: 60px 40px;
  gap: 40px; /* ← 適度な間隔 */
}

.left-column {
  width: 250px;
  text-align: center;
  padding-right: 20px;
  /* border-right: 2px solid #ccc; ← ここで縦線を追加 */
}

.right-column {
  padding-left: 40px;
  flex: 1;
  font-size: 16px;
  color: #333;
  word-wrap: break-word;        /* 単語が長くても折り返す */
  overflow-wrap: break-word;    /* こちらも併用しておくと安心 */
  white-space: normal;          /* テキストは自動的に折り返す */
   max-width: 500px;        /* ← この幅で折り返す */
  word-break: break-word;
  overflow-wrap: break-word;
  white-space: normal;
}

.small-photo {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 12px;
}

.employee-name {
  font-size: 15px;
  color: #1e3a8a;
  margin: 0;
}

.edit-button {
  margin-top: 40px;
  padding: 8px 16px;
  margin-left: 170px;
}
.right-column p {
  margin-bottom: 32px;
  line-height: 2; 
  font-size: 16px;
  word-break: break-word; /* ← こちらでも強制的に折り返す */
}
.fade-enter-active {
  transition: opacity 0.8s ease, transform 0.8s ease;
}
.fade-enter-from {
  opacity: 0;
  transform: translateY(30px);
}
.fade-enter-to {
  opacity: 1;
  transform: translateY(0);
}
.left-column h1 {
  font-weight: bold;    /* 太字にする */
  font-size: 26px;      /* 少し大きくする（お好みで調整） */
  color: #000309;       /* 現在の文字色をキープ（任意） */
  margin-top: 10px;     /* 写真との間に少し余白（任意） */
}
.right-column strong {
  font-weight: 600;
  font-size: 20px; /* ← 必要なら文字サイズも */
}


</style>
