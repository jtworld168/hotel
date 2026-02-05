import { defineStore } from 'pinia'
import { ref } from 'vue'
import { productApi } from '@/api/product'
import type { Product } from '@/types'

export const useProductStore = defineStore('product', () => {
  const products = ref<Product[]>([])
  const loading = ref(false)
  
  // Get all products
  const getProducts = async () => {
    loading.value = true
    try {
      const res = await productApi.getList()
      products.value = res.data
    } catch (error) {
      console.error('Get products failed:', error)
    } finally {
      loading.value = false
    }
  }
  
  // Search products
  const searchProducts = async (keyword: string) => {
    loading.value = true
    try {
      const res = await productApi.search(keyword)
      products.value = res.data
    } catch (error) {
      console.error('Search products failed:', error)
    } finally {
      loading.value = false
    }
  }
  
  return {
    products,
    loading,
    getProducts,
    searchProducts
  }
})
