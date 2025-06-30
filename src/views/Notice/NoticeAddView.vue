<script setup>
  import axios from 'axios';
  import { reactive} from 'vue';
  import { useRouter } from 'vue-router';
  import api from '@/plugin/axios.js';

  const data = reactive({
    title: '',
    body: '',
  });


  const router = useRouter();

  //記事追加
  const addNotice = async () => {
    try {
      const url = `/notices`;
      
      const res = await api.post(url, {
        body: data.body,
        title: data.title
        // createdAt: data.value.createdAt,
        // createdUserId: data.value.createdUserId
      });
      console.log("res.data", res.data);

      data.title = '';
      data.body = '';

      router.push('/notice');
    } catch (error) {
      console.error(error);
    }
  };

</script>

<template>
  <div class="box26">
  <h2>お知らせを追加</h2>
  <p class="title">タイトル</p>
  <input type="text" v-model="data.title" />
  <p class="body">本文</p>
  <textarea v-model="data.body"></textarea>
  <p class="length">文字数：{{ data.body.length }}</p>

    <div class="button-group">
      <button @click="addNotice">追加</button>
      <button @click="router.push('/notice')" type="button">戻る</button>
    </div>
</div>
</template>

<style scoped>
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

  .title {
    font-size: 16px;
    border-bottom: solid 1px #38b3f2;
  }

  .body {
    font-size: 16px;
    border-bottom: solid 1px #38b3f2;
  }

  input[type="text"],
  textarea {
    width: 100%;
    padding: 10px 15px;
    font-size: 16px;
    border: none;
    border-bottom: 2px solid #007BFF;
    background-color: #f9faff;
    transition: border-color 0.3s;
    box-sizing: border-box;
  }

  input[type="text"]:focus,
  textarea:focus {
    outline: none;
    border-bottom-color: #0056b3;
    background-color: #fff;
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

  button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .box26 {
  width: 93vw !important;
  max-width: none !important;
  margin: 0 auto !important;
  box-sizing: border-box;
  padding: 0.5em 1em 2em !important;
}
.button-group {
  margin-top: 1em;
  display: flex;
}

.button-group button + button {
  margin-left: 0.5em;
}

</style>
