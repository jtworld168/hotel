import request from '@/utils/request'
import type { Result, LoginForm, RegisterForm, User, UserInfo } from '@/types'

export const userApi = {
  // User registration
  register(data: RegisterForm): Promise<Result<User>> {
    return request.post('/user/register', data)
  },
  
  // User login
  login(data: LoginForm): Promise<Result<UserInfo>> {
    return request.post('/user/login', data)
  },
  
  // User logout
  logout(): Promise<Result<void>> {
    return request.post('/user/logout')
  },
  
  // Get current user info
  getUserInfo(): Promise<Result<User>> {
    return request.get('/user/info')
  }
}
