package com.hotel.vending.service;

import com.hotel.vending.dto.CreateOrderDTO;
import com.hotel.vending.entity.Order;
import java.util.List;

/**
 * Order Service Interface
 */
public interface OrderService {
    
    /**
     * Create order
     */
    Order createOrder(CreateOrderDTO createOrderDTO);
    
    /**
     * Pay order (Feature 3: WeChat and Alipay payment)
     */
    String payOrder(Long orderId);
    
    /**
     * Get user's orders
     */
    List<Order> getUserOrders(Integer status);
    
    /**
     * Get order detail
     */
    Order getOrderDetail(Long orderId);
    
    /**
     * Cancel order
     */
    void cancelOrder(Long orderId);
}
