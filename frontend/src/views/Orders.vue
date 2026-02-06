<template>
  <div class="orders">
    <h1>My Orders</h1>
    
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="All Orders" name="all" />
      <el-tab-pane label="Pending" name="pending" />
      <el-tab-pane label="Paid" name="paid" />
      <el-tab-pane label="Completed" name="completed" />
      <el-tab-pane label="Cancelled" name="cancelled" />
    </el-tabs>
    
    <div v-loading="loading">
      <el-empty v-if="orders.length === 0" description="No orders found" />
      
      <el-card v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <div>
            <span class="order-no">Order No: {{ order.orderNo }}</span>
            <el-tag :type="getStatusTag(order.status)" style="margin-left: 15px">
              {{ getStatusText(order.status) }}
            </el-tag>
          </div>
          <div class="order-time">{{ formatDate(order.createTime) }}</div>
        </div>
        
        <el-divider />
        
        <div class="order-info">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="info-item">
                <span class="label">Total Amount:</span>
                <span class="value">¥{{ order.totalAmount.toFixed(2) }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="info-item">
                <span class="label">Discount:</span>
                <span class="value" style="color: #67c23a">
                  -¥{{ order.discountAmount.toFixed(2) }}
                </span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="info-item">
                <span class="label">Coupon:</span>
                <span class="value" style="color: #e6a23c">
                  -¥{{ order.couponAmount.toFixed(2) }}
                </span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="info-item">
                <span class="label">Pay Amount:</span>
                <span class="value pay-amount">¥{{ order.payAmount.toFixed(2) }}</span>
              </div>
            </el-col>
          </el-row>
          
          <div class="payment-info">
            <span class="label">Payment Method:</span>
            <el-tag :type="order.payType === 'wechat' ? 'success' : 'warning'">
              {{ order.payType === 'wechat' ? 'WeChat Pay' : 'Alipay' }}
            </el-tag>
            <span v-if="order.payTime" class="pay-time">
              Paid at: {{ formatDate(order.payTime) }}
            </span>
          </div>
          
          <div v-if="order.remark" class="remark">
            <span class="label">Remark:</span>
            <span>{{ order.remark }}</span>
          </div>
        </div>
        
        <el-divider />
        
        <div class="order-actions">
          <el-button @click="viewDetail(order.id)">View Detail</el-button>
          <el-button
            v-if="order.status === 0"
            type="danger"
            @click="cancelOrder(order.id)"
          >
            Cancel Order
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { orderApi } from '@/api/order'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Order } from '@/types'

const activeTab = ref('all')
const loading = ref(false)
const orders = ref<Order[]>([])

const statusMap: Record<string, number | undefined> = {
  'all': undefined,
  'pending': 0,
  'paid': 1,
  'completed': 3,
  'cancelled': 4
}

const getOrders = async (status?: number) => {
  loading.value = true
  try {
    const res = await orderApi.getMyOrders(status)
    orders.value = res.data
  } catch (error) {
    console.error('Failed to load orders:', error)
  } finally {
    loading.value = false
  }
}

const handleTabChange = (tab: string) => {
  getOrders(statusMap[tab])
}

const viewDetail = async (orderId: number) => {
  try {
    const res = await orderApi.getDetail(orderId)
    ElMessageBox.alert(JSON.stringify(res.data, null, 2), 'Order Detail')
  } catch (error) {
    console.error('Failed to load order detail:', error)
  }
}

const cancelOrder = async (orderId: number) => {
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to cancel this order?',
      'Confirm',
      {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning'
      }
    )
    
    await orderApi.cancel(orderId)
    ElMessage.success('Order cancelled successfully')
    getOrders(statusMap[activeTab.value])
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to cancel order:', error)
    }
  }
}

const getStatusTag = (status: number) => {
  const tags: Record<number, string> = {
    0: 'warning',
    1: 'success',
    2: 'info',
    3: 'success',
    4: 'danger'
  }
  return tags[status] || 'info'
}

const getStatusText = (status: number) => {
  const texts: Record<number, string> = {
    0: 'Pending Payment',
    1: 'Paid',
    2: 'Shipped',
    3: 'Completed',
    4: 'Cancelled'
  }
  return texts[status] || 'Unknown'
}

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}

onMounted(() => {
  getOrders()
})
</script>

<style scoped>
.orders {
  padding: 20px 0;
}

h1 {
  margin-bottom: 30px;
}

.order-card {
  margin-bottom: 20px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-no {
  font-weight: bold;
  font-size: 16px;
}

.order-time {
  color: #666;
  font-size: 14px;
}

.order-info {
  margin: 20px 0;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.info-item .label {
  color: #666;
  font-size: 14px;
}

.info-item .value {
  font-size: 18px;
  font-weight: bold;
}

.pay-amount {
  color: #f56c6c;
  font-size: 20px;
}

.payment-info {
  margin-top: 15px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.payment-info .label {
  color: #666;
}

.pay-time {
  color: #666;
  font-size: 14px;
}

.remark {
  margin-top: 15px;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.remark .label {
  color: #666;
  margin-right: 10px;
}

.order-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}
</style>
