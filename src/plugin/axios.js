import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080', // バックエンドのドメイン.
  withCredentials: true, // Cookieでの通信を可能にするコード。Spring Securityでは絶対に必要.
});

export default api;
