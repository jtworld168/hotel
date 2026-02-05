package com.hotel.vending.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.vending.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * Product Mapper
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
