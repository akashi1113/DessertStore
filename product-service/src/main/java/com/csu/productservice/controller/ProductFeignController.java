package com.csu.productservice.controller;

import com.csu.productservice.dto.CommodityResponse;
import com.csu.productservice.entity.Category;
import com.csu.productservice.entity.Item;
import com.csu.productservice.entity.Product;
import com.csu.productservice.service.CategoryService;
import com.csu.productservice.service.ItemService;
import com.csu.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品服务Feign接口控制器
 * 提供给其他微服务（如order-service）调用的内部接口
 */
@RestController
@RequestMapping("/feign")  // ⚠️ 使用 /feign 前缀，与普通API区分
public class ProductFeignController {

    private static final Logger logger = LoggerFactory.getLogger(ProductFeignController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // ========================================
    // Item（商品项）相关接口
    // ========================================

    /**
     * 根据ID获取商品项信息
     * 供订单服务创建订单时调用
     *
     * @param itemId 商品项ID
     * @return 商品项实体（直接返回实体，不包装Response）
     */
    @GetMapping("/items/{itemId}")
    public Item getItemById(@PathVariable String itemId) {
        logger.info("[Feign调用] 获取商品项信息, itemId: {}", itemId);

        CommodityResponse<Item> response = itemService.searchItem(itemId);

        if (response == null || response.getData() == null) {
            logger.error("[Feign调用] 商品项不存在, itemId: {}", itemId);
            throw new RuntimeException("商品项不存在: " + itemId);
        }

        Item item = response.getData();
        logger.info("[Feign调用] 商品项信息: id={}, name={}, price={}, stock={}",
                item.getItemId(), item.getName(), item.getPrice(), item.getStock());

        return item;
    }


    /**
     * 扣减库存
     */
    @PutMapping("/items/{itemId}/reduce-stock")
    public Map<String, Object> reduceStock(
            @PathVariable String itemId,
            @RequestParam Integer quantity) {

        logger.info("[Feign调用] 扣减库存, itemId: {}, quantity: {}", itemId, quantity);

        Map<String, Object> result = new HashMap<>();

        try {
            // 🆕 调用真实的库存扣减方法
            boolean success = itemService.reduceStock(itemId, quantity);

            result.put("success", success);
            result.put("message", "库存扣减成功");

            // 查询剩余库存
            CommodityResponse<Item> response = itemService.searchItem(itemId);
            if (response.getData() != null) {
                result.put("remainingStock", response.getData().getStock());
            }

            logger.info("[Feign调用] 库存扣减成功, itemId: {}", itemId);

        } catch (RuntimeException e) {
            logger.error("[Feign调用] 库存扣减失败", e);
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }

    /**
     * 回滚库存
     */
    @PutMapping("/items/{itemId}/restore-stock")
    public Map<String, Object> restoreStock(
            @PathVariable String itemId,
            @RequestParam Integer quantity) {

        logger.info("[Feign调用] 回滚库存, itemId: {}, quantity: {}", itemId, quantity);

        Map<String, Object> result = new HashMap<>();

        try {
            // 🆕 调用真实的库存增加方法
            boolean success = itemService.increaseStock(itemId, quantity);

            result.put("success", success);
            result.put("message", "库存回滚成功");

            logger.info("[Feign调用] 库存回滚成功, itemId: {}", itemId);

        } catch (RuntimeException e) {
            logger.error("[Feign调用] 库存回滚失败", e);
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }

    /**
     * 检查库存
     */
    @GetMapping("/items/{itemId}/check-stock")
    public Map<String, Object> checkStock(
            @PathVariable String itemId,
            @RequestParam Integer quantity) {

        logger.info("[Feign调用] 检查库存, itemId: {}, quantity: {}", itemId, quantity);

        Map<String, Object> result = new HashMap<>();

        // 🆕 调用真实的库存检查方法
        boolean available = itemService.checkStock(itemId, quantity);

        result.put("available", available);
        result.put("requiredStock", quantity);

        // 获取当前库存
        CommodityResponse<Item> response = itemService.searchItem(itemId);
        if (response.getData() != null) {
            result.put("currentStock", response.getData().getStock());
        } else {
            result.put("currentStock", 0);
        }

        if (!available) {
            result.put("message", "库存不足");
        }

        logger.info("[Feign调用] 库存检查结果: {}", result);
        return result;
    }

    // ========================================
    // Product（商品）相关接口
    // ========================================

    /**
     * 根据ID获取商品信息
     *
     * @param productId 商品ID
     * @return 商品实体
     */
    @GetMapping("/products/{productId}")
    public Product getProductById(@PathVariable String productId) {
        logger.info("[Feign调用] 获取商品信息, productId: {}", productId);

        CommodityResponse<Product> response = productService.getProduct(productId);

        if (response == null || response.getData() == null) {
            logger.error("[Feign调用] 商品不存在, productId: {}", productId);
            throw new RuntimeException("商品不存在: " + productId);
        }

        return response.getData();
    }

    /**
     * 根据分类ID获取商品列表
     *
     * @param categoryId 分类ID
     * @return 商品列表
     */
    @GetMapping("/products/by-category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable String categoryId) {
        logger.info("[Feign调用] 根据分类获取商品列表, categoryId: {}", categoryId);

        CommodityResponse<List<Product>> response = productService.getProductListByCategory(categoryId);

        if (response == null || response.getData() == null) {
            logger.warn("[Feign调用] 该分类下没有商品, categoryId: {}", categoryId);
            return Collections.emptyList();
            // 返回空列表
        }

        return response.getData();
    }

    // ========================================
    // Category（分类）相关接口
    // ========================================

    /**
     * 根据ID获取分类信息
     *
     * @param categoryId 分类ID
     * @return 分类实体
     */
    @GetMapping("/categories/{categoryId}")
    public Category getCategoryById(@PathVariable String categoryId) {
        logger.info("[Feign调用] 获取分类信息, categoryId: {}", categoryId);

        CommodityResponse<Category> response = categoryService.getById(categoryId);

        if (response == null || response.getData() == null) {
            logger.error("[Feign调用] 分类不存在, categoryId: {}", categoryId);
            throw new RuntimeException("分类不存在: " + categoryId);
        }

        return response.getData();
    }

    /**
     * 获取所有分类列表
     *
     * @return 分类列表
     */
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        logger.info("[Feign调用] 获取所有分类列表");

        CommodityResponse<List<Category>> response = categoryService.getCategoryList();

        if (response == null || response.getData() == null) {
            return Collections.emptyList();
        }

        return response.getData();
    }

    // ========================================
    // 健康检查接口
    // ========================================

    /**
     * Feign接口健康检查
     *
     * @return 服务状态
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("service", "product-service");
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        return health;
    }
}