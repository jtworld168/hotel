package com.hotel.vending.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order Entity
 */
@Data
@TableName("`order`")
public class Order {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderNo;
    
    private Long userId;
    
    /**
     * Total amount
     */
    private BigDecimal totalAmount;
    
    /**
     * Employee discount amount
     */
    private BigDecimal discountAmount;
    
    /**
     * Coupon discount amount
     */
    private BigDecimal couponAmount;
    
    /**
     * Actual payment amount
     */
    private BigDecimal payAmount;
    
    /**
     * Payment type: wechat, alipay
     */
    private String payType;
    
    private LocalDateTime payTime;
    
    /**
     * Status: 0=pending, 1=paid, 2=shipped, 3=completed, 4=cancelled
     */
    private Integer status;
    
    private Long userCouponId;
    
    private String remark;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
