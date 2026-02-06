<template>
  <div class="register-page">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <h2>Register</h2>
        </div>
      </template>
      
      <el-form
        :model="registerForm"
        :rules="rules"
        ref="formRef"
        label-width="80px"
      >
        <el-form-item label="Username" prop="username">
          <el-input v-model="registerForm.username" placeholder="Please enter username" />
        </el-form-item>
        
        <el-form-item label="Password" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="Please enter password"
          />
        </el-form-item>
        
        <el-form-item label="Nickname" prop="nickname">
          <el-input v-model="registerForm.nickname" placeholder="Please enter nickname (optional)" />
        </el-form-item>
        
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="Please enter phone (optional)" />
        </el-form-item>
        
        <el-form-item label="Email" prop="email">
          <el-input v-model="registerForm.email" placeholder="Please enter email (optional)" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" style="width: 100%">
            Register
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <el-button @click="router.push('/login')" style="width: 100%">
            Back to Login
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { RegisterForm } from '@/types'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const formRef = ref<FormInstance>()

const registerForm = reactive<RegisterForm>({
  username: '',
  password: '',
  nickname: '',
  phone: '',
  email: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: 'Please enter username', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please enter password', trigger: 'blur' },
    { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.register(registerForm)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-card {
  width: 400px;
}

.card-header h2 {
  margin: 0;
  text-align: center;
}
</style>
