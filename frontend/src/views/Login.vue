<template>
  <div class="login-page">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>Login</h2>
        </div>
      </template>
      
      <el-form
        :model="loginForm"
        :rules="rules"
        ref="formRef"
        label-width="80px"
      >
        <el-form-item label="Username" prop="username">
          <el-input v-model="loginForm.username" placeholder="Please enter username" />
        </el-form-item>
        
        <el-form-item label="Password" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="Please enter password"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">
            Login
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <el-button @click="router.push('/register')" style="width: 100%">
            Register
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="demo-accounts">
        <el-divider>Demo Accounts</el-divider>
        <p><strong>Admin:</strong> admin / 123456</p>
        <p><strong>Employee:</strong> employee1 / 123456 (with discount)</p>
        <p><strong>Customer:</strong> customer1 / 123456</p>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { LoginForm } from '@/types'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const formRef = ref<FormInstance>()

const loginForm = reactive<LoginForm>({
  username: '',
  password: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: 'Please enter username', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please enter password', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.login(loginForm)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
}

.card-header h2 {
  margin: 0;
  text-align: center;
}

.demo-accounts {
  margin-top: 20px;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.demo-accounts p {
  margin: 5px 0;
  font-size: 14px;
}
</style>
