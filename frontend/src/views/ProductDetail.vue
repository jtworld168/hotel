<template>
  <div class="product-detail" v-loading="loading">
    <el-card v-if="product">
      <el-row :gutter="40">
        <el-col :span="10">
          <el-image
            :src="product.image || '/placeholder.png'"
            fit="cover"
            style="width: 100%"
          />
        </el-col>
        
        <el-col :span="14">
          <div class="detail-info">
            <h1>{{ product.name }}</h1>
            
            <el-tag v-if="product.hasDiscount" type="success" size="large">
              Employee Discount Applied
            </el-tag>
            
            <div class="price-section">
              <div class="current-price">¥{{ product.currentPrice.toFixed(2) }}</div>
              <div v-if="product.hasDiscount" class="original-price">
                Original: ¥{{ product.originalPrice.toFixed(2) }}
              </div>
            </div>
            
            <el-divider />
            
            <div class="product-stats">
              <div class="stat-item">
                <span class="label">Stock:</span>
                <span class="value">{{ product.stock }}</span>
              </div>
              <div class="stat-item">
                <span class="label">Sales:</span>
                <span class="value">{{ product.sales }}</span>
              </div>
              <div class="stat-item">
                <span class="label">Category ID:</span>
                <span class="value">{{ product.categoryId }}</span>
              </div>
            </div>
            
            <el-divider />
            
            <div class="description">
              <h3>Product Description</h3>
              <p>{{ product.description || 'No description available' }}</p>
            </div>
            
            <el-divider />
            
            <div class="quantity-section">
              <span class="label">Quantity:</span>
              <el-input-number
                v-model="quantity"
                :min="1"
                :max="product.stock"
              />
            </div>
            
            <div class="actions">
              <el-button
                type="primary"
                size="large"
                @click="addToCart"
                :disabled="product.stock <= 0"
              >
                Add to Cart
              </el-button>
              <el-button size="large" @click="router.back()">
                Back
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { productApi } from '@/api/product'
import { ElMessage } from 'element-plus'
import type { Product } from '@/types'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

const loading = ref(false)
const product = ref<Product>()
const quantity = ref(1)

const getProductDetail = async () => {
  loading.value = true
  try {
    const res = await productApi.getById(Number(route.params.id))
    product.value = res.data
  } catch (error) {
    ElMessage.error('Failed to load product')
  } finally {
    loading.value = false
  }
}

const addToCart = () => {
  if (product.value) {
    cartStore.addToCart(product.value, quantity.value)
    ElMessage.success(`Added ${quantity.value} item(s) to cart`)
    quantity.value = 1
  }
}

onMounted(() => {
  getProductDetail()
})
</script>

<style scoped>
.product-detail {
  padding: 20px 0;
}

.detail-info {
  padding: 20px;
}

.detail-info h1 {
  margin: 0 0 20px 0;
  font-size: 32px;
}

.price-section {
  margin: 30px 0;
}

.current-price {
  font-size: 36px;
  color: #f56c6c;
  font-weight: bold;
}

.original-price {
  font-size: 18px;
  color: #999;
  text-decoration: line-through;
  margin-top: 5px;
}

.product-stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  display: flex;
  gap: 10px;
}

.stat-item .label {
  color: #666;
}

.stat-item .value {
  font-weight: bold;
}

.description h3 {
  margin: 0 0 15px 0;
}

.description p {
  color: #666;
  line-height: 1.6;
}

.quantity-section {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 30px;
}

.quantity-section .label {
  font-weight: bold;
}

.actions {
  display: flex;
  gap: 15px;
}
</style>
