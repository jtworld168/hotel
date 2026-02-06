package com.hotel.vending.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.hotel.vending.common.Result;
import com.hotel.vending.service.ProductService;
import com.hotel.vending.vo.ProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product Controller
 */
@Tag(name = "Product Management")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    @Operation(summary = "Get all products (with employee pricing)")
    @GetMapping("/list")
    public Result<List<ProductVO>> getAllProducts() {
        List<ProductVO> products = productService.getAllProducts();
        return Result.success(products);
    }
    
    @Operation(summary = "Get product by ID (with employee pricing)")
    @GetMapping("/{id}")
    public Result<ProductVO> getProductById(@PathVariable Long id) {
        ProductVO product = productService.getProductById(id);
        return Result.success(product);
    }
    
    @Operation(summary = "Get products by category")
    @GetMapping("/category/{categoryId}")
    public Result<List<ProductVO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductVO> products = productService.getProductsByCategory(categoryId);
        return Result.success(products);
    }
    
    @Operation(summary = "Search products")
    @GetMapping("/search")
    public Result<List<ProductVO>> searchProducts(@RequestParam String keyword) {
        List<ProductVO> products = productService.searchProducts(keyword);
        return Result.success(products);
    }
}
