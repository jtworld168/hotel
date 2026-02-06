# 新功能说明 / New Features Guide

## 1. 语言切换 / Language Toggle

### 功能描述
在页面右上角添加了一个语言切换按钮，可以在中文和英文之间切换界面语言。

### 使用方法
1. 点击页面右上角的圆形按钮（显示"中"或"EN"）
2. 界面会立即切换到另一种语言
3. 语言偏好会自动保存到浏览器本地存储

### 支持的界面元素
- 导航菜单（首页、商品、优惠券、订单）
- 按钮文本（购物车、登录、注册、个人资料、退出）
- 页脚版权信息
- 系统标题

---

## 2. 二维码扫描 / QR Code Scanner

### 功能描述
在页面右上角添加了一个扫码按钮，可以打开相机扫描二维码。

### 使用方法
1. 点击页面右上角的相机图标按钮
2. 浏览器会请求相机权限，点击"允许"
3. 将相机对准二维码
4. 系统会自动识别并处理二维码内容

### 支持的二维码类型
- **纯数字**：自动识别为商品ID，跳转到商品详情页
- **URL链接**：在新窗口中打开链接
- **product:123**：自定义格式，跳转到对应商品页面

### 技术实现
- 使用设备原生相机API
- 实时视频流处理
- jsQR库进行二维码识别
- 自动资源清理

---

## Feature Descriptions

### 1. Language Toggle Button

**Location**: Top-right corner of the header (circular button showing "中" or "EN")

**Functionality**:
- Toggle between English and Chinese interface
- Click the button to switch languages instantly
- Language preference is saved to browser localStorage
- All header text updates dynamically

**Supported Elements**:
- Navigation menu items
- Button labels
- System title
- Footer text

### 2. QR Code Scanner

**Location**: Top-right corner of the header (camera icon button)

**Functionality**:
- Click camera icon to open scanner
- Browser requests camera permission
- Point camera at QR code
- Automatic detection and action

**Supported QR Code Types**:
- **Numeric codes**: Navigate to product detail page
- **URLs**: Open in new browser tab
- **Custom format** (product:123): Navigate to specific product

**Technical Details**:
- Uses native getUserMedia API
- Real-time video stream processing
- jsQR library for decoding
- Automatic resource cleanup

---

## 技术细节 / Technical Details

### 实现方式
- **语言管理**：使用 localStorage 存储用户语言偏好
- **二维码扫描**：使用 jsQR 库进行实时解码
- **相机访问**：通过 navigator.mediaDevices.getUserMedia API
- **状态管理**：Vue 3 Composition API 的响应式状态

### 浏览器兼容性
- 现代浏览器（Chrome、Firefox、Safari、Edge）
- 需要HTTPS或localhost环境才能访问相机
- 移动设备优先使用后置摄像头

---

## 使用示例 / Usage Examples

### 语言切换示例
```
初始状态：界面显示英文
点击"中"按钮 → 界面切换为中文
点击"EN"按钮 → 界面切换回英文
```

### 扫码示例
```
扫描商品条码: "12345" → 跳转到 /product/12345
扫描网址: "https://example.com" → 在新标签页打开
扫描自定义码: "product:67" → 跳转到 /product/67
```

---

## 注意事项 / Notes

1. **相机权限**：首次使用扫码功能时，浏览器会请求相机权限，需要用户允许
2. **HTTPS要求**：相机功能在生产环境需要HTTPS协议
3. **光线条件**：扫码功能在光线充足的环境下效果更好
4. **二维码清晰度**：确保二维码清晰可见，建议距离20-30厘米

---

## 更新日志 / Changelog

**2024-02-06**
- ✅ 添加中英文语言切换功能
- ✅ 添加二维码扫描功能
- ✅ 界面头部新增两个圆形按钮
- ✅ 支持实时相机扫描
- ✅ 自动识别商品ID和URL链接
