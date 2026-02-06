import request from '@/utils/request'
import type { Result, Product } from '@/types'

export const productApi = {
  // Get all products
  getList(): Promise<Result<Product[]>> {
    return request.get('/product/list')
  },
  
  // Get product by ID
  getById(id: number): Promise<Result<Product>> {
    return request.get(`/product/${id}`)
  },
  
  // Get products by category
  getByCategory(categoryId: number): Promise<Result<Product[]>> {
    return request.get(`/product/category/${categoryId}`)
  },
  
  // Search products
  search(keyword: string): Promise<Result<Product[]>> {
    return request.get('/product/search', { params: { keyword } })
  }
}
