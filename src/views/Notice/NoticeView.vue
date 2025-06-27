<script setup>
  import { useRouter } from 'vue-router';
  import { ref, reactive, onMounted, computed } from 'vue';
  import axios from 'axios';
  import api from '@/plugin/axios.js';

  const data = reactive({
    title: '',
    body: '',
    createdAt: '',
    creater: '',
  });

  const notices = ref([]);

  const nameMap=ref(new Map());

  const getName = async (id) => {
    const res = await api.get(`/users/${id}`);
    return res.data.name;
  };

  //一覧表示
  //2件目（最新の情報）が1件目の下に来てしまうことの修正必要
  const getNotices = async () => {
    try {
      const res = await api.get('/notices');
      notices.value = res.data;
      console.log("res: ", res)
      console.log("res.data: ", res.data)
    } catch (error) {
      console.error(error);
    }
  };

  const router = useRouter();

  // const currentPage = ref(1)
  // const perPage = 5

  //削除機能
  const deleteNotices = async (id) => {
    if(!window.confirm("本当に削除しますか？")){
      return;
    }

    try {
      const res = await api.delete(`/notices/` + id);
      await getNotices();
      console.log(res.data);
    } catch (error) {
      console.log("error: ", error)
    }
  };

  //公開日時 (表示が1月1日00:00のような形式になるよう修正)
  const formatDate = (isoString) => {
    if (!isoString) return '';
    const date = new Date(isoString);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const hour = date.getHours();
    const minute = date.getMinutes().toString().padStart(2, '0');
    return `${month}月${day}日${hour}:${minute}`;
  }

  //   ページネーション  
  // const paginatedNotices = computed(() => {
  //   const start = (currentPage.value - 1) * perPage
  //   return notices.value.slice(start, start + perPage)
  // })

  // const totalPages = computed(() =>
  //   Math.ceil(notices.value.length / perPage)
  // )

  // const prevPage = () => {
  //   if (currentPage.value > 1) currentPage.value--
  // }

  // const nextPage = () => {
  //   if (currentPage.value < totalPages.value) currentPage.value++
  // }

  // onMounted(getNotices);

  onMounted(()=>{
    getNotices();
    for(let i; i<notices.value.size(); i++){
      const name = getName(notices.value.createdUserId);
      newMap.value.set(notices.value.createdUserId,name);
    }
  });

</script>

<template>
  <div class="container">
    <div class="container2">
      <main class="main">
        <h2>お知らせ一覧</h2>
        <!-- <form @submit.prevent class="search_container">
          <input type="text" v-model="keyword" size="25" placeholder="キーワード検索">
          <input type="submit" value="検索">
        </form> -->
        <!-- 
        <div>
          <button @click="sortOrder='asc'">古い順</button>
          <button @click="sortOrder='desc'">新しい順</button>

        </div> -->

        <table class="table1">
          <thead>
            <tr>
              <th>タイトル</th>
              <th>公開日時</th>
              <th>掲載者</th>
              <th>削除</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="notice in notices" :key="notice.id">
              <!-- v-for = "notice in searchNotices" 検索機能実装パターン-->
              <td>
                <router-link :to="`/detail/${notice.id}`">{{notice.title}}</router-link>
              </td>
              <td>{{ formatDate(notice.createdAt) }}</td>
              <td>{{ notice.createdUserName }}</td>
              <td><button @click="deleteNotices(notice.id)">削除</button></td>
            </tr>
          </tbody>
        </table>
        <div class="add">
          <label><button @click="router.push('/addnotice')">追加する</button></label>
        </div>
        <!-- <div class="pagination">
          <button @click="prevPage" :disabled="currentPage === 1">前へ</button>
          <span>ページ {{ currentPage }} / {{ totalPages }}</span>
          <button @click="nextPage" :disabled="currentPage === totalPages">次へ</button>
        </div> -->
      </main>
    </div>
  </div>
</template>

<style scoped>
  .container {
    width: 100%;
    clear: both;
  }

  .container2 {
    width: 100%;
    margin: 0 auto;
  }

  .main {
    padding: 2px;
    margin: 8px;
    height: 500px;
  }

  h2 {
    color: rgb(22, 121, 182);
    font-family: inherit;
    font-size: 24px;
    display: inline-block;
    font-weight: bold;
    border-left: solid 5px #38b3f2;
    border-bottom: solid 1px #38b3f2;
    margin: 0 auto 50px;
    padding: 5px 5px 5px 5px;
  }

  .search_container {
    box-sizing: border-box;
    position: relative;
    border: 1px solid #999;
    display: block;
    padding: 3px 10px;
    border-radius: 20px;
    height: 2.3em;
    width: 260px;
  }

  .search_container input[type="text"] {
    border: none;
    position: relative;
    height: 2.0em;
  }

  .search_container input[type="text"]:focus {
    outline: 0;
  }

  .search_container input[type="submit"] {
    cursor: pointer;
    font-family: FontAwesome;
    border: none;
    border-radius: 20%;
    background: #3879D9;
    color: #fff;
    outline: none;
    width: 2.9em;
    height: 2.0em;
  }


  .table1 {
    border-collapse: collapse;
    table-layout: fixed;
    width: 100%;
    max-width: 700px;
    overflow: hidden;
    text-align: center;
  }

  .table1 tr {
    background-color: #e6f1f6;
  }

  .table1 tr:nth-child(even) {
    background-color: #fff;
  }

  .table1 th,
  .table1 td {
    padding: 1em;
  }

  .table1 thead th {
    background-color: #4d9bc1;
    color: #fff;
    font-weight: bold;
    text-align: center;
  }

  .table1 tbody th {
    color: #4d9bc1;
  }

  @media screen and (max-width: 787px) {
    .table1 {
      border-radius: 0;
      text-align: left;
    }

    .table1 thead {
      display: none;
    }

    .table1 tr {
      background-color: unset;
    }

    .table1 th,
    .table1 td {
      display: block;
      border: 0;
      border-bottom: 1px solid #4d9bc1;
    }

    .table1 tbody th {
      background: #4d9bc1;
      color: #fff;
      text-align: center;
    }

    .table1 td::before {
      content: attr(data-label);
      background-color: #e6f1f6;
      font-weight: bold;
      display: inline-block;
      width: 20%;
      min-width: 4em;
      text-align: center;
      margin-right: 0.5em;
      padding: 4px;
    }
  }

  .add {
    margin-top: 20px;
    display: flex;
    justify-content: space-between;
    gap: 10px;
  }

  button {
    padding: 8px 16px;
    border: none;
    background-color: #3c82f6;
    color: white;
    border-radius: 4px;
    cursor: pointer;
  }

  button:hover {
    background-color: #2563eb;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    gap: 10px;
    align-items: center;
  }

  button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
</style>