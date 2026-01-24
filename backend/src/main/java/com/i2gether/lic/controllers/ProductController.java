package com.i2gether.lic.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2gether.lic.models.InsuranceProduct;
import com.i2gether.lic.models.InsuranceProduct.ProductType;
import com.i2gether.lic.services.ProductService;

@RestController
@RequestMapping("/api/v1/agent/products")
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get all available insurance products
     */
    @GetMapping
    public List<InsuranceProduct> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Get product by type
     */
    @GetMapping("/{productType}")
    public InsuranceProduct getProductByType(@PathVariable String productType) {
        try {
            ProductType type = ProductType.valueOf(productType.toUpperCase());
            return productService.getProductByType(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product type: " + productType);
        }
    }

    /**
     * Search products by keyword
     */
    @GetMapping("/search")
    public List<InsuranceProduct> searchProducts(String keyword) {
        return productService.searchProducts(keyword);
    }
}

