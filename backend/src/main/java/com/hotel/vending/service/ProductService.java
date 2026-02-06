package com.hotel.vending.service;

import com.hotel.vending.entity.Product;
import com.hotel.vending.vo.ProductVO;
import java.util.List;

/**
 * Product Service Interface
 */
public interface ProductService {
    
    /**
     * Get all products with employee pricing
     */
    List<ProductVO> getAllProducts();
    
    /**
     * Get product by ID with employee pricing
     */
    ProductVO getProductById(Long id);
    
    /**
     * Get products by category
     */
    List<ProductVO> getProductsByCategory(Long categoryId);
    
    /**
     * Search products
     */
    List<ProductVO> searchProducts(String keyword);
}
