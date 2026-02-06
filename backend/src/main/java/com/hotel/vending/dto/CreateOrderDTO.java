package com.hotel.vending.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * Create Order DTO
 */
@Data
public class CreateOrderDTO {
    
    @NotNull(message = "Product IDs cannot be empty")
    private Long[] productIds;
    
    @NotNull(message = "Quantities cannot be empty")
    private Integer[] quantities;
    
    private Long userCouponId;
    
    @NotNull(message = "Payment type cannot be empty")
    private String payType; // wechat or alipay
    
    private String remark;
}
