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
          </div>
          <FullCalendar :key="currentFirstDay.getTime()" :options="calendarOptions" />
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
              <router-link to="/overview" class="more-link">もっと見る</router-link>
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
import { ref, computed, watchEffect } from 'vue'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import { mockSchedules } from '@/mock/schedules'
import { mockNotices } from '@/mock/notices'

const currentFirstDay = ref(getTodayStart())
const calendarOptions = ref({})
const weekTitle = ref('')

watchEffect(() => {
  const start = new Date(currentFirstDay.value)
  const end = new Date(start)
  end.setDate(start.getDate() + 7)

  weekTitle.value = formatRange(start, end)

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
    events: mockSchedules
      .filter(s => {
        const d = new Date(s.date_time_start)
        return s.created_by === 'self' && d >= start && d < end
      })
      .map(s => ({
        id: s.id.toString(),
        title: s.title,
        start: s.date_time_start,
        end: s.date_time_end
      })),
    eventContent: arg => {
      const d = new Date(arg.event.start)
      const hh = String(d.getHours()).padStart(2, '0')
      const mm = String(d.getMinutes()).padStart(2, '0')
      return { html: `<div style='font-size:0.85rem'>${hh}:${mm}<br/>${arg.event.title}</div>` }
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

const todayStart = getTodayStart()
const tomorrowStart = new Date(todayStart)
tomorrowStart.setDate(tomorrowStart.getDate() + 1)

const todaySchedules = computed(() =>
  mockSchedules
    .filter(s => {
      const d = new Date(s.date_time_start)
      return s.created_by === 'self' && d >= todayStart && d < tomorrowStart
    })
    .sort((a, b) => new Date(a.date_time_start) - new Date(b.date_time_start))
    .map(s => {
      const d = new Date(s.date_time_start)
      const hh = String(d.getHours()).padStart(2, '0')
      const mm = String(d.getMinutes()).padStart(2, '0')
      return { id: s.id, title: s.title, time: `${hh}:${mm}` }
    })
)

const newestNotices = computed(() =>
  mockNotices
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
}

.today-schedule h2,
.notice-section h2 {
  margin-bottom: 12px;
  font-size: 1.1rem;
  border-left: 4px solid #3c82f6;
  padding-left: 8px;
  color: #3c82f6;
}

.today-schedule ul,
.notice-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.today-schedule li,
.notice-section li {
  margin-bottom: 8px;
  font-size: 0.95rem;
}

.date {
  margin-left: 8px;
  color: #888;
  font-size: 0.85rem;
}

.notice-section a,
.more-link {
  color: #3c82f6;
  font-size: 0.9rem;
  text-decoration: none;
}

.notice-section a:hover,
.more-link:hover {
  text-decoration: underline;
}

.more-link-wrapper {
  margin-top: 4px;
  text-align: left;
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
</style>