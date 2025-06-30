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
  
    </div>
  
    <form class="login-form" @submit.prevent="login">
      <img src="@/assets/logo.png" alt="Cybozu Logo" class="logo" />
      <p>username:<input v-model="username" /></p>
      <p>password:<input v-model="password" type="password" /></p>
      <button type="submit">Submit</button>
    </form>
  </div>
</template>
<style scoped>

.login-hero {

  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;


  background-image: url('@/assets/man hinh.jpeg');
  background-position: center center;
  background-size: cover;
  background-repeat: no-repeat;


  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}


.logo {
  display: block;
  margin: 0 auto 20px;
  width: 150px; /* 必要に応じて調整 */
  height: auto;
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
  width: 420px;
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

