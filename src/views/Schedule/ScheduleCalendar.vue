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
    <FullCalendar
      ref="calendarRef"
      :options="calendarOptions"
      :events="events"
    />
  </div>
</template>

<script>
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import { mockSchedules } from '../../mock/schedules'

export default {
  components: { FullCalendar },
  data() {
    return {
      currentYear: '',
      currentMonth: '',
      events: [],
      calendarOptions: {
        plugins: [dayGridPlugin],
        initialView: 'dayGridMonth',
        locale: 'ja',
        headerToolbar: false,
        height: 'auto',
        expandRows: true,
        datesSet: this.handleDateChange,
        eventClick: info => {
          this.$router.push(`/schedule/${info.event.id}`)
        },
        eventContent: arg => {
          const d = new Date(arg.event.start)
          const hh = String(d.getHours()).padStart(2, '0')
          const mm = String(d.getMinutes()).padStart(2, '0')
          return { html: `<span>${hh}:${mm} ${arg.event.title}</span>` }
        }
      }
    }
  },
  mounted() {
    const userId = this.$route.params.userId
    const list = userId
      ? mockSchedules.filter(s => s.created_by === userId)
      : mockSchedules
    this.events = list.map(item => ({
      id:    item.id.toString(),
      title: item.title,
      start: item.date_time_start,
      end:   item.date_time_end
    }))
    this.$nextTick(() => {
      const api = this.$refs.calendarRef.getApi()
      api.setOption('events', this.events)
    })
  },
  methods: {
    handleDateChange(arg) {
      const d = arg.view.currentStart
      this.currentYear = d.getFullYear()
      this.currentMonth = d.getMonth() + 1
    },
    goPrev() {
      this.$refs.calendarRef.getApi().prev()
    },
    goToday() {
      this.$refs.calendarRef.getApi().today()
    },
    goNext() {
      this.$refs.calendarRef.getApi().next()
    }
  }
}
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
.fc-event {
  cursor: pointer;
}
.fc-day-sun {
  background-color: #ffe5e5;
}
.fc-day-sat {
  background-color: #e5f0ff;
}
</style>
