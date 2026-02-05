<template>
  <div class="layout">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo">
            <el-icon><Shop /></el-icon>
            <span>Hotel Vending</span>
          </div>
          
          <el-menu
            :default-active="activeMenu"
            mode="horizontal"
            :ellipsis="false"
            @select="handleMenuSelect"
          >
            <el-menu-item index="/home">Home</el-menu-item>
            <el-menu-item index="/products">Products</el-menu-item>
            <el-menu-item index="/coupons" v-if="userStore.isLogin">Coupons</el-menu-item>
            <el-menu-item index="/orders" v-if="userStore.isLogin">Orders</el-menu-item>
          </el-menu>
          
          <div class="header-right">
            <el-badge :value="cartStore.totalItems" class="cart-badge" v-if="cartStore.totalItems > 0">
              <el-button @click="router.push('/cart')" :icon="ShoppingCart">Cart</el-button>
            </el-badge>
            <el-button v-else @click="router.push('/cart')" :icon="ShoppingCart">Cart</el-button>
            
            <template v-if="userStore.isLogin">
              <el-dropdown @command="handleCommand">
                <el-button>
                  <el-icon><User /></el-icon>
                  {{ userStore.userInfo?.nickname || userStore.userInfo?.username }}
                  <span v-if="userStore.userInfo?.role === 'employee'" class="employee-badge">Employee</span>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="profile">Profile</el-dropdown-item>
                    <el-dropdown-item command="logout">Logout</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <el-button @click="router.push('/login')">Login</el-button>
              <el-button type="primary" @click="router.push('/register')">Register</el-button>
            </template>
          </div>
        </div>
      </el-header>
      
      <el-main class="main">
        <router-view />
      </el-main>
      
      <el-footer class="footer">
        <p>© 2024 Hotel Vending System. All rights reserved.</p>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { Shop, ShoppingCart, User } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const cartStore = useCartStore()

const activeMenu = computed(() => route.path)

const handleMenuSelect = (index: string) => {
  router.push(index)
}

const handleCommand = (command: string) => {
  if (command === 'logout') {
    userStore.logout()
  } else if (command === 'profile') {
    router.push('/profile')
  }
}

onMounted(() => {
  if (userStore.isLogin && !userStore.userInfo) {
    userStore.getUserInfo()
  }
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
}

.header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.cart-badge {
  margin-right: 10px;
}

.employee-badge {
  margin-left: 5px;
  padding: 2px 6px;
  background-color: #67c23a;
  color: white;
  border-radius: 3px;
  font-size: 12px;
}

.main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  min-height: calc(100vh - 120px);
}

.footer {
  background-color: #f5f5f5;
  text-align: center;
  padding: 20px;
}
</style>
