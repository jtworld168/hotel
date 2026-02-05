package com.hotel.vending.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product Entity
 */
@Data
@TableName("product")
public class Product {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long categoryId;
    
    private String name;
    
    private String description;
    
    /**
     * Product images (comma separated)
     */
    private String image;
    
    /**
     * Original price
     */
    private BigDecimal price;
    
    /**
     * Employee special price (null = use discount rate)
     */
    private BigDecimal employeePrice;
    
    private Integer stock;
    
    private Integer sales;
    
    /**
     * Status: 0=off shelf, 1=on shelf
     */
    private Integer status;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
