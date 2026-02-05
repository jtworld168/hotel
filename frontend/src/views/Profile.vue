<template>
  <div class="profile">
    <h1>Profile</h1>
    
    <el-card v-if="userStore.userInfo">
      <div class="profile-info">
        <el-avatar :size="100" :src="userStore.userInfo.avatar">
          {{ userStore.userInfo.nickname?.charAt(0) || userStore.userInfo.username.charAt(0) }}
        </el-avatar>
        
        <div class="info-section">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="Username">
              {{ userStore.userInfo.username }}
            </el-descriptions-item>
            <el-descriptions-item label="Nickname">
              {{ userStore.userInfo.nickname || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="Phone">
              {{ userStore.userInfo.phone || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="Email">
              {{ userStore.userInfo.email || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="Role">
              <el-tag :type="getRoleTag(userStore.userInfo.role)">
                {{ getRoleText(userStore.userInfo.role) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="Employee Discount" v-if="userStore.userInfo.role === 'employee'">
              <el-tag type="success">
                {{ ((1 - (userStore.userInfo.employeeDiscount || 1)) * 100).toFixed(0) }}% OFF
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const getRoleTag = (role: string) => {
  const tags: Record<string, string> = {
    'admin': 'danger',
    'employee': 'success',
    'customer': 'info'
  }
  return tags[role] || 'info'
}

const getRoleText = (role: string) => {
  const texts: Record<string, string> = {
    'admin': 'Administrator',
    'employee': 'Employee',
    'customer': 'Customer'
  }
  return texts[role] || role
}

onMounted(() => {
  if (!userStore.userInfo) {
    userStore.getUserInfo()
  }
})
</script>

<style scoped>
.profile {
  padding: 20px 0;
}

h1 {
  margin-bottom: 30px;
}

.profile-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
}

.info-section {
  width: 100%;
}
</style>
