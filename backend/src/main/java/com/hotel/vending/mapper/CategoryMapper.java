package com.hotel.vending.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.vending.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * Category Mapper
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
