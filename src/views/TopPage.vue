<template>
  <div class="container">
    <div class="top-page-wrapper">
      <div>
        <section class="week-schedule">
          <div class="week-nav">
            <div class="week-buttons">
              <button class="nav-button double-left" @click="goPrevSunday">≪ 前週</button>
              <button class="nav-button today" @click="goToday">今日</button>
              <button class="nav-button double-right" @click="goNextSunday">翌週 ≫</button>
            </div>
            <div class="week-title">{{ weekTitle }}</div>
          <div class="calendar-box">
  <FullCalendar :key="currentFirstDay.getTime()" :options="calendarOptions" />
</div>
</div>
        </section>

        <div class="bottom">
          <section class="today-schedule">
            <h2>今日のスケジュール</h2>
            <ul>
              <li v-for="s in todaySchedules" :key="s.id">
                <router-link :to="`/schedule/${s.id}`">
                  • <strong>{{ s.title }}</strong> — {{ s.time }}
                </router-link>
              </li>
              <li v-if="todaySchedules.length === 0">予定はありません。</li>
            </ul>
            <div class="more-link-wrapper">
              <router-link to="/Schedule" class="more-link">もっと見る</router-link>
            </div>
          </section>

          <section class="notice-section">
            <h2>最新のお知らせ</h2>
            <ul>
              <li v-for="n in newestNotices" :key="n.id">
                <router-link :to="`/notice/${n.id}`">{{ n.title }}</router-link>
                <span class="date">({{ formatDate(n.createdAt) }})</span>
              </li>
              <li v-if="newestNotices.length === 0">お知らせはありません。</li>
            </ul>
            <div class="more-link-wrapper">
              <router-link to="/notice" class="more-link">もっと見る</router-link>
            </div>
          </section>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watchEffect, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'

const router = useRouter()
const currentFirstDay = ref(getTodayStart())
const calendarOptions = ref({})
const weekTitle = ref('')
const allSchedules = ref([])

const FIXED_USER_ID = 1

watchEffect(() => {
  const start = new Date(currentFirstDay.value)
  const end = new Date(start)
  end.setDate(start.getDate() + 7)

  weekTitle.value = formatRange(start, end)

  const filteredEvents = allSchedules.value
    .filter(item => {
      const d = new Date(item.startDateTime)
      const isWithinWeek = d >= start && d < end
      const isRelatedToUser =
        item.createdUserId === FIXED_USER_ID ||
        (item.participants || []).some(p =>
          typeof p === 'number' ? p === FIXED_USER_ID :
          typeof p === 'object' ? p.id === FIXED_USER_ID : false
        )
      return isWithinWeek && isRelatedToUser
    })
    .map(item => ({
      id: item.id.toString(),
      title: item.title,
      start: item.startDateTime,
      end: item.endDateTime
    }))

  calendarOptions.value = {
    plugins: [dayGridPlugin],
    initialView: 'dayGridWeek',
    initialDate: start,
    firstDay: start.getDay(),
    locale: 'ja',
    headerToolbar: false,
    height: 'auto',
    dayCellDidMount: arg => {
      const day = arg.date.getDay()
      if (day === 0) arg.el.classList.add('fc-sunday-bg')
      if (day === 6) arg.el.classList.add('fc-saturday-bg')
    },
    events: filteredEvents,
    eventContent: arg => {
      const d = new Date(arg.event.start)
      const hh = String(d.getHours()).padStart(2, '0')
      const mm = String(d.getMinutes()).padStart(2, '0')
      return {
        html: `<div class="fc-custom-event">${hh}:${mm}<br/>${arg.event.title}</div>`
      }
    },
    eventClick: function(info) {
      router.push(`/schedule/${info.event.id}`)
    }
  }
})

function getTodayStart() {
  const d = new Date()
  d.setHours(0, 0, 0, 0)
  return d
}

function goToday() {
  currentFirstDay.value = getTodayStart()
}

function goPrevSunday() {
  const base = new Date(currentFirstDay.value)
  const day = base.getDay()
  base.setDate(base.getDate() - (day === 0 ? 7 : day))
  base.setHours(0, 0, 0, 0)
  currentFirstDay.value = base
}

function goNextSunday() {
  const base = new Date(currentFirstDay.value)
  const day = base.getDay()
  const delta = 7 - day
  base.setDate(base.getDate() + delta)
  base.setHours(0, 0, 0, 0)
  currentFirstDay.value = base
}

function formatRange(start, end) {
  const s = new Date(start)
  const e = new Date(end)
  e.setDate(e.getDate() - 1)
  return `${s.getFullYear()}/${s.getMonth() + 1}/${s.getDate()} - ${e.getMonth() + 1}/${e.getDate()}`
}

// 今日スケジュール抽出用
const todayStart = getTodayStart()
const tomorrowStart = new Date(todayStart)
tomorrowStart.setDate(tomorrowStart.getDate() + 1)

const todaySchedules = computed(() =>
  allSchedules.value
    .filter(item => {
      const d = new Date(item.startDateTime)
      const isToday = d >= todayStart && d < tomorrowStart
      const isRelatedToUser =
        item.createdUserId === FIXED_USER_ID ||
        (item.participants || []).some(p =>
          typeof p === 'number' ? p === FIXED_USER_ID :
          typeof p === 'object' ? p.id === FIXED_USER_ID : false
        )
      return isToday && isRelatedToUser
    })
    .sort((a, b) => new Date(a.startDateTime) - new Date(b.startDateTime))
    .map(item => {
      const d = new Date(item.startDateTime)
      const hh = String(d.getHours()).padStart(2, '0')
      const mm = String(d.getMinutes()).padStart(2, '0')
      return { id: item.id, title: item.title, time: `${hh}:${mm}` }
    })
)

const notices = ref([])

onMounted(async () => {
  try {
    const [scheduleRes, noticeRes] = await Promise.all([
      axios.get('http://localhost:8080/schedules'),
      axios.get('http://localhost:8080/notices')
    ])
    allSchedules.value = scheduleRes.data
    notices.value = noticeRes.data
  } catch (error) {
    console.error('データ取得失敗:', error)
  }
})

const newestNotices = computed(() =>
  notices.value
    .slice()
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    .slice(0, 3)
)

const formatDate = iso => new Date(iso).toLocaleDateString()
</script>



<style scoped>
.container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.top-page-wrapper {
  transform: scale(1.0);
  transform-origin: top left;
  width: 1150px;
  padding: 20px;
  box-sizing: border-box;

  background-image: url('@/assets/schedule/top-bg.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  border-radius: 12px;
}


.week-schedule {
  margin-bottom: 32px;
}

.week-nav {
  margin-bottom: 16px;
}

.week-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 8px;
}

.nav-button {
  background-color: #2c3e50;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 6px 12px;
  font-size: 14px;
  cursor: pointer;
  font-weight: 500;
}
.nav-button:hover {
  background-color: #1a1a1a;
}

.week-title {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 16px;
}

.bottom {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  margin-top: 32px;
}

.today-schedule,
.notice-section {
  flex: 1;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 16px;
}

.today-schedule h2,
.notice-section h2 {
  margin-bottom: 12px;
  font-size: 1.1rem;
  border-left: 4px solid #2c3e50;
  padding-left: 8px;
  color: #2c3e50;
}

.today-schedule ul,
.notice-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.today-schedule li,
.notice-section li {
  margin: 12px 0;
}

.today-schedule ul li a,
.notice-section ul li a {
  color: #2c3e50;
  font-size: 1rem;
  text-decoration: underline;
  font-weight: 500;
}

.date {
  margin-left: 8px;
  color: #888;
  font-size: 0.85rem;
}

.more-link-wrapper {
  margin-top: 8px;
  text-align: left;
}

.more-link {
  color: #1f6feb;
  font-size: 0.9rem;
  text-decoration: none;
  font-weight: 500;
}
.more-link:hover {
  text-decoration: underline;
}

.fc-sunday-bg {
  background-color: #ffe5e5 !important;
}

.fc-saturday-bg {
  background-color: #e5f0ff !important;
}

.fc {
  width: 100% !important;
  table-layout: fixed !important;
}

.fc-scrollgrid,
.fc-scrollgrid-sync-table {
  table-layout: fixed !important;
  width: 100% !important;
}
.calendar-box {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
}
.fc .fc-col-header-cell-cushion {
  color: #2c3e50 !important;
  font-weight: 600;
}
.fc .fc-daygrid-day-number {
  color: #2c3e50 !important;
  font-weight: 500;
}
.fc-custom-event {
  font-size: 0.85rem;
  line-height: 1.3;
  color: #1f6feb;
  text-decoration: underline;
  font-weight: 500;
}
</style>
