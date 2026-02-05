package com.hotel.vending.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.vending.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * Order Item Mapper
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
