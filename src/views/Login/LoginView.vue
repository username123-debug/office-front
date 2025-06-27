<!-- <script setup>
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
</template> -->

<script setup>
// Vue の Composition API から ref をインポート
import { ref } from 'vue';
// Vue Router の useRouter をインポート（ルーティング操作のため）
import { useRouter } from 'vue-router';
// カスタム axios インスタンスをインポート（API 通信に使用）
import api from '@/plugin/axios.js';
import { defineEmits } from 'vue';


const emit = defineEmits(['login-success']);

// フォームの入力値をリアクティブ変数として定義
const username = ref('');
const password = ref('');

const user = ref([]);
// ルーターインスタンス取得（ページ遷移用）
const router = useRouter();

// ログイン処理関数
const login = async () => {
  try {
    // フォームデータを URLSearchParams 形式で作成
    const params = new URLSearchParams();
    params.append("username", username.value);
    params.append("password", password.value);

    // ログインAPIにPOSTリクエスト（application/x-www-form-urlencoded 形式）
    const res  = await api.post("/api/login", params, {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      }
    });

    console.log('res',res);
    user.value = res.data;

    // ログイン成功したのでフラグをtrueに
    console.log('ログイン成功！emit直前');
    emit('login-success');
    // console.log("isLoggedIn: ", isLoggedIn.value);
    // ロールに応じて遷移先を分岐
    if (user.value.role === "ADMIN" || user.value.role === "ROLE_ADMIN") {
      // 管理者の場合、管理者用ページへ遷移
      router.push("/admin/home");
      console.log("管理者のページに遷移（テスト用に一般ユーザーと同じページを表示中）");
    } else {
      // 一般ユーザーの場合、トップページへ遷移
      router.push("/top");
      console.log("一般ユーザーのページに遷移");
    }
  } catch (error) {
    // エラーハンドリング（例: 認証失敗、通信エラーなど）
    console.log("error is occurred:", error);
  }
};
</script>

<template>
  
<div class="login-hero">
    <div class="hero-content">
      <!-- <div class="hero-text">
        <h1>サイボウズのクラウド基盤で、<br>組織の情報共有に安全・安心を。</h1>
        <p>Safety and security for organizational information</p>
      </div> -->
    </div>
  
    <form class="login-form" @submit.prevent="login">
      <p>username:<input v-model="username" /></p>
      <p>password:<input v-model="password" type="password" /></p>
      <button type="submit">Submit</button>
    </form>
  </div>
</template>
<style scoped>
.login-background {
  height: 100vh;
  background: linear-gradient(to right, #90e0ef, #0077b6);
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-hero {
  min-height: 100vh;
  width: 90vw;
  background: linear-gradient(135deg, #90e0ef, #0077b6);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  font-family: "Helvetica Neue", sans-serif;
  text-align: center;
}

.hero-content {
  margin-bottom: 40px;
}

.hero-text h1 {
  font-size: 26px;
  font-weight: bold;
  color: #003566;
  margin-bottom: 12px;
}

.hero-text p {
  font-size: 16px;
  color: #005f73;
}



.login-form p {
  margin-bottom: 20px;
  text-align: left;
  font-size: 16px;
  color: #333;
}

.login-form button {
  width: 100%;
  padding: 12px;
  background-color: #074eb3;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-form button:hover {
  background-color: #0b719a;
}


.login-form {
  width: 400px;
  padding: 30px;
  border: 2px solid #278bdc;
  border-radius: 12px;
  background-color: #f9f9f9;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.login-form p {
  margin-bottom: 20px;
  font-size: 16px;
}

.login-form input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 16px;
}

.login-form button {
  width: 100%;
  padding: 10px;
  background-color: #2da1e0;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
}

</style>

