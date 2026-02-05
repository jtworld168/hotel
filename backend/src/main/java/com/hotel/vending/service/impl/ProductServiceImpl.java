package com.hotel.vending.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotel.vending.entity.Product;
import com.hotel.vending.entity.User;
import com.hotel.vending.mapper.ProductMapper;
import com.hotel.vending.mapper.UserMapper;
import com.hotel.vending.service.ProductService;
import com.hotel.vending.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Product Service Implementation
 * Feature 1: Employee internal pricing
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    
    @Override
    public List<ProductVO> getAllProducts() {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getStatus, 1);
        List<Product> products = productMapper.selectList(queryWrapper);
        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductVO getProductById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        return convertToVO(product);
    }
    
    @Override
    public List<ProductVO> getProductsByCategory(Long categoryId) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getCategoryId, categoryId)
                    .eq(Product::getStatus, 1);
        List<Product> products = productMapper.selectList(queryWrapper);
        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductVO> searchProducts(String keyword) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Product::getName, keyword)
                    .or()
                    .like(Product::getDescription, keyword);
        queryWrapper.eq(Product::getStatus, 1);
        List<Product> products = productMapper.selectList(queryWrapper);
        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Product to ProductVO with employee pricing
     */
    private ProductVO convertToVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        vo.setOriginalPrice(product.getPrice());
        
        // Calculate price based on user role (Feature 1: Employee Pricing)
        BigDecimal currentPrice = product.getPrice();
        boolean hasDiscount = false;
        
        if (StpUtil.isLogin()) {
            Long userId = StpUtil.getLoginIdAsLong();
            User user = userMapper.selectById(userId);
            
            if (user != null && "employee".equals(user.getRole())) {
                // Check if product has a specific employee price
                if (product.getEmployeePrice() != null) {
                    currentPrice = product.getEmployeePrice();
                    hasDiscount = true;
                } 
                // Otherwise use employee discount rate
                else if (user.getEmployeeDiscount() != null) {
                    currentPrice = product.getPrice().multiply(user.getEmployeeDiscount());
                    hasDiscount = true;
                }
            }
        }
        
        vo.setCurrentPrice(currentPrice);
        vo.setHasDiscount(hasDiscount);
        
        return vo;
    }
}
