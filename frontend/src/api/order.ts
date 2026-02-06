import request from '@/utils/request'
import type { Result, Order, CreateOrderForm } from '@/types'

export const orderApi = {
  // Create order
  create(data: CreateOrderForm): Promise<Result<Order>> {
    return request.post('/order/create', data)
  },
  
  // Pay order
  pay(orderId: number): Promise<Result<string>> {
    return request.post(`/order/pay/${orderId}`)
  },
  
  // Get user's orders
  getMyOrders(status?: number): Promise<Result<Order[]>> {
    return request.get('/order/my', { params: { status } })
  },
  
  // Get order detail
  getDetail(orderId: number): Promise<Result<Order>> {
    return request.get(`/order/${orderId}`)
  },
  
  // Cancel order
  cancel(orderId: number): Promise<Result<void>> {
    return request.post(`/order/cancel/${orderId}`)
  }
}
