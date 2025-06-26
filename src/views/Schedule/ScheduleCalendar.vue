<template>
  <div class="calendar-container">
    <div class="calendar-header">
      <div class="calendar-title">{{ currentYear }}年{{ currentMonth }}月</div>
      <div class="calendar-buttons">
        <button @click="goPrev">< 前月</button>
        <button @click="goToday">今月</button>
        <button @click="goNext">翌月 ></button>
      </div>
    </div>
    <div class="calendar-wrapper">
      <FullCalendar
        ref="calendarRef"
        :options="calendarOptions"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'

const route = useRoute()
const router = useRouter()

const calendarRef = ref(null)
const currentYear = ref('')
const currentMonth = ref('')
const events = ref([])

function handleDateChange(arg) {
  const d = arg.view.currentStart
  currentYear.value = d.getFullYear()
  currentMonth.value = d.getMonth() + 1
}

function goPrev() {
  calendarRef.value?.getApi().prev()
}

function goToday() {
  calendarRef.value?.getApi().today()
}

function goNext() {
  calendarRef.value?.getApi().next()
}

const calendarOptions = reactive({
  plugins: [dayGridPlugin],
  initialView: 'dayGridMonth',
  locale: 'ja',
  headerToolbar: false,
  height: 'auto',
  expandRows: true,
  dayCellDidMount: arg => {
    const day = arg.date.getDay()
    if (day === 0) {
      arg.el.querySelector('.fc-daygrid-day-frame')?.classList.add('fc-sunday-bg')
    }
    if (day === 6) {
      arg.el.querySelector('.fc-daygrid-day-frame')?.classList.add('fc-saturday-bg')
    }
  },
  datesSet: handleDateChange,
  eventClick: info => {
    router.push(`/schedule/${info.event.id}`)
  },
  eventContent: arg => {
    const d = new Date(arg.event.start)
    const hh = String(d.getHours()).padStart(2, '0')
    const mm = String(d.getMinutes()).padStart(2, '0')
    return {
      html: `<div class='fc-custom-event'>${hh}:${mm}<br>${arg.event.title}</div>`
    }
  },
  events: []
})

onMounted(async () => {
  const userId = Number(route.params.userId)
  try {
    const response = await axios.get('http://localhost:8080/schedules')

    if (!Array.isArray(response.data)) {
      console.error('取得したデータは配列ではありません:', response.data)
      return
    }

    events.value = response.data
      .filter(item =>
        item.createdUserId === userId ||
        (item.participants || []).some(p =>
          typeof p === 'number' ? p === userId :
          typeof p === 'object' ? p.id === userId : false
        )
      )
      .map(item => ({
        id: item.id.toString(),
        title: item.title,
        start: item.startDateTime,
        end: item.endDateTime
      }))

    nextTick(() => {
      calendarRef.value?.getApi().setOption('events', events.value)
    })
  } catch (error) {
    console.error('スケジュールの取得に失敗しました', error)
  }
})
</script>

<style>
.calendar-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px;
}
.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.calendar-title {
  font-size: 20px;
  font-weight: bold;
  flex: 1;
  text-align: center;
}
.calendar-buttons {
  display: flex;
  gap: 8px;
}
.calendar-buttons button {
  background-color: #2c3e50;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 6px 12px;
  font-size: 14px;
  cursor: pointer;
}
.calendar-wrapper {
  overflow-x: auto;
  min-width: 900px;
}
.fc-event {
  cursor: pointer;
}
.fc-sunday-bg {
  background-color: #ffe5e5 !important;
}
.fc-saturday-bg {
  background-color: #e5f0ff !important;
}
.fc-custom-event {
  font-size: 0.8rem;
  line-height: 1.2;
  white-space: normal;
  word-break: break-word;
}
</style>
