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
    if (user.role === "ADMIN" || user.role === "ROLE_ADMIN") {
      // 管理者の場合、管理者用ページへ遷移
      router.push("/admin/home");
      console.log("管理者のページに遷移");
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
  <!-- フォーム送信時に login 関数を呼び出し（ページリロードは防止） -->
  <form @submit.prevent="login">
    <p>username:<input v-model="username" /></p>
    <p>password:<input v-model="password" type="password" /></p>
    <button type="submit">submit</button>
  </form>
</template>
