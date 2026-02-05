import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { CartItem, Product } from '@/types'

export const useCartStore = defineStore('cart', () => {
  const cartItems = ref<CartItem[]>([])
  
  // Add to cart
  const addToCart = (product: Product, quantity: number = 1) => {
    const existingItem = cartItems.value.find(item => item.productId === product.id)
    
    if (existingItem) {
      existingItem.quantity += quantity
    } else {
      cartItems.value.push({
        id: Date.now(),
        productId: product.id,
        product: product,
        quantity: quantity
      })
    }
    
    saveToLocalStorage()
  }
  
  // Remove from cart
  const removeFromCart = (productId: number) => {
    const index = cartItems.value.findIndex(item => item.productId === productId)
    if (index !== -1) {
      cartItems.value.splice(index, 1)
      saveToLocalStorage()
    }
  }
  
  // Update quantity
  const updateQuantity = (productId: number, quantity: number) => {
    const item = cartItems.value.find(item => item.productId === productId)
    if (item) {
      item.quantity = quantity
      saveToLocalStorage()
    }
  }
  
  // Clear cart
  const clearCart = () => {
    cartItems.value = []
    saveToLocalStorage()
  }
  
  // Total amount
  const totalAmount = computed(() => {
    return cartItems.value.reduce((total, item) => {
      return total + (item.product?.currentPrice || 0) * item.quantity
    }, 0)
  })
  
  // Total items
  const totalItems = computed(() => {
    return cartItems.value.reduce((total, item) => total + item.quantity, 0)
  })
  
  // Save to localStorage
  const saveToLocalStorage = () => {
    localStorage.setItem('cart', JSON.stringify(cartItems.value))
  }
  
  // Load from localStorage
  const loadFromLocalStorage = () => {
    const saved = localStorage.getItem('cart')
    if (saved) {
      cartItems.value = JSON.parse(saved)
    }
  }
  
  // Initialize
  loadFromLocalStorage()
  
  return {
    cartItems,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    totalAmount,
    totalItems
  }
})
