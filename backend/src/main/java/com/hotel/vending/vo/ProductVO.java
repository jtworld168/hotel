package com.hotel.vending.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Product VO with calculated price based on user role
 */
@Data
public class ProductVO {
    
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private String image;
    private BigDecimal originalPrice;
    private BigDecimal currentPrice; // Price for current user (considering employee discount)
    private Integer stock;
    private Integer sales;
    private Integer status;
    private Boolean hasDiscount; // Whether employee discount is applied
}
