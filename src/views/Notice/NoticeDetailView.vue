<script setup>
  import axios from 'axios';
  import { ref, reactive, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import api from '@/plugin/axios.js';

  const notice = reactive({
    id: null,
    title: '',
    body: '',
    createdAt: '',
    createdUserName: '',
  });

  const router = useRouter();
  const route = useRoute();

  //特定の一件を表示
  const findNotice = async () => {
    try {
      const noticeId = route.params.id;
      const res = await api.get(`/notices/${noticeId}`);
      notice.id = res.data.id;
      notice.title = res.data.title;
      notice.body = res.data.body;
      notice.createdAt = res.data.createdAt;
      notice.createdUserName = res.data.createdUserName;
    } catch (error) {
      console.error(error);
    }
  }

  //公開日時
  const formatDate = (isoString) => {
    if (!isoString) return '';
    const date = new Date(isoString);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const hour = date.getHours();
    const minute = date.getMinutes().toString().padStart(2, '0');
    return `${month}月${day}日${hour}:${minute}`;
  };

  onMounted(findNotice);

</script>


<template>
  <div class="container2">
    <h2>お知らせ詳細</h2>
    <div class="box26">
      <p>タイトル:{{ notice.title }}</p>
      <p>本文:{{ notice.body }}</p>
    </div>
    <div>
      <p class="date">公開日時：{{ formatDate(notice.createdAt) }}
      </p>
      <p class="writer">掲載者:{{ notice.createdUserName }} </p>
    </div>
    <button @click="router.push('/editnotice')">編集へ</button>
    <div class="back">
      <button @click="router.push('/notice')">一覧に戻る</button>
    </div>
  </div>
</template>

<style>
  .container2 {
    width: 100%;
    height: 550px;
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

  .main {
    padding: 2px;
    margin: 8px;
    height: 500px;
  }

  .box26 {
    position: relative;
    margin: 2em 0;
    padding: 0.5em 1em;
    border: solid 3px #95ccff;
    border-radius: 8px;
    height: 60%;
  }

  .box26 .box-title {
    position: absolute;
    display: inline-block;
    top: -13px;
    left: 10px;
    padding: 0 9px;
    line-height: 1;
    font-size: 19px;
    background: #FFF;
    color: #95ccff;
    font-weight: bold;
  }

  .box26 p {
    margin: 0;
    padding: 0;
  }

  .date {
    border: 2px solid black;
    width: 35%;
  }

  .writer {
    border: 2px solid black;
    width: 35%;
  }

  .back {
    text-align: right;
  }
</style>
