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

<script>
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import { mockSchedules } from '../../mock/schedules'

export default {
  name: 'ScheduleOverview',
  components: { FullCalendar },
  data() {
    const today = new Date()
    today.setHours(0, 0, 0, 0)
    return {
      keyword: this.$route.query.keyword || '',
      currentWeekStart: today,
      employees: [
        { id: 'self', name: '自分' },
        { id: 'a', name: 'A' },
        { id: 'b', name: 'B' }
      ],
      calendarOptionsMap: {},
      weekTitle: ''
    }
  },
  computed: {
    filteredEmployees() {
      return this.employees.filter(e => !this.keyword || e.name.includes(this.keyword))
    }
  },
  watch: {
    currentWeekStart: {
      immediate: true,
      handler() {
        this.updateAllCalendars()
        this.weekTitle = this.formatWeekRange(this.currentWeekStart)
      }
    }
  },
  methods: {
    updateAllCalendars() {
      const newMap = {}
      this.employees.forEach(emp => {
        newMap[emp.id] = this.getCalendarOptions(emp.id)
      })
      this.calendarOptionsMap = newMap
    },
    getEventsFor(userId) {
      const start = new Date(this.currentWeekStart)
      const end = new Date(start)
      end.setDate(start.getDate() + 7)
      return mockSchedules
        .filter(s => {
          const d = new Date(s.date_time_start)
          return s.created_by === userId && d >= start && d < end
        })
        .map(s => ({
          id: s.id.toString(),
          title: s.title,
          start: s.date_time_start,
          end: s.date_time_end
        }))
    },
    getCalendarOptions(userId) {
      return {
        plugins: [dayGridPlugin],
        initialView: 'dayGridWeek',
        locale: 'ja',
        headerToolbar: false,
        height: 'auto',
        contentHeight: 300,
        expandRows: true,
        aspectRatio: 2.5,
        initialDate: this.currentWeekStart,
        firstDay: this.currentWeekStart.getDay(),
        events: this.getEventsFor(userId),
        dayCellDidMount: arg => {
          const day = arg.date.getDay()
          if (day === 0) arg.el.classList.add('fc-sunday-bg')
          if (day === 6) arg.el.classList.add('fc-saturday-bg')
        },
        eventClick: info => {
          this.$router.push(`/schedule/${info.event.id}`)
        },
        eventContent: arg => {
          const d = new Date(arg.event.start)
          const hh = String(d.getHours()).padStart(2, '0')
          const mm = String(d.getMinutes()).padStart(2, '0')
          return {
            html: `<div style='font-size:0.85rem'>${hh}:${mm}<br/>${arg.event.title}</div>`
          }
        }
      }
    },
    changeWeek(deltaDays) {
      const t = new Date(this.currentWeekStart)
      if (deltaDays === -7) {
        const day = t.getDay()
        t.setDate(t.getDate() - (day === 0 ? 7 : day))
      } else if (deltaDays === 7) {
        const day = t.getDay()
        t.setDate(t.getDate() + (day === 0 ? 7 : 7 - day))
      } else {
        t.setDate(t.getDate() + deltaDays)
      }
      this.currentWeekStart = t
    },
    goThisWeek() {
      const today = new Date()
      today.setHours(0, 0, 0, 0)
      this.currentWeekStart = today
    },
    formatWeekRange(date) {
      const s = new Date(date)
      const e = new Date(s)
      e.setDate(s.getDate() + 6)
      return `${s.getFullYear()}/${s.getMonth() + 1}/${s.getDate()} - ${e.getMonth() + 1}/${e.getDate()}`
    },
    goToCalendar(userId) {
      this.$router.push(`/calendar/${userId}`)
    }
  }
}
</script>

<style>
.overview-container {
  max-width: 1300px;
  margin: 0 auto;
  padding: 20px;
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
  min-width: 800px;
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
</style>
