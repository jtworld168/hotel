<template>
  <div class="home">
    <el-carousel height="300px" class="banner">
      <el-carousel-item>
        <div class="banner-item banner-1">
          <h1>Welcome to Hotel Vending System</h1>
          <p>24/7 Unmanned Shopping Experience</p>
        </div>
      </el-carousel-item>
      <el-carousel-item>
        <div class="banner-item banner-2">
          <h1>Employee Special Discounts</h1>
          <p>Exclusive pricing for hotel staff</p>
        </div>
      </el-carousel-item>
      <el-carousel-item>
        <div class="banner-item banner-3">
          <h1>Electronic Coupons</h1>
          <p>Save more with our coupon system</p>
        </div>
      </el-carousel-item>
    </el-carousel>
    
    <div class="features">
      <h2>Features</h2>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="hover">
            <template #header>
              <el-icon size="40" color="#409eff"><Discount /></el-icon>
            </template>
            <h3>Employee Pricing</h3>
            <p>Hotel employees enjoy special discounts on all products</p>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <template #header>
              <el-icon size="40" color="#67c23a"><Ticket /></el-icon>
            </template>
            <h3>Electronic Coupons</h3>
            <p>Collect and use coupons to save even more</p>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <template #header>
              <el-icon size="40" color="#e6a23c"><WalletFilled /></el-icon>
            </template>
            <h3>Dual Payment</h3>
            <p>Support for both WeChat Pay and Alipay</p>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <div class="action-buttons">
      <el-button type="primary" size="large" @click="router.push('/products')">
        Start Shopping
      </el-button>
      <el-button size="large" @click="router.push('/coupons')" v-if="userStore.isLogin">
        View Coupons
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { Discount, Ticket, WalletFilled } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
</script>

<style scoped>
.home {
  padding: 20px 0;
}

.banner {
  margin-bottom: 40px;
  border-radius: 8px;
  overflow: hidden;
}

.banner-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: white;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.banner-1 {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.banner-2 {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.banner-3 {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.banner-item h1 {
  font-size: 36px;
  margin-bottom: 10px;
}

.banner-item p {
  font-size: 18px;
}

.features {
  margin-bottom: 40px;
}

.features h2 {
  text-align: center;
  margin-bottom: 30px;
  font-size: 28px;
}

.features h3 {
  text-align: center;
  margin: 15px 0;
}

.features p {
  text-align: center;
  color: #666;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 40px;
}
</style>
