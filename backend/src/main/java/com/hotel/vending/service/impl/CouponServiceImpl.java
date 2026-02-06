package com.hotel.vending.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotel.vending.entity.Coupon;
import com.hotel.vending.entity.UserCoupon;
import com.hotel.vending.mapper.CouponMapper;
import com.hotel.vending.mapper.UserCouponMapper;
import com.hotel.vending.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Coupon Service Implementation
 * Feature 2: Electronic coupon system
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    
    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;
    
    @Override
    public List<Coupon> getAvailableCoupons() {
        LambdaQueryWrapper<Coupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Coupon::getStatus, 1)
                    .le(Coupon::getStartTime, LocalDateTime.now())
                    .ge(Coupon::getEndTime, LocalDateTime.now())
                    .apply("received_count < total_count");
        return couponMapper.selectList(queryWrapper);
    }
    
    @Override
    @Transactional
    public UserCoupon receiveCoupon(Long couponId) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        // Check if coupon exists and is available
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new RuntimeException("Coupon not found");
        }
        
        if (coupon.getStatus() == 0) {
            throw new RuntimeException("Coupon is not available");
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            throw new RuntimeException("Coupon is not within valid time range");
        }
        
        if (coupon.getReceivedCount() >= coupon.getTotalCount()) {
            throw new RuntimeException("Coupon has been fully claimed");
        }
        
        // Check if user already received this coupon
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId)
                    .eq(UserCoupon::getCouponId, couponId);
        UserCoupon existingUserCoupon = userCouponMapper.selectOne(queryWrapper);
        if (existingUserCoupon != null) {
            throw new RuntimeException("You have already received this coupon");
        }
        
        // Create user coupon
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0); // unused
        userCouponMapper.insert(userCoupon);
        
        // Update received count
        coupon.setReceivedCount(coupon.getReceivedCount() + 1);
        couponMapper.updateById(coupon);
        
        return userCoupon;
    }
    
    @Override
    public List<UserCoupon> getUserCoupons(Integer status) {
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId);
        if (status != null) {
            queryWrapper.eq(UserCoupon::getStatus, status);
        }
        return userCouponMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<UserCoupon> getAvailableCouponsForOrder(BigDecimal orderAmount) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        // Get all unused user coupons
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId)
                    .eq(UserCoupon::getStatus, 0);
        List<UserCoupon> userCoupons = userCouponMapper.selectList(queryWrapper);
        
        // Filter coupons that are applicable for the order amount
        return userCoupons.stream()
                .filter(userCoupon -> {
                    Coupon coupon = couponMapper.selectById(userCoupon.getCouponId());
                    if (coupon == null) return false;
                    
                    LocalDateTime now = LocalDateTime.now();
                    if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
                        return false;
                    }
                    
                    // Check if order amount meets condition
                    return orderAmount.compareTo(coupon.getConditionAmount()) >= 0;
                })
                .collect(Collectors.toList());
    }
}
