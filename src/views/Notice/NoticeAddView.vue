<script setup>
  import axios from 'axios';
  import { reactive } from 'vue';
  import { useRouter } from 'vue-router';
   import api from '@/plugin/axios.js';

  // const data = reactive({
  //   title: '',
  //   body: '',
  // });

  const data = ref([]);

  const router = useRouter();

//記事追加
  // const addNotice = async () => {
  //   try {
  //     const url = '/notices';
  //     const res = await api.post(url, data);
  //     console.log(res.data);

  //     data.title = '';
  //     data.body = '';

  //     router.push('/notice');
  //   } catch (error) {
  //     console.error(error);
  //   }
  // };

  const addNotice = async () => {
    try {
      const url = `/notices`;
      
      const res = await api.post(url, {
        body: data.value.body,
        title: data.value.title,
        createdAt: data.value.createdAt,
        createdUserId: data.value.createdUserId

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
  <h2>お知らせを追加</h2>
  <p>タイトル：<input type="text" v-model="data.title" /></p>
  <p>本文<textarea v-model="data.body"></textarea></p>

  <button @click="addNotice">追加</button>

</template>
