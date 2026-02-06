package com.hotel.vending.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.hotel.vending.common.Result;
import com.hotel.vending.dto.CreateOrderDTO;
import com.hotel.vending.entity.Order;
import com.hotel.vending.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Order Controller
 */
@Tag(name = "Order Management")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @Operation(summary = "Create order")
    @PostMapping("/create")
    public Result<Order> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO) {
        StpUtil.checkLogin();
        Order order = orderService.createOrder(createOrderDTO);
        return Result.success(order);
    }
    
    @Operation(summary = "Pay order (WeChat/Alipay)")
    @PostMapping("/pay/{orderId}")
    public Result<String> payOrder(@PathVariable Long orderId) {
        StpUtil.checkLogin();
        String paymentUrl = orderService.payOrder(orderId);
        return Result.success(paymentUrl);
    }
    
    @Operation(summary = "Get user's orders")
    @GetMapping("/my")
    public Result<List<Order>> getUserOrders(@RequestParam(required = false) Integer status) {
        StpUtil.checkLogin();
        List<Order> orders = orderService.getUserOrders(status);
        return Result.success(orders);
    }
    
    @Operation(summary = "Get order detail")
    @GetMapping("/{orderId}")
    public Result<Order> getOrderDetail(@PathVariable Long orderId) {
        StpUtil.checkLogin();
        Order order = orderService.getOrderDetail(orderId);
        return Result.success(order);
    }
    
    @Operation(summary = "Cancel order")
    @PostMapping("/cancel/{orderId}")
    public Result<Void> cancelOrder(@PathVariable Long orderId) {
        StpUtil.checkLogin();
        orderService.cancelOrder(orderId);
        return Result.success();
    }
}
