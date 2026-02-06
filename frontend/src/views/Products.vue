<template>
  <div class="products">
    <div class="page-header">
      <h1>Products</h1>
      <el-input
        v-model="searchKeyword"
        placeholder="Search products..."
        :prefix-icon="Search"
        @keyup.enter="handleSearch"
        style="width: 300px"
      >
        <template #append>
          <el-button :icon="Search" @click="handleSearch" />
        </template>
      </el-input>
    </div>
    
    <div v-loading="productStore.loading">
      <el-empty v-if="productStore.products.length === 0" description="No products found" />
      
      <el-row :gutter="20" v-else>
        <el-col :span="6" v-for="product in productStore.products" :key="product.id">
          <el-card class="product-card" shadow="hover">
            <div class="product-image">
              <el-image
                :src="product.image || '/placeholder.png'"
                fit="cover"
                style="width: 100%; height: 200px"
              />
              <el-tag v-if="product.hasDiscount" type="success" class="discount-tag">
                Employee Discount
              </el-tag>
            </div>
            
            <div class="product-info">
              <h3>{{ product.name }}</h3>
              <p class="description">{{ product.description }}</p>
              
              <div class="price-info">
                <span class="current-price">¥{{ product.currentPrice.toFixed(2) }}</span>
                <span v-if="product.hasDiscount" class="original-price">
                  ¥{{ product.originalPrice.toFixed(2) }}
                </span>
              </div>
              
              <div class="product-footer">
                <span class="stock">Stock: {{ product.stock }}</span>
                <span class="sales">Sales: {{ product.sales }}</span>
              </div>
              
              <div class="actions">
                <el-button @click="router.push(`/product/${product.id}`)">
                  View Details
                </el-button>
                <el-button type="primary" @click="addToCart(product)">
                  Add to Cart
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useProductStore } from '@/stores/product'
import { useCartStore } from '@/stores/cart'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { Product } from '@/types'

const router = useRouter()
const productStore = useProductStore()
const cartStore = useCartStore()
const searchKeyword = ref('')

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    productStore.searchProducts(searchKeyword.value.trim())
  } else {
    productStore.getProducts()
  }
}

const addToCart = (product: Product) => {
  if (product.stock <= 0) {
    ElMessage.warning('Product out of stock')
    return
  }
  
  cartStore.addToCart(product, 1)
  ElMessage.success('Added to cart')
}

onMounted(() => {
  productStore.getProducts()
})
</script>

<style scoped>
.products {
  padding: 20px 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.product-card {
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-image {
  position: relative;
}

.discount-tag {
  position: absolute;
  top: 10px;
  right: 10px;
}

.product-info {
  padding: 15px 0;
}

.product-info h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.description {
  color: #666;
  font-size: 14px;
  margin: 0 0 15px 0;
  height: 40px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.price-info {
  margin-bottom: 10px;
}

.current-price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
  margin-right: 10px;
}

.original-price {
  font-size: 16px;
  color: #999;
  text-decoration: line-through;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  font-size: 14px;
  color: #666;
}

.actions {
  display: flex;
  gap: 10px;
}

.actions .el-button {
  flex: 1;
}
</style>
