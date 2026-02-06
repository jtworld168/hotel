<template>
  <div class="cart">
    <h1>Shopping Cart</h1>
    
    <el-empty v-if="cartStore.cartItems.length === 0" description="Your cart is empty">
      <el-button type="primary" @click="router.push('/products')">
        Go Shopping
      </el-button>
    </el-empty>
    
    <div v-else>
      <el-table :data="cartStore.cartItems" style="width: 100%">
        <el-table-column label="Product" width="300">
          <template #default="{ row }">
            <div class="product-info">
              <el-image
                :src="row.product?.image || '/placeholder.png'"
                fit="cover"
                style="width: 60px; height: 60px; margin-right: 15px"
              />
              <div>
                <div class="product-name">{{ row.product?.name }}</div>
                <el-tag v-if="row.product?.hasDiscount" type="success" size="small">
                  Employee Discount
                </el-tag>
              </div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="Price" width="150">
          <template #default="{ row }">
            <div class="price">
              <div class="current-price">¥{{ row.product?.currentPrice.toFixed(2) }}</div>
              <div v-if="row.product?.hasDiscount" class="original-price">
                ¥{{ row.product?.originalPrice.toFixed(2) }}
              </div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="Quantity" width="200">
          <template #default="{ row }">
            <el-input-number
              :model-value="row.quantity"
              @update:model-value="(val) => updateQuantity(row, val)"
              :min="1"
              :max="row.product?.stock || 1"
            />
          </template>
        </el-table-column>
        
        <el-table-column label="Subtotal" width="150">
          <template #default="{ row }">
            <span class="subtotal">
              ¥{{ ((row.product?.currentPrice || 0) * row.quantity).toFixed(2) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="Actions">
          <template #default="{ row }">
            <el-button type="danger" @click="removeItem(row)">
              Remove
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-card class="summary-card">
        <div class="summary">
          <div class="summary-row">
            <span class="label">Total Items:</span>
            <span class="value">{{ cartStore.totalItems }}</span>
          </div>
          <div class="summary-row total">
            <span class="label">Total Amount:</span>
            <span class="value">¥{{ cartStore.totalAmount.toFixed(2) }}</span>
          </div>
          
          <el-button
            type="primary"
            size="large"
            @click="checkout"
            style="width: 100%; margin-top: 20px"
          >
            Checkout
          </el-button>
        </div>
      </el-card>
    </div>
    
    <!-- Checkout Dialog -->
    <el-dialog v-model="showCheckoutDialog" title="Checkout" width="500px">
      <el-form :model="checkoutForm" label-width="120px">
        <el-form-item label="Select Coupon">
          <el-select
            v-model="checkoutForm.userCouponId"
            placeholder="No coupon"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="coupon in availableCoupons"
              :key="coupon.id"
              :label="`Coupon #${coupon.id}`"
              :value="coupon.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="Payment Method" required>
          <el-radio-group v-model="checkoutForm.payType">
            <el-radio label="wechat">WeChat Pay</el-radio>
            <el-radio label="alipay">Alipay</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="Remark">
          <el-input
            v-model="checkoutForm.remark"
            type="textarea"
            :rows="3"
            placeholder="Optional remark"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showCheckoutDialog = false">Cancel</el-button>
        <el-button type="primary" @click="confirmCheckout">Confirm Order</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { couponApi } from '@/api/coupon'
import { orderApi } from '@/api/order'
import { ElMessage } from 'element-plus'
import type { CartItem, UserCoupon, CreateOrderForm } from '@/types'

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const showCheckoutDialog = ref(false)
const availableCoupons = ref<UserCoupon[]>([])
const checkoutForm = ref<CreateOrderForm>({
  productIds: [],
  quantities: [],
  userCouponId: undefined,
  payType: 'wechat',
  remark: ''
})

const updateQuantity = (row: CartItem, quantity: number) => {
  cartStore.updateQuantity(row.productId, quantity)
}

const removeItem = (row: CartItem) => {
  cartStore.removeFromCart(row.productId)
  ElMessage.success('Removed from cart')
}

const checkout = async () => {
  if (!userStore.isLogin) {
    ElMessage.warning('Please login first')
    router.push('/login')
    return
  }
  
  // Load available coupons
  try {
    const res = await couponApi.getAvailableForOrder(cartStore.totalAmount)
    availableCoupons.value = res.data
  } catch (error) {
    console.error('Failed to load coupons:', error)
  }
  
  showCheckoutDialog.value = true
}

const confirmCheckout = async () => {
  checkoutForm.value.productIds = cartStore.cartItems.map(item => item.productId)
  checkoutForm.value.quantities = cartStore.cartItems.map(item => item.quantity)
  
  try {
    const orderRes = await orderApi.create(checkoutForm.value)
    const order = orderRes.data
    
    // Pay order
    const payRes = await orderApi.pay(order.id)
    
    ElMessage.success('Order created successfully!')
    cartStore.clearCart()
    showCheckoutDialog.value = false
    router.push('/orders')
  } catch (error) {
    console.error('Checkout failed:', error)
  }
}

onMounted(() => {
  if (!userStore.isLogin) {
    ElMessage.warning('Please login first')
    router.push('/login')
  }
})
</script>

<style scoped>
.cart {
  padding: 20px 0;
}

h1 {
  margin-bottom: 30px;
}

.product-info {
  display: flex;
  align-items: center;
}

.product-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.price {
  display: flex;
  flex-direction: column;
}

.current-price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: bold;
}

.original-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
}

.subtotal {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}

.summary-card {
  margin-top: 30px;
  max-width: 400px;
  margin-left: auto;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  font-size: 16px;
}

.summary-row.total {
  font-size: 20px;
  font-weight: bold;
  color: #f56c6c;
  padding-top: 15px;
  border-top: 1px solid #eee;
}
</style>
