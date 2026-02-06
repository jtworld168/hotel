package com.hotel.vending.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User Entity
 */
@Data
@TableName("user")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String password;
    
    private String nickname;
    
    private String phone;
    
    private String email;
    
    private String avatar;
    
    /**
     * Role: customer, employee, admin
     */
    private String role;
    
    /**
     * Employee discount rate (0.80 = 20% off)
     */
    private BigDecimal employeeDiscount;
    
    /**
     * Status: 0=disabled, 1=enabled
     */
    private Integer status;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
