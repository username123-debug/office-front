import { createRouter, createWebHistory } from 'vue-router'
import TopPage from '../views/TopPage.vue'
import axios from 'axios'
axios.defaults.withCredentials = true

// スケジュール
import ScheduleCalendar from '../views/Schedule/ScheduleCalendar.vue'
import ScheduleDetail from '../views/Schedule/ScheduleDetail.vue'
import ScheduleEdit from '../views/Schedule/ScheduleEdit.vue'
import ScheduleAdd from '../views/Schedule/ScheduleAdd.vue'
import ScheduleOverview from '../views/Schedule/ScheduleOverview.vue'

// お知らせ
const NoticeView = () => import('../views/Notice/NoticeView.vue')
const NoticeDetailView = () => import('../views/Notice/NoticeDetailView.vue')
const NoticeAddView = () => import('../views/Notice/NoticeAddView.vue')
import EditNotice from '../views/Notice/NoticeEditView.vue';

//お知らせ
// import NoticeView from '../views/Notice/NoticeView.vue';
// import NoticeView from '../views/Notice/NoticeView.vue';
// import NoticeView from '../views/Notice/NoticeView.vue';

// 社員紹介
import InforList from '../views/Introduce/InforList.vue'
import InforDetail from '../views/Introduce/InforDetail.vue'
import InforEdit from '../views/Introduce/InforEdit.vue'
import InforAdd from '../views/Introduce/InforAdd.vue'
import InforDelete from '../views/Introduce/InforDelete.vue'
import LoginView from '@/views/Login/LoginView.vue'


const routes = [
  //ログイン
  { path: '/', name: 'Login', component: LoginView },
  // トップページ(ROLE_USER用)
  { path: '/top', name: 'TopPage', component: TopPage },
  //ROLE_ADMIN用のトップページ
  { path: '/admin/home', name: 'HomePage', component: TopPage},

  // スケジュール
  { path: '/schedule', name: 'Overview', component: ScheduleOverview },
  { path: '/schedule/add', name: 'ScheduleAdd', component: ScheduleAdd },
  { path: '/schedule/:id/edit', name: 'ScheduleEdit', component: ScheduleEdit },
  { path: '/schedule/:id', name: 'ScheduleDetail', component: ScheduleDetail },
  { path: '/calendar/:userId', name: 'ScheduleCalendar', component: ScheduleCalendar },

  // お知らせ
  { path: '/notice', name: 'NoticeView', component: NoticeView },
  { path: '/notice/:id', name: 'NoticeDetailView', component: NoticeDetailView, props:true },
  { path: '/detail/:id', name: 'NoticeDetail', component: NoticeDetailView, props:true },
  { path: '/addnotice', name: 'NoticeAdd', component: NoticeAddView },
  { path: '/editnotice/:id', name: 'EditNotice', component: EditNotice},

  // 社員紹介
  { path: '/introduce', name: 'IntroduceList', component: InforList },
  { path: '/introduce/detail/:id', name: 'IntroduceDetail', component: InforDetail },
  { path: '/introduce/edit/:id', name: 'Edit', component: InforEdit },
  { path: '/introduce/add', name: 'IntroduceAdd', component: InforAdd },
  { path: '/introduce/delete', name: 'IntroduceDelete', component: InforDelete }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
