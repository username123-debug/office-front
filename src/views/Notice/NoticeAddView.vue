<script setup>
  import axios from 'axios';
  import { reactive } from 'vue';
  import { useRouter } from 'vue-router';

  const data = reactive({
    title: '',
    body: '',
    createdAt: '',
  });

  const router = useRouter();


  const addNotice = async () => {
    try {
      const url = '';
      const res = await axios.post(url, data);
      console.log(res.data);

      data.title = '';
      data.body = '';
      data.createdAt = '';

      router.push('/notice');
    } catch (error) {
      console.error(error);
    }
  };
  
</script>

<template>
  <h2>お知らせを追加</h2>
  <p>タイトル：<input type="text" v-model="data.title" /></p>
  <p>本文<textarea v-model="data.body"></textarea></p>

  <button @click="addNotice">追加</button>

</template>