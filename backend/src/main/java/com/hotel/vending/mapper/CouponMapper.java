package com.hotel.vending.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.vending.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;

/**
 * Coupon Mapper
 */
@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {
}
