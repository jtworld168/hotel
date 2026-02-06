import request from '@/utils/request'
import type { Result, Coupon, UserCoupon } from '@/types'

export const couponApi = {
  // Get available coupons
  getAvailable(): Promise<Result<Coupon[]>> {
    return request.get('/coupon/available')
  },
  
  // Receive a coupon
  receive(couponId: number): Promise<Result<UserCoupon>> {
    return request.post(`/coupon/receive/${couponId}`)
  },
  
  // Get user's coupons
  getUserCoupons(status?: number): Promise<Result<UserCoupon[]>> {
    return request.get('/coupon/my', { params: { status } })
  },
  
  // Get available coupons for order
  getAvailableForOrder(orderAmount: number): Promise<Result<UserCoupon[]>> {
    return request.get('/coupon/available-for-order', { params: { orderAmount } })
  }
}
