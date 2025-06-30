<script setup>
    import axios from 'axios';
    import { ref, reactive, onMounted } from 'vue';
    import { useRoute, useRouter } from 'vue-router';
    import api from '@/plugin/axios.js';


    const route = useRoute();
    const router = useRouter();

    const data = reactive({
        title: '',
        body: '',
    });

    const noticeId = route.params.id;

    const findNotice = async () => {
        try {
            console.log('noticeId:', noticeId);

            const res = await api.get(`/notices/${noticeId}`);
            data.title = res.data.title;
            data.body = res.data.body;
        } catch (error) {
            console.error(error);
        }
    }

    //編集日時の追加の有無?
    const update = async () => {
      try {
        await api.put(`/notices/${noticeId}`, data);
        console.log('更新成功！');
      } catch (error) {
        console.error(error);
      } finally {
        router.push('/notice');
      }
    };

    onMounted(findNotice);
</script>

<template>
    <h2>編集ページ</h2>
    <div class="box26">
        <p class="title">タイトル</p>
        <input type="text" v-model="data.title" />
        <p class="body">本文</p>
        <textarea v-model="data.body"></textarea>
        <p class="length">文字数：{{ data.body.length }}</p>
    </div>
    <div>
        <button @click="update">編集完了</button>
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
}
</style>