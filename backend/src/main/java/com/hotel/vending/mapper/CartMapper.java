package com.hotel.vending.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.vending.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

/**
 * Cart Mapper
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
}
