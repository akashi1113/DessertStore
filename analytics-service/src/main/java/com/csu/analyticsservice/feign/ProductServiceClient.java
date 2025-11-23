package com.csu.analyticsservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    /**
     * 获取所有分类
     * 对应：GET /feign/categories
     */
    @GetMapping("/feign/categories")
    List<Map<String, Object>> getAllCategories();

    /**
     * 根据分类ID获取商品列表
     * 对应：GET /feign/products/by-category/{categoryId}
     */
    @GetMapping("/feign/products/by-category/{categoryId}")
    List<Map<String, Object>> getProductsByCategory(@PathVariable("categoryId") String categoryId);

    /**
     * 根据商品ID获取商品信息
     * 对应：GET /feign/products/{productId}
     */
    @GetMapping("/feign/products/{productId}")
    Map<String, Object> getProductById(@PathVariable("productId") String productId);

    /**
     * 根据商品项ID获取信息
     * 对应：GET /feign/items/{itemId}
     */
    @GetMapping("/feign/items/{itemId}")
    Map<String, Object> getItemById(@PathVariable("itemId") String itemId);

    /**
     * 扣减库存
     * 对应：PUT /feign/items/{itemId}/reduce-stock
     */
    @PutMapping("/feign/items/{itemId}/reduce-stock")
    Map<String, Object> reduceStock(
            @PathVariable("itemId") String itemId,
            @RequestParam("quantity") Integer quantity);

    /**
     * 恢复库存
     * 对应：PUT /feign/items/{itemId}/restore-stock
     */
    @PutMapping("/feign/items/{itemId}/restore-stock")
    Map<String, Object> restoreStock(
            @PathVariable("itemId") String itemId,
            @RequestParam("quantity") Integer quantity);

    /**
     * 检查库存
     * 对应：GET /feign/items/{itemId}/check-stock
     */
    @GetMapping("/feign/items/{itemId}/check-stock")
    Map<String, Object> checkStock(
            @PathVariable("itemId") String itemId,
            @RequestParam("quantity") Integer quantity);

    /**
     * 健康检查
     * 对应：GET /feign/health
     */
    @GetMapping("/feign/health")
    Map<String, Object> health();
}
