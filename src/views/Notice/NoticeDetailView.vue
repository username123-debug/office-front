<script setup>
  // import axios from 'axios';
  // import { ref, reactive } from 'vue';

  // const data = reactive({
  //   title: '',
  //   body: '',
  //   createdAt: '',
  //   writer: '',
  // });

  // const updateId = ref();
  
  // const{
  //   notices,
  //   get
  // } = getNotices();

  // const getNotices = async() => {
  //   try{
  //     const noticeId = route.params.id;
  //     const res = await axios.get(``);
  //     data.title = res.data.title;
  //     data.body = res.data.body;
  //     data.createdAt = res.data.createdAt;
  //     data.writer = res.data.writer;
  //   }catch(error){
  //     console.error(error);
  //   }
  // }

  // const update = async() =>{
  //   try{
  //     const url = '';
  //     const res = await axios.put(url,data);
  //     console.log(res.data);
  //     data.title = '';
  //     data.body = '';
  //   }catch(error){
  //     console.log(error)
  //   }
  // }
</script>


<template>
  <div class="container2">
    <h2>お知らせ詳細</h2>
    <div class="box26">
      <p>タイトル：<input type="text" v-model="data.title" /></p>
      <p>本文：<textarea v-model="data.body"></textarea></p>
    </div>
    <div>
      <p class="date">公開日時:{{ notice.createdAt }}
      </p>
      <p class="writer">掲載者:{{notice.writer}} </p>
    </div>
    <button @click="update">編集完了</button>
    <div class="back">
      <button>一覧に戻る</button>
    </div>
  </div>
</template>

<style>
  .container2 {
    border: 2px solid black;
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
    border: 2px solid black;
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