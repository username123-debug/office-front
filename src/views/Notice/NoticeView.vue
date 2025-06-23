<script setup>
  import { useRouter } from 'vue-router';
  import { ref, reactive, onMounted, computed } from 'vue';
  import axios from 'axios';
  import { getNotices } from '../../components/get/getNotices.js';

  const data = reactive({
    title: '',
    body: '',
    createdAt: '',
    creater: '',
  });

  const {
    notices,
    get
  } = getNotices();

  const router = useRouter();

  // const currentPage = ref(1)
  // const perPage = 5


  const deleteNotices = async (id) => {
    try {
      const url = '' + id;
      const res = await axios.delete(url);
      await get();
      console.log(res.data);
    } catch (error) {
      console.log(error)
    }
  };
  // 検索機能
  // const keyword = ref('');
  // const searchNotices = computed(() => {
  //   if(!keyword.value) return notices.value;
  //   return notices.value.filter(notice =>
  //     notice.title.toLowerCase().includes(keyword.value.toLowerCase())
  //   );
  // });

  //並び替え機能
  //const sortOrder = ref('asc');

  // const sortedNotices = computed(() => {
  //   const copied = Array.from(notices.value);
  //   return copied.sort((a, b) => {
  //     const dateA = new Date(a.createdAt);
  //     const dateB = new Date(b.createdAt);
  //     if(sortOrder.value === 'asc'){
  //       return dateA - dateB;
  //     }else {
  //       return dateB - dateA;
  //     }
  //   });
  // });

  //    ページネーション  
  // const paginatedNotices = computed(() => {
  //   const start = (currentPage.value - 1) * perPage
  //   return 
  // })

  onMounted(get);

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
                {{notice.title}}
              </td>
              <td>{{notice.createdAt}}</td>
              <td>{{notice.creater}}</td>
              <td><button @click="deleteNotices(notice.id)">削除</button></td>
            </tr>
          </tbody>
        </table>
        <div class="add">
          <label><button @click="router.push('/addnotice')">お知らせを追加する</button></label>
        </div>
      </main>
    </div>
  </div>
</template>

<style>
  .container2 {
    border: 2px solid black;
    width: 100%;
    height: 550px;
  }

  .main {
    border: 2px solid black;
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
    text-align: right;
  }

  button {
    width: 120px;
    height: 40px;
    border: none;
    color: white;
    background-color: rgb(15, 161, 219)
  }

  button:hover {
    color: #fff;
    background-color: rgb(28, 180, 240);
  }
</style>