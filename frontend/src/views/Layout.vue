<template>
  <div class="layout">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo">
            <el-icon><Shop /></el-icon>
            <span>{{ lang === 'en' ? 'Hotel Vending' : '酒店售货系统' }}</span>
          </div>
          
          <el-menu
            :default-active="activeMenu"
            mode="horizontal"
            :ellipsis="false"
            @select="handleMenuSelect"
          >
            <el-menu-item index="/home">{{ lang === 'en' ? 'Home' : '首页' }}</el-menu-item>
            <el-menu-item index="/products">{{ lang === 'en' ? 'Products' : '商品' }}</el-menu-item>
            <el-menu-item index="/coupons" v-if="userStore.isLogin">{{ lang === 'en' ? 'Coupons' : '优惠券' }}</el-menu-item>
            <el-menu-item index="/orders" v-if="userStore.isLogin">{{ lang === 'en' ? 'Orders' : '订单' }}</el-menu-item>
          </el-menu>
          
          <div class="header-right">
            <!-- Language Toggle Button -->
            <el-button @click="toggleLang" circle size="default">
              {{ lang === 'en' ? '中' : 'EN' }}
            </el-button>
            
            <!-- QR Scanner Button -->
            <el-button @click="openScanner" circle size="default">
              <el-icon><Camera /></el-icon>
            </el-button>
            
            <el-badge :value="cartStore.totalItems" class="cart-badge" v-if="cartStore.totalItems > 0">
              <el-button @click="router.push('/cart')" :icon="ShoppingCart">{{ lang === 'en' ? 'Cart' : '购物车' }}</el-button>
            </el-badge>
            <el-button v-else @click="router.push('/cart')" :icon="ShoppingCart">{{ lang === 'en' ? 'Cart' : '购物车' }}</el-button>
            
            <template v-if="userStore.isLogin">
              <el-dropdown @command="handleCommand">
                <el-button>
                  <el-icon><User /></el-icon>
                  {{ userStore.userInfo?.nickname || userStore.userInfo?.username }}
                  <span v-if="userStore.userInfo?.role === 'employee'" class="employee-badge">{{ lang === 'en' ? 'Employee' : '员工' }}</span>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="profile">{{ lang === 'en' ? 'Profile' : '个人资料' }}</el-dropdown-item>
                    <el-dropdown-item command="logout">{{ lang === 'en' ? 'Logout' : '退出' }}</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <el-button @click="router.push('/login')">{{ lang === 'en' ? 'Login' : '登录' }}</el-button>
              <el-button type="primary" @click="router.push('/register')">{{ lang === 'en' ? 'Register' : '注册' }}</el-button>
            </template>
          </div>
        </div>
      </el-header>
      
      <el-main class="main">
        <router-view />
      </el-main>
      
      <el-footer class="footer">
        <p>{{ lang === 'en' ? '© 2024 Hotel Vending System. All rights reserved.' : '© 2024 酒店售货系统。保留所有权利。' }}</p>
      </el-footer>
    </el-container>
    
    <!-- QR Scanner Modal -->
    <el-dialog v-model="scannerOpen" :title="lang === 'en' ? 'QR Code Scanner' : '二维码扫描'" width="600px">
      <div style="text-align: center; padding: 20px;">
        <video ref="videoEl" style="width: 100%; max-width: 500px; border-radius: 8px;" autoplay></video>
        <canvas ref="canvasEl" style="display: none;"></canvas>
        <p style="margin-top: 15px;">{{ lang === 'en' ? 'Point camera at QR code' : '将相机对准二维码' }}</p>
        <p v-if="scanMsg" style="margin-top: 10px; color: #67c23a;">{{ scanMsg }}</p>
      </div>
      <template #footer>
        <el-button @click="closeScanner">{{ lang === 'en' ? 'Close' : '关闭' }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { Shop, ShoppingCart, User, Camera } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const cartStore = useCartStore()

// Language state
const lang = ref(localStorage.getItem('appLang') || 'en')

// Scanner state  
const scannerOpen = ref(false)
const videoEl = ref<HTMLVideoElement>()
const canvasEl = ref<HTMLCanvasElement>()
const scanMsg = ref('')
let mediaStream: MediaStream | null = null
let scanInterval: number | null = null

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

// Toggle between English and Chinese
const toggleLang = () => {
  lang.value = lang.value === 'en' ? 'zh' : 'en'
  localStorage.setItem('appLang', lang.value)
}

// Open QR scanner
const openScanner = async () => {
  scannerOpen.value = true
  scanMsg.value = ''
  
  try {
    // Wait for dialog to render
    await new Promise(resolve => setTimeout(resolve, 100))
    
    if (!videoEl.value || !canvasEl.value) return
    
    // Request camera access
    mediaStream = await navigator.mediaDevices.getUserMedia({ 
      video: { facingMode: 'environment' } 
    })
    
    videoEl.value.srcObject = mediaStream
    
    // Start scanning
    scanInterval = window.setInterval(() => {
      tryDecodeQR()
    }, 500)
    
  } catch (err) {
    ElMessage.error(lang.value === 'en' ? 'Camera access denied' : '相机访问被拒绝')
    scannerOpen.value = false
  }
}

// Close scanner and cleanup
const closeScanner = () => {
  if (scanInterval) {
    clearInterval(scanInterval)
    scanInterval = null
  }
  
  if (mediaStream) {
    mediaStream.getTracks().forEach(track => track.stop())
    mediaStream = null
  }
  
  scannerOpen.value = false
  scanMsg.value = ''
}

// Simple QR decode attempt using canvas
const tryDecodeQR = () => {
  if (!videoEl.value || !canvasEl.value) return
  
  const video = videoEl.value
  const canvas = canvasEl.value
  const ctx = canvas.getContext('2d')
  
  if (!ctx || video.readyState !== video.HAVE_ENOUGH_DATA) return
  
  canvas.width = video.videoWidth
  canvas.height = video.videoHeight
  ctx.drawImage(video, 0, 0, canvas.width, canvas.height)
  
  const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height)
  
  // Use jsQR library if available, otherwise show message
  if (typeof (window as any).jsQR === 'function') {
    const code = (window as any).jsQR(imageData.data, imageData.width, imageData.height)
    
    if (code) {
      scanMsg.value = lang.value === 'en' ? `Scanned: ${code.data}` : `扫描结果: ${code.data}`
      
      // Handle the scanned data
      if (/^\d+$/.test(code.data)) {
        // If it's just numbers, assume it's a product ID
        setTimeout(() => {
          closeScanner()
          router.push(`/product/${code.data}`)
        }, 1000)
      } else if (code.data.startsWith('http')) {
        window.open(code.data, '_blank')
      }
      
      if (scanInterval) {
        clearInterval(scanInterval)
        scanInterval = null
      }
    }
  }
}

onMounted(() => {
  if (userStore.isLogin && !userStore.userInfo) {
    userStore.getUserInfo()
  }
  
  // Load jsQR library dynamically
  const script = document.createElement('script')
  script.src = 'https://cdn.jsdelivr.net/npm/jsqr@1.4.0/dist/jsQR.min.js'
  document.head.appendChild(script)
})

onUnmounted(() => {
  closeScanner()
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
