import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userApi } from '@/api/user'
import type { User, UserInfo, LoginForm, RegisterForm } from '@/types'
import { ElMessage } from 'element-plus'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<User | null>(null)
  
  const isLogin = ref<boolean>(!!token.value)
  
  // Login
  const login = async (loginForm: LoginForm) => {
    try {
      const res = await userApi.login(loginForm)
      token.value = res.data.token
      userInfo.value = res.data
      isLogin.value = true
      localStorage.setItem('token', token.value)
      ElMessage.success('Login successful')
      router.push('/')
    } catch (error) {
      console.error('Login failed:', error)
    }
  }
  
  // Register
  const register = async (registerForm: RegisterForm) => {
    try {
      await userApi.register(registerForm)
      ElMessage.success('Registration successful, please login')
      router.push('/login')
    } catch (error) {
      console.error('Registration failed:', error)
    }
  }
  
  // Logout
  const logout = async () => {
    try {
      await userApi.logout()
    } catch (error) {
      console.error('Logout failed:', error)
    } finally {
      token.value = ''
      userInfo.value = null
      isLogin.value = false
      localStorage.removeItem('token')
      router.push('/login')
    }
  }
  
  // Get user info
  const getUserInfo = async () => {
    try {
      const res = await userApi.getUserInfo()
      userInfo.value = res.data
    } catch (error) {
      console.error('Get user info failed:', error)
    }
  }
  
  return {
    token,
    userInfo,
    isLogin,
    login,
    register,
    logout,
    getUserInfo
  }
})
