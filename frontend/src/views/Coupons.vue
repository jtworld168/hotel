<template>
  <div class="coupons">
    <h1>Electronic Coupons</h1>
    
    <el-tabs v-model="activeTab">
      <el-tab-pane label="Available Coupons" name="available">
        <div v-loading="loadingAvailable">
          <el-empty v-if="availableCoupons.length === 0" description="No available coupons" />
          
          <el-row :gutter="20" v-else>
            <el-col :span="8" v-for="coupon in availableCoupons" :key="coupon.id">
              <el-card class="coupon-card">
                <div class="coupon-header">
                  <h3>{{ coupon.name }}</h3>
                  <el-tag :type="getCouponTypeTag(coupon.type)">
                    {{ getCouponTypeText(coupon.type) }}
                  </el-tag>
                </div>
                
                <div class="coupon-body">
                  <div class="discount-info">
                    <span class="discount-amount">
                      {{ getDiscountText(coupon) }}
                    </span>
                  </div>
                  
                  <div class="coupon-detail">
                    <p v-if="coupon.conditionAmount > 0">
                      Min. order: ¥{{ coupon.conditionAmount.toFixed(2) }}
                    </p>
                    <p>Valid: {{ formatDate(coupon.startTime) }} - {{ formatDate(coupon.endTime) }}</p>
                    <p>Remaining: {{ coupon.totalCount - coupon.receivedCount }} / {{ coupon.totalCount }}</p>
                  </div>
                </div>
                
                <el-button type="primary" @click="receiveCoupon(coupon.id)" style="width: 100%">
                  Receive
                </el-button>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="My Coupons" name="my">
        <div v-loading="loadingMy">
          <el-empty v-if="myCoupons.length === 0" description="No coupons received yet" />
          
          <el-table :data="myCoupons" v-else>
            <el-table-column label="Coupon ID" prop="id" width="100" />
            <el-table-column label="Coupon" width="150">
              <template #default="{ row }">
                <span>Coupon #{{ row.couponId }}</span>
              </template>
            </el-table-column>
            <el-table-column label="Status" width="120">
              <template #default="{ row }">
                <el-tag :type="getStatusTag(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="Received Time">
              <template #default="{ row }">
                {{ formatDate(row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="Use Time">
              <template #default="{ row }">
                {{ row.useTime ? formatDate(row.useTime) : '-' }}
              </template>
            </el-table-column>
            <el-table-column label="Order ID">
              <template #default="{ row }">
                {{ row.orderId || '-' }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { couponApi } from '@/api/coupon'
import { ElMessage } from 'element-plus'
import type { Coupon, UserCoupon } from '@/types'

const activeTab = ref('available')
const loadingAvailable = ref(false)
const loadingMy = ref(false)
const availableCoupons = ref<Coupon[]>([])
const myCoupons = ref<UserCoupon[]>([])

const getAvailableCoupons = async () => {
  loadingAvailable.value = true
  try {
    const res = await couponApi.getAvailable()
    availableCoupons.value = res.data
  } catch (error) {
    console.error('Failed to load coupons:', error)
  } finally {
    loadingAvailable.value = false
  }
}

const getMyCoupons = async () => {
  loadingMy.value = true
  try {
    const res = await couponApi.getUserCoupons()
    myCoupons.value = res.data
  } catch (error) {
    console.error('Failed to load my coupons:', error)
  } finally {
    loadingMy.value = false
  }
}

const receiveCoupon = async (couponId: number) => {
  try {
    await couponApi.receive(couponId)
    ElMessage.success('Coupon received successfully!')
    getAvailableCoupons()
    getMyCoupons()
  } catch (error) {
    console.error('Failed to receive coupon:', error)
  }
}

const getCouponTypeTag = (type: string) => {
  const tags: Record<string, string> = {
    'full_reduction': 'success',
    'discount': 'warning',
    'fixed': 'danger'
  }
  return tags[type] || 'info'
}

const getCouponTypeText = (type: string) => {
  const texts: Record<string, string> = {
    'full_reduction': 'Full Reduction',
    'discount': 'Discount',
    'fixed': 'Fixed Amount'
  }
  return texts[type] || type
}

const getDiscountText = (coupon: Coupon) => {
  if (coupon.type === 'full_reduction' || coupon.type === 'fixed') {
    return `¥${coupon.discountAmount?.toFixed(2)}`
  } else if (coupon.type === 'discount') {
    return `${((1 - (coupon.discountRate || 0)) * 100).toFixed(0)}% OFF`
  }
  return ''
}

const getStatusTag = (status: number) => {
  const tags: Record<number, string> = {
    0: 'success',
    1: 'info',
    2: 'danger'
  }
  return tags[status] || 'info'
}

const getStatusText = (status: number) => {
  const texts: Record<number, string> = {
    0: 'Unused',
    1: 'Used',
    2: 'Expired'
  }
  return texts[status] || 'Unknown'
}

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString()
}

onMounted(() => {
  getAvailableCoupons()
  getMyCoupons()
})
</script>

<style scoped>
.coupons {
  padding: 20px 0;
}

h1 {
  margin-bottom: 30px;
}

.coupon-card {
  margin-bottom: 20px;
}

.coupon-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.coupon-header h3 {
  margin: 0;
  font-size: 18px;
}

.coupon-body {
  margin-bottom: 20px;
}

.discount-info {
  text-align: center;
  margin-bottom: 15px;
}

.discount-amount {
  font-size: 36px;
  color: #f56c6c;
  font-weight: bold;
}

.coupon-detail p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}
</style>
