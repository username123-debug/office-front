<template>
  <div class="overview-container">
    <div class="week-nav">
      <div class="week-buttons">
        <button class="nav-button double-left" @click="changeWeek(-7)">≪ 前週</button>
        <button class="nav-button single-left" @click="changeWeek(-1)">＜ 前日</button>
        <button class="nav-button today" @click="goThisWeek">今日</button>
        <button class="nav-button single-right" @click="changeWeek(1)">翌日 ＞</button>
        <button class="nav-button double-right" @click="changeWeek(7)">翌週 ≫</button>
      </div>
      <div class="week-title">{{ weekTitle }}</div>
    </div>

    <div v-for="employee in filteredEmployees" :key="employee.id" class="employee-calendar-box">
      <h3 class="employee-name" @click="goToCalendar(employee.id)">
        {{ employee.name.replace('社員', '') }}
      </h3>
      <div class="calendar-wrapper">
        <FullCalendar
          ref="calendars"
          :key="`cal-${employee.id}-${currentWeekStart.getTime()}`"
          :options="calendarOptionsMap[employee.id]"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'

const router = useRouter()
const route = useRoute()

const keyword = ref('')

watch(
  () => route.query.keyword,
  newKeyword => {
    keyword.value = newKeyword || ''
  },
  { immediate: true }
)

const today = new Date()
today.setHours(0, 0, 0, 0)
const currentWeekStart = ref(today)

const weekTitle = ref('')
const employees = ref([])
const calendarOptionsMap = ref({})
const scheduleData = ref([])

const getData = async () => {
  const [userRes, scheduleRes] = await Promise.all([
    axios.get('http://localhost:8080/users/abstract'),
    axios.get('http://localhost:8080/schedules')
  ])

  employees.value = Object.entries(userRes.data).map(([id, name]) => ({
    id: Number(id),
    name
  }))

  scheduleData.value = scheduleRes.data
  updateAllCalendars()
}

onMounted(() => {
  getData()
})

const filteredEmployees = computed(() => {
  const lowerKeyword = keyword.value.toLowerCase()
  return employees.value.filter(e =>
    !lowerKeyword || e.name.toLowerCase().includes(lowerKeyword)
  )
})

watch(currentWeekStart, () => {
  updateAllCalendars()
  weekTitle.value = formatWeekRange(currentWeekStart.value)
}, { immediate: true })

function updateAllCalendars() {
  const newMap = {}
  employees.value.forEach(emp => {
    newMap[emp.id] = getCalendarOptions(emp.id)
  })
  calendarOptionsMap.value = newMap
}

function getEventsFor(userId) {
  const start = new Date(currentWeekStart.value)
  const end = new Date(start)
  end.setDate(start.getDate() + 7)

  return scheduleData.value
    .filter(s => {
      const dStart = new Date(s.startDateTime)
      const dEnd = new Date(s.endDateTime)
      return (s.createdUserId === userId || (s.participants || []).includes(userId)) &&
             dEnd > start && dStart < end
    })
    .map(s => ({
      id: s.id.toString(),
      title: s.title,
      start: s.startDateTime,
      end: s.endDateTime
    }))
}

function getCalendarOptions(userId) {
  return {
    plugins: [dayGridPlugin],
    initialView: 'dayGridWeek',
    locale: 'ja',
    headerToolbar: false,
    height: 'auto',
    contentHeight: 300,
    expandRows: true,
    aspectRatio: 2.5,
    initialDate: currentWeekStart.value,
    firstDay: currentWeekStart.value.getDay(),
    events: getEventsFor(userId),
    dayCellDidMount: arg => {
      const day = arg.date.getDay()
      if (day === 0) arg.el.classList.add('fc-sunday-bg')
      if (day === 6) arg.el.classList.add('fc-saturday-bg')
    },
    eventClick: info => {
      router.push(`/schedule/${info.event.id}`)
    },
    eventContent: arg => {
      const d = new Date(arg.event.start)
      const hh = String(d.getHours()).padStart(2, '0')
      const mm = String(d.getMinutes()).padStart(2, '0')
      return {
        html: `<div class="fc-custom-event">${hh}:${mm}<br/>${arg.event.title}</div>`
      }
    }
  }
}

function changeWeek(deltaDays) {
  const t = new Date(currentWeekStart.value)
  if (deltaDays === -7) {
    const day = t.getDay()
    t.setDate(t.getDate() - (day === 0 ? 7 : day))
  } else if (deltaDays === 7) {
    const day = t.getDay()
    t.setDate(t.getDate() + (day === 0 ? 7 : 7 - day))
  } else {
    t.setDate(t.getDate() + deltaDays)
  }
  currentWeekStart.value = t
}

function goThisWeek() {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  currentWeekStart.value = today
}

function formatWeekRange(date) {
  const s = new Date(date)
  const e = new Date(s)
  e.setDate(s.getDate() + 6)
  return `${s.getFullYear()}/${s.getMonth() + 1}/${s.getDate()} - ${e.getMonth() + 1}/${e.getDate()}`
}

function goToCalendar(userId) {
  router.push(`/calendar/${userId}`)
}
</script>

<style>
.overview-container {
  position: absolute;
  top: 60px;
  left: 260px;
  right: 0;
  bottom: 0;
  overflow-y: auto;
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
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 24px;
  color: #2c3e50;
}
.employee-calendar-box {
  border: 1px solid #ccc;
  border-radius: 8px;
  margin-bottom: 24px;
  padding: 16px;
  width: 100%;
  box-sizing: border-box;
  background-color: #fdfdfd;
}

.employee-name {
  margin-bottom: 8px;
  font-weight: bold;
  color: #3366cc;
  cursor: pointer;
}
.calendar-wrapper {
  background-color: white;
  padding: 6px;
  border-radius: 4px;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.05);
  overflow-x: auto;
}

.main-content {
  flex: 1;
  padding: 20px;
  min-width: 900px;
}
.fc-sunday-bg {
  background-color: #ffe5e5 !important;
}
.fc-saturday-bg {
  background-color: #e5f0ff !important;
}
/* イベントタイトル */
.fc-custom-event {
  font-size: 0.85rem;
  line-height: 1.3;
  color: #1f6feb;
  text-decoration: underline;
  font-weight: 500;
  padding-left: 2px;
}

.fc .fc-col-header-cell-cushion {
  color: #2c3e50 !important;
}

.fc .fc-daygrid-day-number {
  color: #2c3e50 !important;
}

.fc .fc-day-today {
  background-color: #fffce0 !important;
}

.fc-event:hover {
  background-color: #f0f4fa;
}


</style>
