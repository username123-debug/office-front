<template>
  <div class="delete-page">
    <h2>社員情報削除</h2>
    <ul class="employee-list">
      <li v-for="employee in employees" :key="employee.id" class="employee-item">
        <span>{{ employee.name }}（{{ employee.myDepartment }}）</span>
        <button @click="deleteEmployee(employee.id)">削除</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import photo001 from '@/assets/sky.jpg'
import photo002 from '@/assets/summer.jpg'
import photo003 from '@/assets/sun.jpg'
import photo004 from '@/assets/sunflower.jpg'
import photo005 from '@/assets/sunflower2.jpg'
import photo006 from '@/assets/cloud.png'

// 初期データ
const initialEmployees = [
  { id: '001', name: '田中 太郎', myDepartment: '営業部', photo: photo001 },
  { id: '002', name: '山田 花子', myDepartment: '人事部', photo: photo002 },
  { id: '003', name: '佐藤 一郎', myDepartment: 'IT部門', photo: photo003 },
  { id: '004', name: '鈴木 次郎', myDepartment: '財務部', photo: photo004 },
  { id: '005', name: '伊藤 三郎', myDepartment: '生産部門', photo: photo005 },
  { id: '006', name: '高橋 四郎', myDepartment: '営業部', photo: photo006 }
]

// 社員一覧（初期データ＋追加データ）
const employees = ref([])

onMounted(() => {
  const stored = JSON.parse(localStorage.getItem('employees') || '[]')
  employees.value = [...initialEmployees, ...stored]
})

// 削除処理（localStorage にも反映）
const deleteEmployee = (id) => {
  if (confirm('本当に削除しますか？')) {
    // 初期データにあるかどうかチェック（初期データは削除対象外とするならこの処理を変更）
    const isInitial = initialEmployees.some(e => e.id === id)
    if (isInitial) {
      alert('この社員は初期データなので削除できません。')
      return
    }

    // 表示用 employees から削除
    employees.value = employees.value.filter(emp => emp.id !== id)

    // localStorage からも削除
    const stored = JSON.parse(localStorage.getItem('employees') || '[]')
    const updated = stored.filter(emp => emp.id !== id)
    localStorage.setItem('employees', JSON.stringify(updated))
  }
}
</script>

<style scoped>
.delete-page {
  padding: 20px;
}
.employee-list {
  list-style: none;
  padding: 0;
}
.employee-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 8px;
}
button {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background-color: #c0392b;
}
</style>
