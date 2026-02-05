package com.hotel.vending.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotel.vending.dto.CreateOrderDTO;
import com.hotel.vending.entity.*;
import com.hotel.vending.mapper.*;
import com.hotel.vending.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order Service Implementation
 * Includes employee pricing, coupon discount, and payment integration
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;
    
    @Override
    @Transactional
    public Order createOrder(CreateOrderDTO createOrderDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        
        // Create order
        Order order = new Order();
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        order.setUserId(userId);
        order.setPayType(createOrderDTO.getPayType());
        order.setRemark(createOrderDTO.getRemark());
        order.setStatus(0); // pending payment
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        
        // Create order items
        Long[] productIds = createOrderDTO.getProductIds();
        Integer[] quantities = createOrderDTO.getQuantities();
        
        if (productIds.length != quantities.length) {
            throw new RuntimeException("Product IDs and quantities must have the same length");
        }
        
        for (int i = 0; i < productIds.length; i++) {
            Product product = productMapper.selectById(productIds[i]);
            if (product == null) {
                throw new RuntimeException("Product not found: " + productIds[i]);
            }
            
            if (product.getStock() < quantities[i]) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }
            
            // Calculate price with employee discount (Feature 1)
            BigDecimal itemPrice = product.getPrice();
            if ("employee".equals(user.getRole())) {
                if (product.getEmployeePrice() != null) {
                    itemPrice = product.getEmployeePrice();
                } else if (user.getEmployeeDiscount() != null) {
                    itemPrice = product.getPrice().multiply(user.getEmployeeDiscount());
                }
                
                // Calculate discount amount
                BigDecimal originalItemTotal = product.getPrice().multiply(new BigDecimal(quantities[i]));
                BigDecimal discountedItemTotal = itemPrice.multiply(new BigDecimal(quantities[i]));
                discountAmount = discountAmount.add(originalItemTotal.subtract(discountedItemTotal));
            }
            
            BigDecimal itemTotal = itemPrice.multiply(new BigDecimal(quantities[i]));
            totalAmount = totalAmount.add(itemTotal);
            
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getImage());
            orderItem.setPrice(itemPrice);
            orderItem.setQuantity(quantities[i]);
            orderItem.setTotalAmount(itemTotal);
            
            // Update stock
            product.setStock(product.getStock() - quantities[i]);
            product.setSales(product.getSales() + quantities[i]);
            productMapper.updateById(product);
            
            // Save later with order ID
            orderItem.setOrderId(0L); // Temporary, will update after order is created
            orderItemMapper.insert(orderItem);
        }
        
        order.setTotalAmount(totalAmount.add(discountAmount)); // Original total
        order.setDiscountAmount(discountAmount);
        
        // Apply coupon if provided (Feature 2)
        BigDecimal couponAmount = BigDecimal.ZERO;
        if (createOrderDTO.getUserCouponId() != null) {
            UserCoupon userCoupon = userCouponMapper.selectById(createOrderDTO.getUserCouponId());
            if (userCoupon == null || !userCoupon.getUserId().equals(userId)) {
                throw new RuntimeException("Invalid coupon");
            }
            
            if (userCoupon.getStatus() != 0) {
                throw new RuntimeException("Coupon already used or expired");
            }
            
            Coupon coupon = couponMapper.selectById(userCoupon.getCouponId());
            if (coupon == null) {
                throw new RuntimeException("Coupon not found");
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
                throw new RuntimeException("Coupon is not within valid time range");
            }
            
            if (totalAmount.compareTo(coupon.getConditionAmount()) < 0) {
                throw new RuntimeException("Order amount does not meet coupon requirement");
            }
            
            // Calculate coupon discount
            if ("full_reduction".equals(coupon.getType()) || "fixed".equals(coupon.getType())) {
                couponAmount = coupon.getDiscountAmount();
            } else if ("discount".equals(coupon.getType())) {
                couponAmount = totalAmount.multiply(BigDecimal.ONE.subtract(coupon.getDiscountRate()));
            }
            
            order.setUserCouponId(userCoupon.getId());
        }
        
        order.setCouponAmount(couponAmount);
        order.setPayAmount(totalAmount.subtract(couponAmount));
        
        // Ensure pay amount is not negative
        if (order.getPayAmount().compareTo(BigDecimal.ZERO) < 0) {
            order.setPayAmount(BigDecimal.ZERO);
        }
        
        orderMapper.insert(order);
        
        // Update order items with correct order ID
        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItem::getOrderId, 0L);
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.updateById(item);
        }
        
        return order;
    }
    
    @Override
    public String payOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        
        Long userId = StpUtil.getLoginIdAsLong();
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Not authorized to pay this order");
        }
        
        if (order.getStatus() != 0) {
            throw new RuntimeException("Order already paid or cancelled");
        }
        
        // Feature 3: WeChat and Alipay payment integration
        // In a real implementation, this would call the payment gateway API
        String paymentUrl;
        if ("wechat".equals(order.getPayType())) {
            paymentUrl = generateWeChatPayUrl(order);
        } else if ("alipay".equals(order.getPayType())) {
            paymentUrl = generateAlipayUrl(order);
        } else {
            throw new RuntimeException("Unsupported payment type");
        }
        
        // Mark order as paid (in real scenario, this would be done by payment callback)
        order.setStatus(1);
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);
        
        // Mark coupon as used if applicable
        if (order.getUserCouponId() != null) {
            UserCoupon userCoupon = userCouponMapper.selectById(order.getUserCouponId());
            if (userCoupon != null) {
                userCoupon.setStatus(1); // used
                userCoupon.setUseTime(LocalDateTime.now());
                userCoupon.setOrderId(order.getId());
                userCouponMapper.updateById(userCoupon);
            }
        }
        
        return paymentUrl;
    }
    
    /**
     * Generate WeChat Pay URL (mock implementation)
     */
    private String generateWeChatPayUrl(Order order) {
        // In real implementation, call WeChat Pay API
        return "wechat://pay?order=" + order.getOrderNo() + "&amount=" + order.getPayAmount();
    }
    
    /**
     * Generate Alipay URL (mock implementation)
     */
    private String generateAlipayUrl(Order order) {
        // In real implementation, call Alipay API
        return "https://alipay.com/pay?order=" + order.getOrderNo() + "&amount=" + order.getPayAmount();
    }
    
    @Override
    public List<Order> getUserOrders(Integer status) {
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        if (status != null) {
            queryWrapper.eq(Order::getStatus, status);
        }
        queryWrapper.orderByDesc(Order::getCreateTime);
        return orderMapper.selectList(queryWrapper);
    }
    
    @Override
    public Order getOrderDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        
        Long userId = StpUtil.getLoginIdAsLong();
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Not authorized to view this order");
        }
        
        return order;
    }
    
    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        
        Long userId = StpUtil.getLoginIdAsLong();
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Not authorized to cancel this order");
        }
        
        if (order.getStatus() != 0) {
            throw new RuntimeException("Only pending orders can be cancelled");
        }
        
        // Restore product stock
        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
        
        for (OrderItem item : orderItems) {
            Product product = productMapper.selectById(item.getProductId());
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                product.setSales(product.getSales() - item.getQuantity());
                productMapper.updateById(product);
            }
        }
        
        // Cancel order
        order.setStatus(4);
        orderMapper.updateById(order);
        
        // Restore coupon if used
        if (order.getUserCouponId() != null) {
            UserCoupon userCoupon = userCouponMapper.selectById(order.getUserCouponId());
            if (userCoupon != null && userCoupon.getStatus() == 1) {
                userCoupon.setStatus(0); // unused
                userCoupon.setUseTime(null);
                userCoupon.setOrderId(null);
                userCouponMapper.updateById(userCoupon);
            }
        }
    }
}
