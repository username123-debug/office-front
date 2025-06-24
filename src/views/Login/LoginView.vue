<script setup>
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import api from '@/plugin/axios.js';

const username=ref('');
const password=ref('');
const router = useRouter();

const login = async () => {
  try{
    const params = new URLSearchParams();
    params.append("username",username.value);
    params.append("password",password.value);
    const response = await api.post("/api/login",params,{
      headers:{
        "Content-Type":"application/x-www-form-urlencoded"
      }
    });
    console.log("ログイン成功:",response);
    if(response.data.role==="ROLE_USER"){
      router.push('/');
      console.log("一般ユーザーのページに遷移")
    }else if(response.data.role==="ROLE_ADMIN"){
      //router.push();
      console.log("管理者のページに遷移");
    }
  }catch(error){
    console.log("error is occurred:",error);
  }
};
</script>
<template>
  <form @submit.prevent="login">
    <p>username:<input v-model="username" /></p>
    <p>password:<input v-model="password" /></p>
    <button type="submit">submit</button>
  </form>
</template>
