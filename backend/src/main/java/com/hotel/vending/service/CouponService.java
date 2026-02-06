package com.hotel.vending.service;

import com.hotel.vending.entity.Coupon;
import com.hotel.vending.entity.UserCoupon;
import java.util.List;

/**
 * Coupon Service Interface
 * Feature 2: Electronic coupon system
 */
public interface CouponService {
    
    /**
     * Get all available coupons
     */
    List<Coupon> getAvailableCoupons();
    
    /**
     * Receive a coupon
     */
    UserCoupon receiveCoupon(Long couponId);
    
    /**
     * Get user's coupons
     */
    List<UserCoupon> getUserCoupons(Integer status);
    
    /**
     * Get available coupons for an order amount
     */
    List<UserCoupon> getAvailableCouponsForOrder(java.math.BigDecimal orderAmount);
}
