package com.hotel.vending.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Coupon Entity
 */
@Data
@TableName("coupon")
public class Coupon {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    /**
     * Type: full_reduction, discount, fixed
     */
    private String type;
    
    /**
     * Minimum order amount
     */
    private BigDecimal conditionAmount;
    
    /**
     * Discount amount (for full_reduction and fixed)
     */
    private BigDecimal discountAmount;
    
    /**
     * Discount rate (for discount type, 0.80 = 20% off)
     */
    private BigDecimal discountRate;
    
    private Integer totalCount;
    
    private Integer receivedCount;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private Integer status;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
