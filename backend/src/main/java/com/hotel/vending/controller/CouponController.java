package com.hotel.vending.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.hotel.vending.common.Result;
import com.hotel.vending.entity.Coupon;
import com.hotel.vending.entity.UserCoupon;
import com.hotel.vending.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Coupon Controller
 * Feature 2: Electronic coupon system
 */
@Tag(name = "Coupon Management")
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
    
    private final CouponService couponService;
    
    @Operation(summary = "Get all available coupons")
    @GetMapping("/available")
    public Result<List<Coupon>> getAvailableCoupons() {
        List<Coupon> coupons = couponService.getAvailableCoupons();
        return Result.success(coupons);
    }
    
    @Operation(summary = "Receive a coupon")
    @PostMapping("/receive/{couponId}")
    public Result<UserCoupon> receiveCoupon(@PathVariable Long couponId) {
        StpUtil.checkLogin();
        UserCoupon userCoupon = couponService.receiveCoupon(couponId);
        return Result.success(userCoupon);
    }
    
    @Operation(summary = "Get user's coupons")
    @GetMapping("/my")
    public Result<List<UserCoupon>> getUserCoupons(@RequestParam(required = false) Integer status) {
        StpUtil.checkLogin();
        List<UserCoupon> userCoupons = couponService.getUserCoupons(status);
        return Result.success(userCoupons);
    }
    
    @Operation(summary = "Get available coupons for order")
    @GetMapping("/available-for-order")
    public Result<List<UserCoupon>> getAvailableCouponsForOrder(@RequestParam BigDecimal orderAmount) {
        StpUtil.checkLogin();
        List<UserCoupon> userCoupons = couponService.getAvailableCouponsForOrder(orderAmount);
        return Result.success(userCoupons);
    }
}
