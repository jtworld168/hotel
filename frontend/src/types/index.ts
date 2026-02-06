// API Response Type
export interface Result<T = any> {
  code: number
  message: string
  data: T
}

// User Types
export interface User {
  id: number
  username: string
  nickname?: string
  phone?: string
  email?: string
  avatar?: string
  role: string
  employeeDiscount?: number
}

export interface UserInfo extends User {
  token: string
}

export interface LoginForm {
  username: string
  password: string
}

export interface RegisterForm {
  username: string
  password: string
  nickname?: string
  phone?: string
  email?: string
}

// Product Types
export interface Product {
  id: number
  categoryId: number
  name: string
  description?: string
  image?: string
  originalPrice: number
  currentPrice: number
  stock: number
  sales: number
  status: number
  hasDiscount: boolean
}

// Category Type
export interface Category {
  id: number
  name: string
  parentId: number
  sort: number
  icon?: string
  status: number
}

// Coupon Types
export interface Coupon {
  id: number
  name: string
  type: string
  conditionAmount: number
  discountAmount?: number
  discountRate?: number
  totalCount: number
  receivedCount: number
  startTime: string
  endTime: string
  status: number
}

export interface UserCoupon {
  id: number
  userId: number
  couponId: number
  status: number
  useTime?: string
  orderId?: number
  createTime: string
}

// Order Types
export interface Order {
  id: number
  orderNo: string
  userId: number
  totalAmount: number
  discountAmount: number
  couponAmount: number
  payAmount: number
  payType: string
  payTime?: string
  status: number
  userCouponId?: number
  remark?: string
  createTime: string
}

export interface CreateOrderForm {
  productIds: number[]
  quantities: number[]
  userCouponId?: number
  payType: string
  remark?: string
}

// Cart Types
export interface CartItem {
  id: number
  productId: number
  product?: Product
  quantity: number
}
